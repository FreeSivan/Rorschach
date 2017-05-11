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
        conRate.setDDef(1 / (double) aNum);
        preRate.setDRate(new SparseDMatrix(aNum, bNum));
        preRate.setDDef(0);
        preRate.setSRate(new SparseDMatrix(aNum, 1));
        preRate.setSDef(0);
        feathers.setMatrix(new SparseDMatrix(aNum, bNum));
        feathers.setDDef(0);
        preRateTrain(org, aNum, bNum);
        trainModule1(aNum, bNum);
        export1(dst);
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

    public void export1(String dst) {
        String fName = dst + "/" + CConst.ME_NAME;
        conRate.getRate().export(fName);
    }

    private void trainModule(int aNum, int bNum) {
        int count = 0;
        while (true) {
            // ���㵱ǰϵ���µĸ���ģ��
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < bNum; ++j) {
                    double val = Math.exp(feathers.lambda(i, j));
                    conRate.setRate(i, j, val, 0);
                    sum += val;
                }
                for (int j = 0; j < bNum; ++j) {
                    conRate.setRate(i, j, conRate.rate(i, j)/sum, 0);
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
            count ++;
            System.out.println("count = " + count);
            if (count > 50) {
                break;
            }
        }
    }

    private void trainModule1(int sNum, int vNum) {
        Map<Integer, Double> sum = new HashMap<>();
        Map<Integer, Map<Integer, Double>> preValue = new HashMap<>();
        double defSum = vNum;

        // ����ÿ�����ڵ�����xi�����yi������һ�γ�����Ϊ1
        for (DMetaFeather feather : feathers) {
            int x = feather.getX();
            int y = feather.getY();
            if (preValue.get(x) == null) {
                preValue.put(x, new HashMap<Integer, Double>());
            }
            if (preValue.get(x).get(y) == null) {
                preValue.get(x).put(y, (double)1);
            }
        }
        int count = 0;
        while (true) {
            // ����ÿһ���������ȼ����Ӧÿ������xi��sum
            for (DMetaFeather feather : feathers) {
                int x = feather.getX();
                int y = feather.getY();
                double lam = feathers.lambda(x, y);
                double val = Math.exp(lam);
                conRate.setRate(x, y, val, 1);
                // ����ÿ��v��sum�����ж�v��sum�Ƿ����
                Double tmpSum = sum.get(x);
                if (tmpSum == null) {
                    double value = defSum - 1 + val;
                    sum.put(x, value);
                }
                else {
                    double value = sum.get(x);
                    value = value - preValue.get(x).get(y) + val;
                    sum.put(x, value);
                    preValue.get(x).put(y, val);
                }
            }
            // ����ÿһ�����������㵱ǰ����
            conRate.setDDef(1/defSum);
            for (DMetaFeather feather : feathers) {
                int x = feather.getX();
                int y = feather.getY();
                double curSum = sum.get(x);
                conRate.setRate(x, y, conRate.rate(x, y)/curSum, 1/curSum);
            }
            // ���ݵ�ǰ����
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
            count ++;
            System.out.println("count = " + count);
            if (count > 50) {
                break;
            }
        }
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
                if (strArr.length != 3) {
                    continue;
                }
                int view = Integer.parseInt(strArr[0]);
                int state = Integer.parseInt(strArr[1]);
                if (view < 0 || state < 0) {
                    continue;
                }
                if (filter.get(state) == null) {
                    feathers.addFeather(state, view);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(view, 1);
                    filter.put(state, tmp);
                }
                else if (filter.get(state).get(view) == null){
                    feathers.addFeather(state, view);
                    filter.get(state).put(view, 1);
                }
                preRate.setRate(state, preRate.rate(state) + 1);
                preRate.setRate(state, view, preRate.rate(state, view) + 1, 0);
                count ++;
            }
            for (int i = 0; i < aNum; ++i) {
                preRate.setRate(i, preRate.rate(i)/count);
                for (int j = 0; j < bNum; ++j) {
                    preRate.setRate(i, j, preRate.rate(i, j) / count, 0);
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
