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
public class MeTrain implements ITrain{

    private DFeather feathers;

    private JointMarginalRate preRate;

    private ConditionalDRate conRate;

    public MeTrain() {
        feathers = new DFeather();
        preRate = new JointMarginalRate();
        conRate = new ConditionalDRate();
    }

    @Override
    public void train(String org, String dst, int aNum, int bNum) {
        conRate.setRate(new SparseDMatrix(aNum, bNum));
        preRate.setDRate(new SparseDMatrix(aNum, bNum));
        preRate.setSRate(new SparseDMatrix(aNum, 1));
        feathers.setMatrix(new SparseDMatrix(aNum, bNum));
        preRateTrain(org, aNum, bNum);
        trainModule(aNum, bNum);
        export(dst);
    }

    @Override
    public void export(String dst) {
        String fName = dst + "/" + CConst.ME_NAME;
        FileLineWriter fWriter = new FileLineWriter(fName);
        int iNum = conRate.getRate().getRowNum();
        int jNum = conRate.getRate().getColNum();
        for (int i = 0; i < iNum; ++i) {
            for (int j = 0; j < jNum; ++j) {
                double rate = conRate.rate(i, j);
                String str = i +"|" + j + "|" + rate;
                fWriter.writeLine(str);
            }
        }
        fWriter.close();
    }

    private void trainModule(int aNum, int bNum) {
        while (condition()) {
            // ���㵱ǰϵ���µĸ���ģ��
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < bNum; ++j) {
                    double val = Math.exp(feathers.lambda(i, j));
                    conRate.setRate(i, j, val);
                    sum += val;
                }
                for (int j = 0; j < bNum; ++j) {
                    conRate.setRate(i, j, conRate.rate(i, j)/sum);
                }
            }
            // ����ÿһ������
            for (DMetaFeather feather : feathers) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = feathers.lambda(x, y);
                // ������������������
                double pE = preRate.rate(x, y);
                // ����������ģ������
                double rE = preRate.rate(x)*conRate.rate(x, y);
                feathers.setLam(x, y, oldLam + Math.log(pE/rE));
            }
        }
    }

    private boolean condition() {
        // TODO
        return true;
    }

    /**
     * description : ����ѵ�����������������
     * @param org
     */
    private void preRateTrain(String org, int aNum, int bNum) {
        try {
            int count = 0;
            String fileName = org + "/" + CConst.ME_FILE_NAME;
            Map<Integer, Map<Integer, Integer>> filter = new HashMap<>();
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length != 2) {
                    continue;
                }
                int view = Integer.parseInt(strArr[0]);
                int state = Integer.parseInt(strArr[1]);
                if (filter.get(view) == null) {
                    feathers.addFeather(view, state);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(state, 1);
                    filter.put(view, tmp);
                }
                else if (filter.get(view).get(state) == null){
                    feathers.addFeather(view, state);
                    filter.get(view).put(state, 1);
                }
                preRate.setRate(view, preRate.rate(view) + 1);
                preRate.setRate(view, state, preRate.rate(view, state) + 1);
                count ++;
            }
            for (int i = 0; i < aNum; ++i) {
                preRate.setRate(i, preRate.rate(i)/count);
                for (int j = 0; j < bNum; ++j) {
                    preRate.setRate(i, j, preRate.rate(i, j) / count);
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
