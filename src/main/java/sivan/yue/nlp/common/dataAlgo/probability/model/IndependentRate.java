package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.IDRate;

/**
 * description : ����������̸���ģ�ͷ�װ��
 *
 * ���HMMģ�͵�������ʾ���
 *
 * rate(t, x) ����ȡ�������X(t)=x�ĸ���P{X(t)=x}
 *
 * Created by xiwen.yxw on 2017/3/23.
 */
public class IndependentRate implements IDRate{
    /**
     * ���ʾ���
     */
    private IDMatrix rate;

    /**
     * ָ�꼯�ϵĴ�С
     */
    private int num;

    public IndependentRate() {}

    public IndependentRate(IDMatrix rate) {
        this.rate = rate;
        this.num = rate.getRowNum();
    }

    /**
     * description : ָ��Ϊtʱ״̬Ϊx�ĸ���
     * @param t �������X(t)�Ĳ���t
     * @param x �������X(t)��״̬x
     * @return X(t)ȡxֵ�ĸ���
     */
    public double rate(int t, int x) {
        return rate.get(t, x);
    }

    @Override
    public void setRate(int x, int y, double val) {
        rate.put(x, y, val);
    }

    public void setRate(IDMatrix rate) {
        this.rate = rate;
        setNum(rate.getRowNum());
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
