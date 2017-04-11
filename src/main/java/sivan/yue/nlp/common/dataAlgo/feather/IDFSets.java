package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description ：二维特征集合接口
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface IDFSets {
    /**
     * description ：返回x，y特征的系数
     * @param x 特征的x维
     * @param y 特征的y维
     * @return x，y决定的特征的系数
     */
    public double lambda(int x, int y);

    /**
     * description ：设置x，y特征的系数
     * @param x 特征的x维
     * @param y 特征的y维
     * @param val x, y特征的系数
     */
    public void setLam(int x, int y, double val);
}
