package sivan.yue.nlp.train.model;

import sivan.yue.nlp.common.dataAlgo.feather.meta.DMetaFeather;
import sivan.yue.nlp.common.dataAlgo.feather.model.DFeather;
import sivan.yue.nlp.common.dataAlgo.matrix.towDimMatrix.SparseDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalDRate;
import sivan.yue.nlp.common.dataAlgo.probability.model.JointMarginalRate;
import sivan.yue.nlp.common.tools.CConst;
import sivan.yue.nlp.common.tools.FileIteratorUtil;
import sivan.yue.nlp.common.tools.FileLineWriter;
import sivan.yue.nlp.train.ITrain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiwen.yxw on 2017/3/28.
 */
public class HmmTrain implements ITrain{
    /**
     * �������ģ�͵��������
     */
    private JointMarginalRate showRate;

    /**
     * ת�Ƹ���ģ�͵��������
     */
    private JointMarginalRate tranRate;

    /**
     * ѵ���������˷�ģ�͸���
     */
    private ConditionalDRate tranRes;

    /**
     * ѵ����Ķ������ģ�͸���
     */
    private ConditionalDRate showRes;

    /**
     * �����������
     */
    private DFeather showFeather;

    /**
     * ת����������
     */
    private DFeather tranFeather;

    public HmmTrain() {
        showRate = new JointMarginalRate();
        tranRate = new JointMarginalRate();
        tranRes = new ConditionalDRate();
        showRes = new ConditionalDRate();
        showFeather = new DFeather();
        tranFeather = new DFeather();
    }

    @Override
    public void train(String org, String dst, int aNum, int bNum) {
        // yi, xi
        showRate.setDRate(new SparseDMatrix(bNum, aNum));
        // xi
        showRate.setSRate(new SparseDMatrix(bNum, 1));
        // yi-1, yi
        tranRate.setDRate(new SparseDMatrix(bNum, bNum));
        // yi-1
        tranRate.setDRate(new SparseDMatrix(bNum, 1));
        // yi-1, yi
        tranRes.setRate(new SparseDMatrix(bNum, bNum));
        // yi, xi
        showRes.setRate(new SparseDMatrix(bNum, aNum));
        // yi, xi
        showFeather.setMatrix(new SparseDMatrix(bNum, aNum));
        // yi, yi-1
        tranFeather.setMatrix(new SparseDMatrix(bNum, bNum));

        preRateTrain(org, aNum, bNum);

        trainModule(aNum, bNum);
    }

    @Override
    public void export(String dst) {
        String fName = dst + "/" + CConst.HMM_FILE_SHOW;
        String fName1 = dst + "/" + CConst.HMM_FILE_TRAN;
        FileLineWriter fWriter = new FileLineWriter(fName);
        FileLineWriter fWriter1 = new FileLineWriter(fName1);
        int iNum = showRes.getRate().getRowNum();
        int jNum = showRes.getRate().getColNum();
        for (int i = 0; i < iNum; ++i) {
            for (int j = 0; j < jNum; ++j) {
                double rate = showRes.rate(i, j);
                String str = i +"|" + j +  "|" + rate;
                fWriter.writeLine(str);
            }
        }
        iNum = tranRes.getRate().getRowNum();
        jNum = tranRes.getRate().getColNum();
        for (int i = 0; i < iNum; ++i) {
            for (int j = 0; j < jNum; ++j) {
                double rate = tranRes.rate(i, j);
                String str = i +"|" + j + "|" + rate;
                fWriter1.writeLine(str);
            }
        }
        fWriter.close();
        fWriter1.close();
    }

