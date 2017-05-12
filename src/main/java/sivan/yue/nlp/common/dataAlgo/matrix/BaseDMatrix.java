package sivan.yue.nlp.common.dataAlgo.matrix;

import sivan.yue.nlp.common.exception.AccessErrException;
import sivan.yue.nlp.common.exception.MatrixErrException;
import sivan.yue.nlp.common.exception.ParamErrException;

/**
 * description : 二维矩阵基类
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public abstract class BaseDMatrix implements IDMatrix {

    protected int colNum;

    protected int rowNum;

    public BaseDMatrix(int m, int n) {
        this.rowNum = m;
        this.colNum = n;
    }

    public double get(int row, int col) {
        if (row >= rowNum || col >= colNum) {
            throw new AccessErrException("get error");
        }
        return getV(row, col);
    }

    /**
     *
     * @param row 指定的矩阵的行
     * @param col 指定的矩阵的列
     * @param val 存入矩阵的值
     * @param d 默认值，用于稀疏矩阵
     */
    public void put(int row, int col, double val, double d) {
        if (row >= rowNum || col >= colNum) {
            throw new AccessErrException("put error");
        }
        putV(row, col, val, d);
    }

    public void put(int row, int col, double val) {
        if (row >= rowNum || col >= colNum) {
            throw new AccessErrException("put error");
        }
        putV(row, col, val, 0);
    }

    public IDMatrix mul(IDMatrix matrix) {
        if (colNum != matrix.getRowNum()) {
            throw new MatrixErrException("mul error");
        }
        IDMatrix result = cloneSelf();
        for (int i = 0; i < rowNum; ++i) {
            for (int j = 0; j < colNum; ++j) {
                double sum = 0;
                for (int k = 0; k < colNum; ++k) {
                    sum+=get(i, k) * matrix.get(k, j);
                }
                result.put(i, j, sum, 0);
            }
        }
        return result;
    }

    public IDMatrix add(IDMatrix matrix) {
        if (rowNum != matrix.getRowNum() ||
                colNum != matrix.getColNum()) {
            throw new MatrixErrException("add error");
        }
        IDMatrix result = cloneSelf();
        for (int i = 0; i < rowNum; ++i) {
            for (int j = 0; j < colNum; ++j) {
                double tmp = get(i, j) + matrix.get(i, j);
                result.put(i, j, tmp, 0);
            }
        }
        return result;
    }

    public IDMatrix sub(IDMatrix matrix) {
        if (rowNum != matrix.getRowNum() ||
                colNum != matrix.getColNum()) {
            throw new MatrixErrException("sub error");
        }
        IDMatrix result = cloneSelf();
        for (int i = 0; i < rowNum; ++i) {
            for (int j = 0; j < colNum; ++j) {
                double tmp = get(i, j) - matrix.get(i, j);
                result.put(i, j, tmp, 0);
            }
        }
        return result;
    }

    @Override
    public int getColNum() {
        return this.colNum;
    }

    @Override
    public int getRowNum() {
        return this.rowNum;
    }

    @Override
    public IDMatrix power(int n) {
        if (n == 0) {
            throw new ParamErrException("power error");
        }
        if (n == 1) {
            return this;
        }
        IDMatrix result = this;
        for (int i = 1; i < n; ++i) {
            result = mul(result);
        }
        return result;
    }

    public void display() {
        for (int i = 0; i < rowNum; ++i) {
            for (int j = 0; j < colNum; ++j) {
                System.out.print(" " + get(i, j));
            }
            System.out.println("");
        }
    }

    protected abstract double getV(int row, int col);

    protected abstract void putV(int row, int col, double val, double d);
}
