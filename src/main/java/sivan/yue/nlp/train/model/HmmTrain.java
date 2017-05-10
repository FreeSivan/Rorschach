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
     * 输出概率模型的先验概率
     */
    private JointMarginalRate showRate;

    /**
     * 转移概率模型的先验概率
     */
    private JointMarginalRate tranRate;

    /**
     * 训练后的马尔克夫模型概率
     */
    private ConditionalDRate tranRes;

    /**
     * 训练后的独立随机模型概率
     */
    private ConditionalDRate showRes;

    /**
     * 输出特征集合
     */
    private DFeather showFeather;

    /**
     * 转移特征集合
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
        // state, view
        showRate.setDRate(new SparseDMatrix(aNum, bNum));
        // state
        showRate.setSRate(new SparseDMatrix(aNum, 1));
        // state, view
        tranRate.setDRate(new SparseDMatrix(aNum, aNum));
        // state
        tranRate.setSRate(new SparseDMatrix(aNum, 1));
        // state, state
        tranRes.setRate(new SparseDMatrix(aNum, aNum));
        // state, view
        showRes.setRate(new SparseDMatrix(aNum, bNum));
        // state, view
        showFeather.setMatrix(new SparseDMatrix(aNum, bNum));
        // state, state
        tranFeather.setMatrix(new SparseDMatrix(aNum, aNum));
        preRateTrain(org, aNum, bNum);
        trainModule(aNum, bNum);
        export(dst);
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
        int count = 0;
        // 训练转移概率模型tranRes
        while (condition()) {
            // 计算当前系数下的概率模型
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < aNum; ++j) {
                    double val = Math.exp(tranFeather.lambda(i, j));
                    tranRes.setRate(i, j, val, 0);
                    sum += val;
                }
                for (int j = 0; j < aNum; ++j) {
                    tranRes.setRate(i, j, tranRes.rate(i, j)/sum, 0);
                }
            }
            // 迭代每一个特征
            for (DMetaFeather feather : tranFeather) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = tranFeather.lambda(x, y);
                // 计算特征的先验期望
                double pE = tranRate.rate(x, y);
                // 计算特征的模型期望
                double rE = tranRate.rate(x)*tranRes.rate(x, y);
                tranFeather.setLam(x, y, oldLam + Math.log(pE/rE));
            }
            count ++;
            System.out.println("count = " + count);
            if (count > 50) {
                break;
            }
        }
        count = 0;
        // 训练转移概率模型showRes
        while (condition()) {
            // 计算当前系数下的概率模型
            for (int i = 0; i < aNum; ++i) {
                int sum = 0;
                for (int j = 0; j < bNum; ++j) {
                    double val = Math.exp(showFeather.lambda(i, j));
                    showRes.setRate(i, j, val, 0);
                    sum += val;
                }
                for (int j = 0; j < bNum; ++j) {
                    showRes.setRate(i, j, showRes.rate(i, j)/sum, 0);
                }
            }
            // 迭代每一个特征
            for (DMetaFeather feather : showFeather) {
                int x = feather.getX();
                int y = feather.getY();
                double oldLam = showFeather.lambda(x, y);
                // 计算特征的先验期望
                double pE = showRate.rate(x, y);
                // 计算特征的模型期望
                double rE = showRate.rate(x)*showRes.rate(x, y);
                showFeather.setLam(x, y, oldLam + Math.log(pE/rE));
            }
            count ++;
            System.out.println("count = " + count);
            if (count > 50) {
                break;
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
                int view = Integer.parseInt(strArr[0]);
                int cState = Integer.parseInt(strArr[1]);
                int pState = Integer.parseInt(strArr[2]);
                if (view < 0 || cState < 0 || pState < 0) {
                    continue;
                }
                // 添加转移特征到特征组
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
                // 添加输出特征到特征组
                if (showFilter.get(cState) == null) {
                    showFeather.addFeather(cState, view);
                    Map<Integer, Integer> tmp = new HashMap<>();
                    tmp.put(view, 1);
                    showFilter.put(cState, tmp);
                }
                else if (showFilter.get(cState).get(view) == null) {
                    showFeather.addFeather(cState, view);
                    showFilter.get(cState).put(view, 1);
                }
                // 转移概率模型的边缘概率的先验概率累加
                tranRate.setRate(pState, tranRate.rate(pState)+1);
                // 转移概率模型的联合概率的先验概率累加
                tranRate.setRate(pState, cState, tranRate.rate(pState, cState)+1, 0);
                // 输出概率模型的边缘概率的先验概率累加
                showRate.setRate(cState, showRate.rate(cState)+1);
                // 输出概率模型的联合概率的先验概率累加
                showRate.setRate(cState, view, showRate.rate(cState, view)+1, 0);
                count ++;
            }
            for (int i = 0; i < aNum; ++i) {
                // 转移概率模型的边缘概率的先验概率
                tranRate.setRate(i, tranRate.rate(i) / count);
                // 输出概率模型的边缘概率的先验概率
                showRate.setRate(i, showRate.rate(i) / count);
                for (int j = 0; j < aNum; ++j) {
                    // 转移概率模型的联合概率的先验概率
                    tranRate.setRate(i, j, tranRate.rate(i, j) / count, 0);
                }
                for (int j = 0; j < bNum; ++j) {
                    // 输出概率模型的联合概率的先验概率
                    showRate.setRate(i, j, showRate.rate(i, j) / count, 0);
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
