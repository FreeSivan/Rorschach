package sivan.yue.nlp.common.dataAlgo.matrix;

/**
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface ITMatrix {
    /**
     * description : 获取矩阵指定位置的值
     * @param x 矩阵的x数
     * @param y 矩阵的y数
     * @param z 矩阵的z数
     * @return 矩阵指定位置的值
     */
    public double get(int x, int y, int z);

    /**
     * description : 指定位置存入值val
     * @param x 指定的矩阵的x值
     * @param y 指定的矩阵的y值
     * @param z 指定的矩阵的z值
     * @param v 存入矩阵的值
     * @param v 存入矩阵的默认值
     */
    public void put(int x, int y, int z, double v, double d);

    /**
     * description : 获取x数
     * @return 矩阵的行数
     */
    public int getXNum();

    /**
     * description : 获取y数
     * @return 矩阵的列数
     */
    public int getYNum();

    /**
     * description : 获取z数
     * @return
     */
    public int getZNum();

    public double getDef();

    public void setDef(double val);

    /**
     * description : 创建一个和自己一样的空矩阵
     * @return 新创建的空矩阵
     */
    public ITMatrix cloneSelf();

    /**
     * description : 到处数据到指定文件
     * @param fileName 文件名
     */
    public void export(String fileName);

    /**
     * description : 从文件中导入数据
     * @param fileName 文件名
     */
    public void load(String fileName);

}
