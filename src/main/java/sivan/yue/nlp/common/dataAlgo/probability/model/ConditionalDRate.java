package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.IDRate;

/**
 * description : ��ά��������ģ�ͷ�װ��
 *
 * ���ڴ洢�����ģ�����ս���ĸ���ģ��
 *
 * rate(x, y) ��ʶP(y|x)�ĸ���
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public class ConditionalDRate implements IDRate{
    /**
     * ��ά���ʾ���
     */
    private IDMatrix rate;

    public ConditionalDRate() {}

    public ConditionalDRate(IDMatrix rate) {
        this.rate = rate;
    }

    /**
     * description : ��ѯx�����������y�����ĸ���
     * @param x ����x
     * @param y ����y
     * @return x�����������y����������
     */
    @Override
    public double rate(int x, int y) {
        return rate.get(x, y);
    }

    @Override
    public void setRate(int x, int y, double val) {
        rate.put(x, y, val);
    }

    public void setRate(IDMatrix rate) {
        this.rate = rate;
    }

    public IDMatrix getRate() {
        return this.rate;
    }
}
