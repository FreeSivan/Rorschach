package sivan.yue.nlp.common.dataAlgo.probability;

/**
 * description : 三参数查询概率接口
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface ITRate {
    /**
     * description :获取参数x y z共同决定的概率
     * @param x 参数x
     * @param y 参数y
     * @param z 参数z
     * @return 参数x y z共同决定的概率
     */
    public double rate(int x, int y, int z);

    public void setRate(int x, int y, int z, double val, double def);

    public void setRate(int x, int y, int z, double val);
}
