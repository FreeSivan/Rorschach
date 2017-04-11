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
    protected void putV(int row, int col, double val) {
        data[row][col] = val;
    }

    @Override
    public IDMatrix cloneSelf() {
        return new DenseDMatrix(getRowNum(), getColNum());
    }

    /*
    public static void main(String[] args) {
        DenseDMatrix matrix = new DenseDMatrix(3, 3);
        matrix.put(0, 0, 0.1);
        matrix.put(0, 1, 0.2);
        matrix.put(0, 2, 0.4);
        matrix.put(1, 0, 0.2);
        matrix.put(1, 1, 0.2);
        matrix.put(1, 2, 0.1);
        matrix.put(2, 0, 0.4);
        matrix.put(2, 1, 0.1);
        matrix.put(2, 2, 0.3);

        IDMatrix matrix1 = matrix.power(7);
        matrix1.display();
        System.out.println("--------------------");
        matrix1.sub(matrix).display();
        System.out.println("--------------------");
        matrix1.add(matrix).display();
    }
    */
}
