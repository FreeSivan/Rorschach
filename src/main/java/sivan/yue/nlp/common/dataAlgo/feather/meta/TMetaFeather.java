package sivan.yue.nlp.common.dataAlgo.feather.meta;

/**
 * description : 三维特征封装类
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public class TMetaFeather {

    private int x;

    private int y;

    private int z;

    public TMetaFeather(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
