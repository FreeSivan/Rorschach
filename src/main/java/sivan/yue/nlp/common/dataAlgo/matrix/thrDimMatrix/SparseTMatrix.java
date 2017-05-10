package sivan.yue.nlp.common.dataAlgo.matrix.thrDimMatrix;

import javafx.beans.binding.MapExpression;
import sivan.yue.nlp.common.dataAlgo.matrix.BaseTMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.ITMatrix;
import sivan.yue.nlp.common.tools.FileLineWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiwen.yxw on 2017/3/24.
 */
public class SparseTMatrix extends BaseTMatrix {

    private Map<Integer, Map<Integer, Map<Integer, Double>>> data;

    private Map<Integer, Map<Integer, Double>> defData;

    private double def = 0;

    public SparseTMatrix(int xNum, int yNum, int zNum, double def) {
        super(xNum, yNum, zNum);
        data = new HashMap<>();
        defData = new HashMap<>();
        this.def = def;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    @Override
    protected double getV(int x, int y, int z) {
        if (data.get(x) != null &&
            data.get(x).get(y) != null &&
            data.get(x).get(y).get(z) !=null) {
            return data.get(x).get(y).get(z);
        }
        if (defData.get(x) != null &&
            defData.get(x).get(y) != null) {
            return defData.get(x).get(y);
        }
        return def;
    }

    /**
     * x, y作为输入，z作为输出
     * 每设置一次x，y，z；就要设置一次关于输入xy的默认概率
     * @param x
     * @param y
     * @param z
     * @param v
     * @param d
     */
    @Override
    protected void putV(int x, int y, int z, double v, double d) {
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
        Map<Integer, Double> defTmp = defData.get(x);
        if (defData.get(x) == null) {
            defTmp = new HashMap<>();
            defData.put(x, defTmp);
        }
        defTmp.put(y, d);
    }

    @Override
    public ITMatrix cloneSelf() {
        return null;
    }

    @Override
    public void export(String fileName) {
        FileLineWriter fWriter = new FileLineWriter(fileName);
        fWriter.writeLine(""+def);
        for (Map.Entry<Integer, Map<Integer, Double>> item : defData.entrySet()) {
            for (Map.Entry<Integer, Double> item1 : item.getValue().entrySet()) {
                String line = item.getKey() + "|" + item1.getKey() + "|" + item1.getValue();
                fWriter.writeLine(line);
            }
        }
        for (Map.Entry<Integer, Map<Integer, Map<Integer, Double>>> item : data.entrySet()) {
            for (Map.Entry<Integer, Map<Integer, Double>> item1 : item.getValue().entrySet()) {
                for (Map.Entry<Integer, Double> item2 : item1.getValue().entrySet()) {
                    String line = item.getKey() + "|" + item1.getKey() + "|" + item2.getKey() + "|" + item2.getValue();
                    fWriter.writeLine(line);
                }
            }
        }
        fWriter.close();
    }
}
