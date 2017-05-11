package sivan.yue.nlp.common.dataAlgo.matrix;

/**
 * description : 二维矩阵操作接口
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public interface IDMatrix {
    /**
     * description : 获取矩阵row行col列的值
     * @param row 矩阵的行数
     * @param col 矩阵的列数
     * @return 矩阵指定位置的值
     */
    public double get(int row, int col);

    /**
     * description : row行col列存入值val
     * @param row 指定的矩阵的行
     * @param col 指定的矩阵的列
     * @param val 存入矩阵的值
     * @param d 默认值，用于稀疏矩阵
     */
    public void put(int row, int col, double val, double d);

    /**
     * description : 求矩阵的幂，必须是方阵
     * @param n 幂的次数
     * @return 返回元矩阵的n次幂
     */
    public IDMatrix power(int n);

    /**
     * description : 矩阵相乘
     * @param matrix 矩阵B
     * @return 两个矩阵相乘的结果矩阵
     */
    public IDMatrix mul(IDMatrix matrix);

    /**
     * description : 矩阵相加
     * @param matrix 矩阵B
     * @return 连个矩阵相加的结果矩阵
     */
    public IDMatrix add(IDMatrix matrix);

    /**
     * description : 矩阵相减
     * @param matrix 矩阵B
     * @return 当前矩阵减去矩阵B的结果矩阵
     */
    public IDMatrix sub(IDMatrix matrix);

    /**
     * description : 获取行数
     * @return 矩阵的行数
     */
    public int getColNum();

    /**
     * description : 获取列数
     * @return 矩阵的列数
     */
    public int getRowNum();

    public double getDef();

    public void setDef(double def);

    /**
     * description : 创建一个和自己一样的空矩阵
     * @return 新创建的空矩阵
     */
    public IDMatrix cloneSelf();

    /**
     * description : 打印矩阵内容
     */
    public void display();

    /**
     * description : 导出数据到指定文件
     * @param fileName 文件名
     */
    public void export(String fileName);

    /**
     * description : 从文件中导入数据
     * @param fileName 文件名
     */
    public void load(String fileName);
}
