package sivan.yue.nlp.train.model;

import sivan.yue.nlp.common.dataAlgo.feather.meta.TMetaFeather;
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
public class MemmTrain implements ITrain{

    private TFeather feathers;

    private JointMarginalRate preRate;

    private ConditionalTRate resRate;

    public MemmTrain() {
        feathers = new TFeather();
        preRate = new JointMarginalRate();
        resRate = new ConditionalTRate();
    }

    /**
     * description : 训练词性解析模型
     * @param org 样本数据地址
     * @param dst 结果输出地址
     * @param sNum 词性数目
     * @param vNum 词数目
     */
    @Override
    public void train(String org, String dst, int sNum, int vNum) {
        // xi yi-1 yi
        resRate.setRate(new SparseTMatrix(vNum, sNum, sNum, 1/(double)sNum));
        // 先验概率
        preRate.setTRate(new SparseTMatrix(vNum, sNum, sNum, 0));
        // 边缘概率xi, yi-1
        preRate.setDRate(new SparseDMatrix(vNum, sNum));
        // 特征矩阵 对应特征 yi-1, yi, xi
        feathers.setMatrix(new SparseTMatrix(vNum, sNum, sNum, 0));
        preRateTrain(org, sNum, vNum);
        trainModule(sNum, vNum);
        export(dst);
    }

    @Override
    public void export(String dst) {
        String fName = dst + "/" + CConst.MEMM_NAME;
        resRate.getRate().export(fName);
    }

    private void trainModule(int sNum, int vNum) {
        // 先算sum
        Map<Integer, Map<Integer, Double>> sum = new HashMap<>();
        Map<Integer, Map<Integer, Map<Integer,Double>>> preValue = new HashMap<>();
        double defSum = sNum;
        // 对于每个存在的输入xi yi-1和输出yi，将上一次乘子置为1
        for (TMetaFeather feather : feathers) {
            int v = feather.getX();
            int p = feather.getY();
            int c = feather.getZ();
            if (preValue.get(v) == null) {
                preValue.put(v, new HashMap<Integer, Map<Integer, Double>>());
            }
            if (preValue.get(v).get(p) == null) {
                preValue.get(v).put(p, new HashMap<Integer, Double>());
            }
            if (preValue.get(v).get(p).get(c) == null) {
                preValue.get(v).get(p).put(c, (double)1);
            }
        }
        int count = 0;
        while (condition()) {
            // 迭代每一个特征，先计算对应每个输入xi和yi-1的sum
            for (TMetaFeather feather : feathers) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                double lam = feathers.lambda(v, p, c);
                double val = Math.exp(lam);
                resRate.setRate(v, p, c, val, 1);
                // 计算每个sum，先判断v，p是否存在
                Map<Integer, Double> tmpSum = sum.get(v);
                if (tmpSum == null) {
                    tmpSum = new HashMap<>();
                    double value = defSum - 1 + val;
                    tmpSum.put(p, value);
                    sum.put(v, tmpSum);
                }
                else if (tmpSum.get(p) == null) {
                    double value = defSum - 1 + val;
                    tmpSum.put(p, value);
                }
                else {
                    double value = sum.get(v).get(p);
                    value = value - preValue.get(v).get(p).get(c) + val;
                    sum.get(v).put(p, value);
                    preValue.get(v).get(p).put(c, val);
                }
            }
            // 迭代每一个特征，计算当前概率
            for (TMetaFeather feather : feathers) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                double curSum = sum.get(v).get(p);
                resRate.setRate(v, p, c, resRate.rate(v, p, c)/curSum, 1/curSum);
            }
            // 根据当前概率
            for (TMetaFeather feather : feathers) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                double oldLam = feathers.lambda(v, p, c);
                // 计算特征的先验期望
                double pE = preRate.rate(v, p, c);
                // 计算特征的模型期望
                double rE = preRate.rate(v, p)*resRate.rate(v, p, c);
                feathers.setLam(v, p, c, oldLam + Math.log(pE/rE));
            }
            count ++;
            System.out.println("count = " + count);
            if (count > 15000) {
                break;
            }
        }
    }

    private boolean condition() {
        return true;
    }

    private void preRateTrain(String org, int sNum, int vNum) {
        try {
            int count = 0;
            String fileName = org + "/" + CConst.MEMM_FILE_NAME;
            Map<Integer, Map<Integer, Map<Integer, Integer>>> filter = new HashMap<>();
            for (String str : FileIteratorUtil.readLines(fileName)) {
                count ++;
            }
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length != 3) {
                    continue;
                }
                int view = Integer.parseInt(strArr[0]);
                int cState = Integer.parseInt(strArr[1]);
                int pState = Integer.parseInt(strArr[2]);
                if (view < 0 || cState < 0 || pState < 0) {
                    continue;
                }
                System.out.println("view = " + view + " cState = " + cState + " pState = " + pState);
                if (filter.get(view) == null) {
                    feathers.addFeather(view, pState, cState);
                    Map<Integer, Map<Integer, Integer>> tmp = new HashMap<>();
                    Map<Integer, Integer> tmp1 = new HashMap<>();
                    tmp1.put(cState, 1);
                    tmp.put(pState, tmp1);
                    filter.put(view, tmp);
                }
                else if (filter.get(view).get(pState) == null){
                    feathers.addFeather(view, pState, cState);
                    Map<Integer, Integer> tmp1 = new HashMap<>();
                    tmp1.put(cState, 1);
                    filter.get(view).put(pState, tmp1);
                }
                else if (filter.get(view).get(pState).get(cState) == null){
                    feathers.addFeather(view, pState, cState);
                    filter.get(view).get(pState).put(cState, 1);
                }
                // 关于输入的边缘概率的先验概率
                preRate.setRate(view, pState, preRate.rate(view, pState) + 1);
                // 关于输入和输出的联合概率的先验概率
                preRate.setRate(view, pState, cState, preRate.rate(view, pState, cState) + 1, 0);
                count ++;
            }
            Map<Integer, Map<Integer, Integer>> filter1 = new HashMap<>();
            for (TMetaFeather feather : feathers) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                if (filter1.get(v) == null) {
                    preRate.setRate(v, p, preRate.rate(v, p) / count);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(p, 1);
                    filter1.put(v, tmp);
                }
                else if (filter1.get(v).get(p) == null) {
                    preRate.setRate(v, p, preRate.rate(v, p) / count);
                    filter1.get(v).put(p, 1);
                }
                preRate.setRate(v, p, c, preRate.rate(v, p, c) / count, 0);
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
