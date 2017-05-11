package sivan.yue.nlp.common.dataAlgo.matrix.towDimMatrix;

import sivan.yue.nlp.common.dataAlgo.matrix.BaseDMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.tools.FileIteratorUtil;
import sivan.yue.nlp.common.tools.FileLineWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * description : 稀松矩阵， 三元表示法， 不占用连续内存
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public class SparseDMatrix extends BaseDMatrix {

    private Map<Integer, Map<Integer, Double>> data;

    private Map<Integer, Double> defData;

    private double def = 0;

    public SparseDMatrix(int m, int n) {
        super(m, n);
        data = new HashMap<>();
        defData = new HashMap<>();
    }

    public double getV(int row, int col) {
        if (data.get(row) != null && data.get(row).get(col) != null) {
            return data.get(row).get(col);
        }
        if (defData.get(row) != null) {
            return defData.get(row);
        }
        return def;
    }

    public void putV(int row, int col, double val, double d) {
        Map<Integer, Double> tmp = data.get(row);
        if (tmp == null) {
            tmp = new HashMap<>();
            data.put(row, tmp);
        }
        tmp.put(col, val);
        defData.put(row, d);
    }

    public double getDef() {
        return def;
    }

    @Override
    public void setDef(double def) {
        this.def = def;
    }

    @Override
    public IDMatrix cloneSelf() {
        SparseDMatrix matrix = new SparseDMatrix(getRowNum(), getColNum());
        matrix.setDef(getDef());
        return matrix;
    }

    @Override
    public void export(String fileName) {
        FileLineWriter fWriter = new FileLineWriter(fileName);
        fWriter.writeLine(""+def);
        for (Map.Entry<Integer, Double> item : defData.entrySet()) {
            String line = item.getKey() + "|" + item.getValue();
            fWriter.writeLine(line);
        }
        for (Map.Entry<Integer, Map<Integer, Double>> item : data.entrySet()) {
            for (Map.Entry<Integer, Double> item1 : item.getValue().entrySet()) {
                String line = item.getKey() + "|" + item1.getKey() + "|" + item1.getValue();
                fWriter.writeLine(line);
            }
        }
        fWriter.close();
    }

    @Override
    public void load(String fileName) {
        try {
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length == 1) {
                    def = Double.parseDouble(strArr[0]);
                }
                else if (strArr.length == 2) {
                    int key = Integer.parseInt(strArr[0]);
                    double value = Double.parseDouble(strArr[1]);
                    defData.put(key, value);
                }
                else if (strArr.length == 3) {
                    int key1 = Integer.parseInt(strArr[0]);
                    int key2 = Integer.parseInt(strArr[1]);
                    double value = Double.parseDouble(strArr[2]);
                    if (data.get(key1) == null) {
                        data.put(key1, new HashMap<Integer, Double>());
                    }
                    data.get(key1).put(key2, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
