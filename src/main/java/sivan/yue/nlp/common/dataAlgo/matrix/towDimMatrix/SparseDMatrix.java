package sivan.yue.nlp.common.dataAlgo.matrix.towDimMatrix;

import sivan.yue.nlp.common.dataAlgo.matrix.BaseDMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * description : 稀松矩阵， 三元表示法， 不占用连续内存
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public class SparseDMatrix extends BaseDMatrix {

    private Map<Integer, Map<Integer, Double>> data;

    public SparseDMatrix(int m, int n) {
        super(m, n);
        data = new HashMap<>();
    }

    public double getV(int row, int col) {
        Map<Integer, Double> tmp = data.get(row);
        if (tmp == null) {
            return 0;
        }
        Double val = tmp.get(col);
        if (val == null) {
            return 0;
        }
        return val;
    }

    public void putV(int row, int col, double val) {
        Map<Integer, Double> tmp = data.get(row);
        if (tmp == null) {
            tmp = new HashMap<>();
            data.put(row, tmp);
        }
        tmp.put(col, val);
    }

    @Override
    public IDMatrix cloneSelf() {
        return new SparseDMatrix(getRowNum(), getColNum());
    }

    /*
    public static void main(String[] args) {
        SparseDMatrix matrix = new SparseDMatrix(3, 3);
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
