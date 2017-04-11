package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description ：三维特征集合接口
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface ITFSets {
    /**
     * description ：返回x，y, z特征的系数
     * @param x 特征的x维
     * @param y 特征的y维
     * @param z 特征的z维
     * @return x, y, z决定的特征对应的系数
     */
    public double lambda(int x, int y, int z);

    /**
     * description ：设置x，y, z特征的系数
     * @param x 特征的x维
     * @param y 特征的y维
     * @param z 特征的z维
     * @param val 参数x, y, z决定的特征的系数
     */
    public void setLam(int x, int y, int z, double val);
}
