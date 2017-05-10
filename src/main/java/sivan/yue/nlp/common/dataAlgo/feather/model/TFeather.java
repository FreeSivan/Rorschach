package sivan.yue.nlp.common.dataAlgo.feather.model;

import sivan.yue.nlp.common.dataAlgo.feather.ITFSets;
import sivan.yue.nlp.common.dataAlgo.feather.ITFeather;
import sivan.yue.nlp.common.dataAlgo.feather.meta.TMetaFeather;
import sivan.yue.nlp.common.dataAlgo.matrix.ITMatrix;
import sivan.yue.nlp.common.exception.StructErrException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xiwen.yxw on 2017/3/27.
 */
public class TFeather implements ITFeather<TMetaFeather>, ITFSets {

    private ITMatrix matrix;

    private List<TMetaFeather> lst = new ArrayList<>();

    public TFeather() {}

    public TFeather(ITMatrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public double lambda(int x, int y, int z) {
        if (matrix == null) {
            throw new StructErrException("DFeather Error!");
        }
        return matrix.get(x, y, z);
    }

    @Override
    public void setLam(int x, int y, int z, double val) {
        if (matrix == null) {
            throw new StructErrException("DFeather Error!");
        }
        matrix.put(x, y, z, val, 0);
    }

    public void setTDef(double val) {
        if (matrix == null) {
            throw new StructErrException("DFeather Error!");
        }
        matrix.setDef(val);
    }

    @Override
    public void addFeather(int x, int y, int z) {
        lst.add(new TMetaFeather(x, y, z));
    }

    @Override
    public Iterator<TMetaFeather> iterator() {
        return lst.iterator();
    }

    public void setMatrix(ITMatrix matrix) {
        this.matrix = matrix;
    }
}
