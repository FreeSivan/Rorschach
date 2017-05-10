package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.IDRate;
import sivan.yue.nlp.common.dataAlgo.probability.ISRate;
import sivan.yue.nlp.common.exception.MatrixErrException;
import sivan.yue.nlp.common.exception.ParamErrException;

import java.util.HashMap;
import java.util.Map;

/**
 * description : 马尔可夫概率模型封装类
 *
 * rate(a) : P(a|init) 获取状态a的初始概率
 *
 * rate(a,b) : P(b|a) 获取状态a到b的一步转移概率
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public class MarkovChainRate implements ISRate, IDRate{
    /**
     * 状态转移矩阵
     */
    private IDMatrix trans;

    /**
     * 初始状态矩阵
     */
    private IDMatrix init;

    /**
     * 状态数目
     */
    private int stateNum;

    /**
     * 保存N步转移矩阵的map
     */
    private Map<Integer, IDMatrix> transMap;

    public MarkovChainRate() {};
    /**
     * description : 构造函数
     * @param init 初始概率矩阵
     * @param trans 转移概率矩阵
     */
    public MarkovChainRate(IDMatrix init, IDMatrix trans) {
        if (init.getRowNum() != trans.getRowNum()) {
            throw new MatrixErrException("markov error");
        }
        this.init = init;
        this.trans = trans;
        this.stateNum = trans.getRowNum();
        transMap = new HashMap<>();
    }

    /**
     * description : 获取状态a的初始概率
     * @param a 状态a
     * @return 状态a的初始概率
     */
    public double rate (int a) {
        return init.get(a, 0);
    }

    @Override
    public void setRate(int x, double val) {
        init.put(x, 0, val, 0);
    }

    /**
     * description : 求状态a经一步转移到状态b的概率
     * @param a 状态a
     * @param b 状态b
     * @return 状态a经一步转移到状态b的概率
     */
    public double rate(int a, int b) {
        return rate(a, b, 1);
    }

    @Override
    public void setRate(int x, int y, double val, double d) {
        trans.put(x, y, val, d);
    }

    /**
     * description : 求状态a经n步转移到状态b的概率
     * @param a 状态a
     * @param b 状态b
     * @param n 转移的步数
     * @return 状态a经n步转移到状态b的概率
     */
    public double rate(int a, int b, int n) {
        if (n < 1) {
            throw new ParamErrException("n == 0");
        }
        if (transMap.get(n) != null) {
            return transMap.get(n).get(a, b);
        }
        IDMatrix tmp = trans.power(n);
        transMap.put(n, tmp);
        return tmp.get(a, b);
    }

    /**
     *
     * @param trans
     */
    public void setTrans(IDMatrix trans) {
        this.trans = trans;
        this.stateNum = trans.getRowNum();
    }

    /**
     *
     * @param init
     */
    public void setInit(IDMatrix init) {
        this.init = init;
        this.stateNum = init.getRowNum();
    }

    public int getStateNum() {
        return stateNum;
    }
}
