package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.IDRate;

/**
 * description : 二维条件概率模型封装类
 *
 * 用于存储最大熵模型最终结果的概率模型
 *
 * rate(x, y) 标识P(y|x)的概率
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public class ConditionalDRate implements IDRate{
    /**
     * 二维概率矩阵
     */
    private IDMatrix rate;

    public ConditionalDRate() {}

    public ConditionalDRate(IDMatrix rate) {
        this.rate = rate;
    }

    /**
     * description : 查询x发生的情况下y发生的概率
     * @param x 参数x
     * @param y 参数y
     * @return x发生的情况下y的条件概率
     */
    @Override
    public double rate(int x, int y) {
        return rate.get(x, y);
    }

    @Override
    public void setRate(int x, int y, double val) {
        rate.put(x, y, val);
    }

    public void setRate(IDMatrix rate) {
        this.rate = rate;
    }

    public IDMatrix getRate() {
        return this.rate;
    }
}
