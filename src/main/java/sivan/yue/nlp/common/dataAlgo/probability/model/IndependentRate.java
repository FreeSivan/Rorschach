package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.IDRate;

/**
 * description : 独立随机过程概率模型封装类
 *
 * 存放HMM模型的输出概率矩阵
 *
 * rate(t, x) ：获取随机变量X(t)=x的概率P{X(t)=x}
 *
 * Created by xiwen.yxw on 2017/3/23.
 */
public class IndependentRate implements IDRate{
    /**
     * 概率矩阵
     */
    private IDMatrix rate;

    /**
     * 指标集合的大小
     */
    private int num;

    public IndependentRate() {}

    public IndependentRate(IDMatrix rate) {
        this.rate = rate;
        this.num = rate.getRowNum();
    }

    /**
     * description : 指标为t时状态为x的概率
     * @param t 随机变量X(t)的参数t
     * @param x 随机变量X(t)的状态x
     * @return X(t)取x值的概率
     */
    public double rate(int t, int x) {
        return rate.get(t, x);
    }

    @Override
    public void setRate(int x, int y, double val) {
        rate.put(x, y, val);
    }

    public void setRate(IDMatrix rate) {
        this.rate = rate;
        setNum(rate.getRowNum());
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
