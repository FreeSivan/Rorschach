package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.dataAlgo.matrix.ITMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.IDRate;
import sivan.yue.nlp.common.dataAlgo.probability.ISRate;
import sivan.yue.nlp.common.dataAlgo.probability.ITRate;

/**
 * description : ����������̸���ģ�ͷ�װ��
 *
 * ���HMM��ME��MEMM��CRFģ�͵��������ģ��
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public class JointMarginalRate implements ISRate, IDRate, ITRate{
    /**
     * ���ϸ��ʾ���
     */
    private IDMatrix dRate;

    /**
     * ���ϸ��ʾ���
     */
    private ITMatrix tRate;

    /**
     * ��Ե���ʾ���
     */
    private IDMatrix sRate;

    public JointMarginalRate() {};
    /**
     * description : ���캯��
     * @param dRate ���ϸ��ʾ���
     * @param sRate ��Ե���ʾ���
     * @param tRate ���ϸ��ʾ���
     */
    public JointMarginalRate(IDMatrix dRate, IDMatrix sRate, ITMatrix tRate) {
        this.dRate = dRate;
        this.sRate = sRate;
        this.tRate = tRate;
    }

    @Override
    public double rate(int x, int y) {
        return dRate.get(x, y);
    }

    @Override
    public void setRate(int x, int y, double val, double def) {
        dRate.put(x, y, val, def);
    }

    @Override
    public void setRate(int x, int y, double val) {
        dRate.put(x, y, val, 0);
    }

    @Override
    public double rate(int x) {
        return sRate.get(x, 0);
    }

    @Override
    public void setRate(int x, double val, double d) {
        sRate.put(x, 0, val, d);
    }

    @Override
    public void setRate(int x, double val) {
        sRate.put(x, 0, val, 0);
    }

    public void setDRate(IDMatrix dRate) {
        this.dRate = dRate;
    }

    public void setSRate(IDMatrix sRate) {
        this.sRate = sRate;
    }

    public void setTRate(ITMatrix tRate) {
        this.tRate = tRate;
    }

    @Override
    public double rate(int x, int y, int z) {
        return tRate.get(x, y, z);
    }

    @Override
    public void setRate(int x, int y, int z, double val, double def) {
        tRate.put(x, y, z, val, def);
    }

    @Override
    public void setRate(int x, int y, int z, double val) {
        tRate.put(x, y, z, val, 0);
    }

    public void setTDef(double val) {
        tRate.setDef(val);
    }

    public void setDDef(double val) {
        dRate.setDef(val);
    }

    public void setSDef(double val) {
        sRate.setDef(val);
    }
}
