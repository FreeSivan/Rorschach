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
        int count = 0;
        while (condition()) {
            // 计算当前系数下的概率模型
            long b = System.currentTimeMillis();
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < bNum; ++j) {
                    double val;
                    if (feathers.lambda(i, j) == 0) {
                        val = 1;
                    }
                    else {
                        val = Math.exp(feathers.lambda(i, j));
                    }
                    conRate.setRate(i, j, val);
                    sum += val;
                }
                for (int j = 0; j < bNum; ++j) {
                    conRate.setRate(i, j, conRate.rate(i, j)/sum);
                }
            }
            System.out.println("time = " + (System.currentTimeMillis() - b));

            b = System.currentTimeMillis();
            // 迭代每一个特征
            for (DMetaFeather feather : feathers) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = feathers.lambda(x, y);
                // 计算特征的先验期望
                double pE = preRate.rate(x, y);
                // 计算特征的模型期望
                double rE = preRate.rate(x)*conRate.rate(x, y);
                feathers.setLam(x, y, oldLam + Math.log(pE/rE));
            }
            System.out.println("time = " + (System.currentTimeMillis() - b));
            count ++;
            System.out.println("count = " + count);
            if (count > 50) {
                break;
            }
        }
    }

    private boolean condition() {
        // TODO
        return true;
    }

    /**
     * description : 根据训练样本生成先验概率
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
                preRate.setRate(state, view, preRate.rate(state, view) + 1);
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
