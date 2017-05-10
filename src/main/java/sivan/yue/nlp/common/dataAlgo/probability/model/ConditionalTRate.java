package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.ITMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.ITRate;

/**
 * description : ��ά��������ģ�ͷ�װ��
 *
 * ���ڴ洢������������ս���ĸ���ģ��
 *
 * rate(x, y, z) ��ʶP(z|xy)�ĸ���
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public class ConditionalTRate implements ITRate{

    /**
     * ��ά���ʾ���
     */
    private ITMatrix rate;

    public ConditionalTRate() {}

    public ConditionalTRate(ITMatrix rate) {
        this.rate = rate;
    }

    /**
     * description : ��ѯxy���ڵ������z�����ĸ���
     * @param x ����x
     * @param y ����y
     * @param z ����z
     * @return xy���ڵ������z��������
     */
    public double rate(int x, int y, int z) {
        return rate.get(x, y, z);
    }

    @Override
    public void setRate(int x, int y, int z, double val, double def) {
        rate.put(x, y, z, val, def);
    }

    public void setRate(ITMatrix rate) {
        this.rate = rate;
    }

    public ITMatrix getRate() {
        return this.rate;
    }

    public void setTDef(double val) {

    }
}
