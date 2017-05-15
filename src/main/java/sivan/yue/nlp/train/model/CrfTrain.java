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
    public void train(String org, String dst, int sNum, int vNum) {
        // 结果模型 xi yi-1 y
        conRate.setRate(new SparseTMatrix(vNum, sNum, sNum));
        conRate.setTDef(1/(double)sNum);
        // 先验概率 xi yi-1 y
        preRate.setTRate(new SparseTMatrix(vNum, sNum, sNum));
        preRate.setTDef(0);
        // 边缘概率 xi yi-1
        preRate.setSRate(new SparseDMatrix(vNum, sNum));
        preRate.setSDef(0);
        // 转移特征矩阵 对应特征 xi yi-1 y
        tFeather.setMatrix(new SparseTMatrix(vNum, sNum, sNum));
        tFeather.setTDef(0);
        // 状态特征矩阵 对应xi yi-1
        dFeather.setMatrix(new SparseDMatrix(vNum, sNum));
        dFeather.setDDef(0);
        preRateTrain(org, sNum, vNum);
        trainModule(sNum, vNum);
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
        while (true) {
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
                // 计算特征的先验期望和模型期望
                double pE = 0;
                double rE = 0;
                double count = 0;
                for (int i = 0; i < aNum; ++i) {
                    if (tFeather.lambda(x, y, i) == 0) {
                        continue;
                    }
                    pE += preRate.rate(x, y, i);
                    rE += preRate.rate(x)*conRate.rate(x, y, i);
                    count ++;
                }
                dFeather.setLam(x, y, oldLam + (1/count)*Math.log(pE/rE));
            }
        }
    }

    private void trainModule1(int sNum, int vNum) {
        // 先算sum
        Map<Integer, Map<Integer, Double>> sum = new HashMap<>();
        Map<Integer, Map<Integer, Map<Integer,Double>>> preValue = new HashMap<>();
        double defSum = sNum;
        // 对于每个存在的输入xi yi-1和输出yi，将上一次乘子置为1
        for (TMetaFeather feather : tFeather) {
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
        while (true) {
            // 迭代每一个特征，先计算对应每个输入xi和yi-1的sum
            for (TMetaFeather feather : tFeather) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                double lam = tFeather.lambda(v, p, c);
                double lam2 = dFeather.lambda(v, p);
                double val = Math.exp(lam + lam2);
                conRate.setRate(v, p, c, val, 1);
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
            conRate.setTDef(1/defSum);
            for (TMetaFeather feather : tFeather) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                double curSum = sum.get(v).get(p);
                conRate.setRate(v, p, c, conRate.rate(v, p, c)/curSum, 1/curSum);
            }
            // 根据当前概率
            for (TMetaFeather feather : tFeather) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                double oldLam = tFeather.lambda(v, p, c);
                // 计算特征的先验期望
                double pE = preRate.rate(v, p, c);
                // 计算特征的模型期望
                double rE = preRate.rate(v, p)*conRate.rate(v, p, c);
                tFeather.setLam(v, p, c, oldLam + Math.log(pE/rE));
            }
            // 迭代每一个状态特征
            for (DMetaFeather feather : dFeather) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = dFeather.lambda(x, y);
                // 计算特征的先验期望和模型期望
                double pE = 0;
                double rE = 0;
                double tmp = 0;
                for (int i = 0; i < sNum; ++i) {
                    if (tFeather.lambda(x, y, i) == 0) {
                        continue;
                    }
                    tmp ++;
                    pE += preRate.rate(x, y, i);
                    rE += preRate.rate(x)*conRate.rate(x, y, i);
                }
                dFeather.setLam(x, y, oldLam + (1/tmp)*Math.log(pE/rE));
            }
            count ++;
            System.out.println("count = " + count);
            if (count > 50) {
                break;
            }
        }
    }

    private void preRateTrain(String org, int sNum, int vNum) {
        try {
            int count = 0;
            String fileName = org + "/" + CConst.CRF_FILE_NAME;
            Map<Integer, Map<Integer, Map<Integer, Integer>>> filter = new HashMap<>();
            Map<Integer, Map<Integer, Integer>> dFilter = new HashMap<>();
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length != 3) {
                    continue;
                }
                int view = Integer.parseInt(strArr[0]);
                int cState = Integer.parseInt(strArr[1]);
                int pState = Integer.parseInt(strArr[2]);
                if (dFilter.get(view) == null) {
                    dFeather.addFeather(view, pState);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(pState, 1);
                    dFilter.put(view, tmp);
                }
                else if (dFilter.get(view).get(pState) == null) {
                    dFeather.addFeather(view, pState);
                    dFilter.get(view).put(pState, 1);
                }
                if (filter.get(view) == null) {
                    tFeather.addFeather(view, pState, cState);
                    Map<Integer, Map<Integer, Integer>> tmp = new HashMap<>();
                    Map<Integer, Integer> tmp1 = new HashMap<>();
                    tmp1.put(pState, 1);
                    tmp.put(cState, tmp1);
                    filter.put(view, tmp);
                }
                else if (filter.get(view).get(pState) == null){
                    tFeather.addFeather(view, pState, cState);
                    Map<Integer, Integer> tmp1 = new HashMap<>();
                    tmp1.put(cState, 1);
                    filter.get(view).put(pState, tmp1);
                }
                else if (filter.get(view).get(pState).get(cState) == null){
                    tFeather.addFeather(view, pState, cState);
                    filter.get(view).get(pState).put(cState, 1);
                }
                preRate.setRate(view, pState, preRate.rate(view) + 1);
                preRate.setRate(view, pState, cState, preRate.rate(view, pState, cState) + 1);
                count ++;
            }
            Map<Integer, Map<Integer, Integer>> rateFilter = new HashMap<>();
            for (TMetaFeather feather : tFeather) {
                int v = feather.getX();
                int p = feather.getY();
                int c = feather.getZ();
                if (rateFilter.get(v) == null) {
                    preRate.setRate(v, p, preRate.rate(v, p) / count);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(p, 1);
                    rateFilter.put(v, tmp);
                }
                else if (rateFilter.get(v).get(p) == null) {
                    preRate.setRate(v, p, preRate.rate(v, p) / count);
                    rateFilter.get(v).put(p, 1);
                }
                preRate.setRate(v, p, c, preRate.rate(v, p, c) / count);
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
