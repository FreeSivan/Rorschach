package sivan.yue.nlp.common.dataAlgo.matrix.towDimMatrix;

import sivan.yue.nlp.common.dataAlgo.matrix.BaseDMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;

/**
 * description : 稠密矩阵， 数组表示法， 占用连续内存
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public class DenseDMatrix extends BaseDMatrix {

    private double[][] data;

    public DenseDMatrix(int m, int n) {
        super(m, n);
        data = new double[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                data[i][j] = 0;
            }
        }
    }

    @Override
    protected double getV(int row, int col) {
        return data[row][col];
    }

    @Override
    protected void putV(int row, int col, double val, double d) {
        data[row][col] = val;
    }

    @Override
    public double getDef() {
        return 0;
    }

    @Override
    public void setDef(double def) {
    }

    @Override
    public IDMatrix cloneSelf() {
        return new DenseDMatrix(getRowNum(), getColNum());
    }

    @Override
    public void export(String fileName) {

    }

    @Override
    public void load(String fileName) {

    }

}
