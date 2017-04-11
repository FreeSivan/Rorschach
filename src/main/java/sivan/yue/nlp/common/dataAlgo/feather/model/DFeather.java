package sivan.yue.nlp.common.dataAlgo.feather.model;

import sivan.yue.nlp.common.dataAlgo.feather.IDFSets;
import sivan.yue.nlp.common.dataAlgo.feather.IDFeather;
import sivan.yue.nlp.common.dataAlgo.feather.meta.DMetaFeather;
import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.exception.StructErrException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xiwen.yxw on 2017/3/27.
 */
public class DFeather implements IDFeather<DMetaFeather>, IDFSets {

    private IDMatrix matrix;

    private List<DMetaFeather> lst = new ArrayList<>();

    public DFeather() {}

    public DFeather(IDMatrix matrix) {
        this.matrix = matrix;
    }
    @Override
    public double lambda(int x, int y) {
        if (matrix == null) {
            throw new StructErrException("DFeather Error!");
        }
        return matrix.get(x, y);
    }

    @Override
    public void setLam(int x, int y, double val) {
        if (matrix == null) {
            throw new StructErrException("DFeather Error!");
        }
        matrix.put(x, y, val);
    }

    @Override
    public void addFeather(int x, int y) {
        lst.add(new DMetaFeather(x, y));
    }

    @Override
    public Iterator iterator() {
        return lst.iterator();
    }

    public void setMatrix(IDMatrix matrix) {
        this.matrix = matrix;
    }
}
