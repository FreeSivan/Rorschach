package sivan.yue.nlp.common.dataAlgo.probability;

/**
 * description : 单参数查询概率接口
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface ISRate {
    /**
     * description : 获取参数x的概率
     * @param x 参数x
     * @return 参数x的概率
     */
    public double rate(int x);

    public void setRate(int x, double val);
}
