package sivan.yue.nlp.train.model;

import sivan.yue.nlp.common.dataAlgo.feather.meta.DMetaFeather;
import sivan.yue.nlp.common.dataAlgo.feather.meta.TMetaFeather;
import sivan.yue.nlp.common.dataAlgo.feather.model.DFeather;
import sivan.yue.nlp.common.dataAlgo.feather.model.TFeather;
import sivan.yue.nlp.common.dataAlgo.matrix.thrDimMatrix.SparseTMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.towDimMatrix.SparseDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalTRate;
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
public class CrfTrain implements ITrain{

    /**
     * 状态特征
     */
    private DFeather dFeather;

    /**
     * 转移特征
     */
    private TFeather tFeather;

    /**
     * 先验概率
     */
    private JointMarginalRate preRate;

    /**
     * 训练的三维条件概率模型
     */
    private ConditionalTRate conRate;

    public CrfTrain() {
        dFeather = new DFeather();
        tFeather = new TFeather();
        preRate = new JointMarginalRate();
        conRate = new ConditionalTRate();
    }

    @Override
    public void train(String org, String dst, int aNum, int bNum) {
        // yi yi-1 xi
        conRate.setRate(new SparseTMatrix(aNum, aNum, bNum));
        // 先验概率
        preRate.setTRate(new SparseTMatrix(aNum, aNum, bNum));
        // 边缘概率
        preRate.setSRate(new SparseDMatrix(aNum, 1));
        // 转移特征矩阵 对应特征 yi-1, yi, xi
        tFeather.setMatrix(new SparseTMatrix(aNum, aNum, bNum));
        // 状态特征矩阵 对应yi, xi
        dFeather.setMatrix(new SparseDMatrix(aNum, bNum));
        preRateTrain(org, aNum, bNum);
        trainModule(aNum, bNum);
        export(dst);
    }

    @Override
    public void export(String dst) {
        String fName = dst + "/" + CConst.CRF_NAME;
        FileLineWriter fWriter = new FileLineWriter(fName);
        int iNum = conRate.getRate().getXNum();
        int jNum = conRate.getRate().getYNum();
        int kNum = conRate.getRate().getZNum();
        for (int i = 0; i < iNum; ++i) {
            for (int j = 0; j < jNum; ++j) {
                for (int k = 0; k < kNum; ++k) {
                    double rate = conRate.rate(i, j, k);
                    String str = i +"|" + j + "|"+k + "|" + rate;
                    fWriter.writeLine(str);
                }
            }
        }
        fWriter.close();
    }

    private void trainModule(int aNum, int bNum) {
        while (condition()) {
            // 计算当前系数下的概率模型
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < bNum; ++j) {
                    for (int k = 0; k < bNum; ++k) {
                        double val = Math.exp(tFeather.lambda(i, j, k)+dFeather.lambda(i, k));
                        conRate.setRate(i, j, k, val);
                        sum += val;
                    }
                }
                for (int j = 0; j < bNum; ++j) {
                    for (int k = 0; k < bNum; ++k) {
                        conRate.setRate(i, j, k, conRate.rate(i, j, k) / sum);
                    }
                }
            }
            // 迭代每一个转移特征
            for (TMetaFeather feather : tFeather) {
                int x = feather.getX();
                int y1 = feather.getY();
                int y2 = feather.getZ();
                double oldLam = tFeather.lambda(x, y1, y2);
                // 计算特征的先验期望
                double pE = preRate.rate(x, y1, y2);
                // 计算特征的模型期望
                double rE = preRate.rate(x)*conRate.rate(x, y1, y2);
                tFeather.setLam(x, y1, y2, oldLam + Math.log(pE/rE));
            }
            // 迭代每一个状态特征
            for (DMetaFeather feather : dFeather) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = dFeather.lambda(x, y);
                // TODO
            }
        }
    }

    private boolean condition() {
        return true;
    }

    private void preRateTrain(String org, int aNum, int bNum) {
        try {
            int count = 0;
            String fileName = org + "/" + CConst.CRF_FILE_NAME;
            Map<Integer, Map<Integer, Map<Integer, Integer>>> filter = new HashMap<>();
            Map<Integer, Map<Integer, Integer>> filter1 = new HashMap<>();
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length != 3) {
                    continue;
                }
                int x = Integer.parseInt(strArr[0]);
                int y1 = Integer.parseInt(strArr[1]);
                int y2 = Integer.parseInt(strArr[2]);
                if (filter1.get(x) == null) {
                    dFeather.addFeather(x, y2);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(y2, 1);
                    filter1.put(x, tmp);
                }
                else if (filter1.get(x).get(y2) == null) {
                    dFeather.addFeather(x, y2);
                    filter1.get(x).put(y2, 1);
                }
                if (filter.get(x) == null) {
                    tFeather.addFeather(x, y1, y2);
                    Map<Integer, Map<Integer, Integer>> tmp = new HashMap<>();
                    Map<Integer, Integer> tmp1 = new HashMap<>();
                    tmp1.put(y1, 1);
                    tmp.put(y2, tmp1);
                    filter.put(x, tmp);
                }
                else if (filter.get(x).get(y1) == null){
                    tFeather.addFeather(x, y1, y2);
                    Map<Integer, Integer> tmp1 = new HashMap<>();
                    tmp1.put(y2, 1);
                    filter.get(x).put(y1, tmp1);
                }
                else if (filter.get(x).get(y1).get(y2) == null){
                    tFeather.addFeather(x, y1, y2);
                    filter.get(x).get(y1).put(y2, 1);
                }
                preRate.setRate(x, preRate.rate(x) + 1);
                preRate.setRate(x, y1, y2, preRate.rate(x, y1, y2) + 1);
                count ++;
            }
            for (int i = 0; i < aNum; ++i) {
                preRate.setRate(i,preRate.rate(i) / count);
                for (int j = 0; j < aNum; ++j) {
                    for (int k = 0; k < bNum; ++k) {
                        preRate.setRate(i, j, k, preRate.rate(i, j, k) / count);
                    }
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
