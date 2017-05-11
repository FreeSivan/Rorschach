package sivan.yue.nlp.common.dataAlgo.matrix.thrDimMatrix;

import sivan.yue.nlp.common.dataAlgo.matrix.BaseTMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.ITMatrix;

/**
 * Created by xiwen.yxw on 2017/3/24.
 */
public class DenseTMatrix extends BaseTMatrix{

    private double[][][] data;

    public DenseTMatrix(int xNum, int yNum, int zNum) {
        super(xNum, yNum, zNum);
        data = new double[xNum][yNum][zNum];
        for (int i = 0; i < xNum; ++i) {
            for (int j = 0; j < yNum; ++j) {
                for (int k = 0; k < zNum; ++k) {
                    data[i][j][k] = 0;
                }
            }
        }
    }

    @Override
    protected double getV(int x, int y, int z) {
        return data[x][y][z];
    }

    @Override
    protected void putV(int x, int y, int z, double v, double d) {
        data[x][y][z] = v;
    }

    @Override
    public double getDef() {
        return 0;
    }

    @Override
    public void setDef(double val) {

    }

    @Override
    public ITMatrix cloneSelf() {
        return new DenseTMatrix(xNum, yNum, zNum);
    }

    @Override
    public void export(String fileName) {

    }

    @Override
    public void load(String fileName) {

    }
}
