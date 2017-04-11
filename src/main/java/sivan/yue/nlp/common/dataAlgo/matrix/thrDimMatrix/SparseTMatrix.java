package sivan.yue.nlp.common.dataAlgo.matrix.thrDimMatrix;

import sivan.yue.nlp.common.dataAlgo.matrix.BaseTMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.ITMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiwen.yxw on 2017/3/24.
 */
public class SparseTMatrix extends BaseTMatrix {

    private Map<Integer, Map<Integer, Map<Integer, Double>>> data;

    public SparseTMatrix(int xNum, int yNum, int zNum) {
        super(xNum, yNum, zNum);
        data = new HashMap<>();
    }

    @Override
    protected double getV(int x, int y, int z) {
        Map<Integer, Map<Integer, Double>> tmp = data.get(x);
        if (tmp == null) {
            return 0;
        }
        Map<Integer, Double> tmp1 = tmp.get(y);
        if (tmp1 == null) {
            return 0;
        }
        Double tmp2 = tmp1.get(z);
        if (tmp2 == null) {
            return 0;
        }
        return tmp2;
    }

    @Override
    protected void putV(int x, int y, int z, double v) {
        Map<Integer, Map<Integer, Double>> tmp = data.get(x);
        if (data.get(x) == null) {
            tmp = new HashMap<>();
            data.put(x, tmp);
        }
        Map<Integer, Double> tmp1 = tmp.get(y);
        if (tmp.get(y) == null) {
            tmp1 = new HashMap<>();
            tmp.put(y, tmp1);
        }
        tmp1.put(z, v);
    }

    @Override
    public ITMatrix cloneSelf() {
        return null;
    }
}
