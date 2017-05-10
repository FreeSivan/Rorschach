package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.ITMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.ITRate;

/**
 * description : 三维条件概率模型封装类
 *
 * 用于存储条件随机场最终结果的概率模型
 *
 * rate(x, y, z) 标识P(z|xy)的概率
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public class ConditionalTRate implements ITRate{

    /**
     * 三维概率矩阵
     */
    private ITMatrix rate;

    public ConditionalTRate() {}

    public ConditionalTRate(ITMatrix rate) {
        this.rate = rate;
    }

    /**
     * description : 查询xy存在的情况下z发生的概率
     * @param x 参数x
     * @param y 参数y
     * @param z 参数z
     * @return xy存在的情况下z的条件率
     */
    public double rate(int x, int y, int z) {
        return rate.get(x, y, z);
    }

    @Override
    public void setRate(int x, int y, int z, double val, double def) {
        rate.put(x, y, z, val, def);
    }

    public void setRate(ITMatrix rate) {
        this.rate = rate;
    }

    public ITMatrix getRate() {
        return this.rate;
    }

    public void setTDef(double val) {

    }
}
