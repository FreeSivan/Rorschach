package sivan.yue.nlp.common.dataAlgo.probability;

/**
 * description : 双参数查询概率接口
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface IDRate {
    /**
     * description : 获取参数x，y共同决定的概率
     * @param x 参数x
     * @param y 参数y
     * @return 参数x，y共同决定的概率
     */
    public double rate(int x, int y);

    public void setRate(int x, int y, double val, double def);

    public void setRate(int x, int y, double val);
}