    private void trainModule(int aNum, int bNum) {
        // ѵ��ת�Ƹ���ģ��tranRes
        while (condition()) {
            // ���㵱ǰϵ���µĸ���ģ��
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < bNum; ++j) {
                    double val = Math.exp(tranFeather.lambda(i, j));
                    tranRes.setRate(i, j, val);
                    sum += val;
                }
                for (int j = 0; j < bNum; ++j) {
                    tranRes.setRate(i, j, tranRes.rate(i, j)/sum);
                }
            }
            // ����ÿһ������
            for (DMetaFeather feather : tranFeather) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = tranFeather.lambda(x, y);
                // ������������������
                double pE = tranRate.rate(x, y);
                // ����������ģ������
                double rE = tranRate.rate(x)*tranRes.rate(x, y);
                tranFeather.setLam(x, y, oldLam + Math.log(pE/rE));
            }
        }
        // ѵ��ת�Ƹ���ģ��showRes
        while (condition()) {
            // ���㵱ǰϵ���µĸ���ģ��
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < bNum; ++j) {
                    double val = Math.exp(showFeather.lambda(i, j));
                    showRes.setRate(i, j, val);
                    sum += val;
                }
                for (int j = 0; j < bNum; ++j) {
                    showRes.setRate(i, j, showRes.rate(i, j)/sum);
                }
            }
            // ����ÿһ������
            for (DMetaFeather feather : showFeather) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = showFeather.lambda(x, y);
                // ������������������
                double pE = showRate.rate(x, y);
                // ����������ģ������
                double rE = showRate.rate(x)*showRes.rate(x, y);
                showFeather.setLam(x, y, oldLam + Math.log(pE/rE));
            }
        }
    }

    private boolean condition() {
        return true;
    }

    private void preRateTrain(String org, int aNum, int bNum) {
        try {
            int count = 0;
            String fileName = org + "/" + CConst.HMM_FILE_NAME;
            Map<Integer, Map<Integer,Integer>> tranFilter = new HashMap<>();
            Map<Integer, Map<Integer,Integer>> showFilter = new HashMap<>();
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length != 3) {
                    continue;
                }
                int pState = Integer.parseInt(strArr[0]);
                int cState = Integer.parseInt(strArr[1]);
                int vState = Integer.parseInt(strArr[2]);
                // ���ת��������������
                if(tranFilter.get(pState) == null) {
                    tranFeather.addFeather(pState, cState);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(cState, 1);
                    tranFilter.put(pState, tmp);
                }
                else if (tranFilter.get(pState).get(cState) == null) {
                    tranFeather.addFeather(pState, cState);
                    tranFilter.get(pState).put(cState, 1);
                }
                // ������������������
                if (showFilter.get(cState) == null) {
                    showFeather.addFeather(cState, vState);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(vState, 1);
                    showFilter.put(cState, tmp);
                }
                else if (showFilter.get(cState).get(vState) == null) {
                    showFeather.addFeather(cState, vState);
                    showFilter.get(cState).put(vState, 1);
                }
                // ת�Ƹ���ģ�͵ı�Ե���ʵ���������ۼ�
                tranRate.setRate(pState, tranRate.rate(pState)+1);
                // ת�Ƹ���ģ�͵����ϸ��ʵ���������ۼ�
                tranRate.setRate(pState, cState, tranRate.rate(pState, cState)+1);
                // �������ģ�͵ı�Ե���ʵ���������ۼ�
                showRate.setRate(cState, tranRate.rate(cState)+1);
                // �������ģ�͵����ϸ��ʵ���������ۼ�
                showRate.setRate(cState, vState, tranRate.rate(cState, vState)+1);
                count ++;
            }
            for (int i = 0; i < bNum; ++i) {
                // ת�Ƹ���ģ�͵ı�Ե���ʵ��������
                tranRate.setRate(i, tranRate.rate(i) / count);
                // �������ģ�͵ı�Ե���ʵ��������
                showRate.setRate(i, showRate.rate(i) / count);
                for (int j = 0; j < bNum; ++j) {
                    // ת�Ƹ���ģ�͵����ϸ��ʵ��������
                    tranRate.setRate(i, j, tranRate.rate(i, j) / count);
                }
                for (int j = 0; j < aNum; ++j) {
                    // �������ģ�͵����ϸ��ʵ��������
                    showRate.setRate(i, j, showRate.rate(i, j) / count);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
