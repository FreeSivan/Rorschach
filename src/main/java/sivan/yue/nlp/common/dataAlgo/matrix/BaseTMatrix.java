package sivan.yue.nlp.common.dataAlgo.matrix;

/**
 * Created by xiwen.yxw on 2017/3/24.
 */
public abstract class BaseTMatrix implements ITMatrix{

    protected int xNum;

    protected int yNum;

    protected int zNum;

    public BaseTMatrix(int xNum, int yNum, int zNum) {
        this.xNum = xNum;
        this.yNum = yNum;
        this.zNum = zNum;
    }

    @Override
    public double get(int x, int y, int z) {
        return getV(x, y, z);
    }

    @Override
    public void put(int x, int y, int z, double v, double d) {
        putV(x, y, z, v, d);
    }

    public void put(int x, int y, int z, double v) {
        putV(x, y, z, v, 0);
    }

    @Override
    public int getXNum() {
        return this.xNum;
    }

    @Override
    public int getYNum() {
        return this.yNum;
    }

    @Override
    public int getZNum() {
        return this.zNum;
    }

    protected abstract double getV(int x, int y, int z);

    protected abstract void putV(int x, int y, int z, double v, double d);
}
