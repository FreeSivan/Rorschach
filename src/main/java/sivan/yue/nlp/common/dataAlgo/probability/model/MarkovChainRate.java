package sivan.yue.nlp.common.dataAlgo.probability.model;

import sivan.yue.nlp.common.dataAlgo.matrix.IDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.IDRate;
import sivan.yue.nlp.common.dataAlgo.probability.ISRate;
import sivan.yue.nlp.common.exception.MatrixErrException;
import sivan.yue.nlp.common.exception.ParamErrException;

import java.util.HashMap;
import java.util.Map;

/**
 * description : ����ɷ����ģ�ͷ�װ��
 *
 * rate(a) : P(a|init) ��ȡ״̬a�ĳ�ʼ����
 *
 * rate(a,b) : P(b|a) ��ȡ״̬a��b��һ��ת�Ƹ���
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public class MarkovChainRate implements ISRate, IDRate{
    /**
     * ״̬ת�ƾ���
     */
    private IDMatrix trans;

    /**
     * ��ʼ״̬����
     */
    private IDMatrix init;

    /**
     * ״̬��Ŀ
     */
    private int stateNum;

    /**
     * ����N��ת�ƾ����map
     */
    private Map<Integer, IDMatrix> transMap;

    public MarkovChainRate() {};
    /**
     * description : ���캯��
     * @param init ��ʼ���ʾ���
     * @param trans ת�Ƹ��ʾ���
     */
    public MarkovChainRate(IDMatrix init, IDMatrix trans) {
        if (init.getRowNum() != trans.getRowNum()) {
            throw new MatrixErrException("markov error");
        }
        this.init = init;
        this.trans = trans;
        this.stateNum = trans.getRowNum();
        transMap = new HashMap<>();
    }

    /**
     * description : ��ȡ״̬a�ĳ�ʼ����
     * @param a ״̬a
     * @return ״̬a�ĳ�ʼ����
     */
    public double rate (int a) {
        return init.get(a, 0);
    }

    @Override
    public void setRate(int x, double val) {
        init.put(x, 0, val, 0);
    }

    /**
     * description : ��״̬a��һ��ת�Ƶ�״̬b�ĸ���
     * @param a ״̬a
     * @param b ״̬b
     * @return ״̬a��һ��ת�Ƶ�״̬b�ĸ���
     */
    public double rate(int a, int b) {
        return rate(a, b, 1);
    }

    @Override
    public void setRate(int x, int y, double val, double d) {
        trans.put(x, y, val, d);
    }

    /**
     * description : ��״̬a��n��ת�Ƶ�״̬b�ĸ���
     * @param a ״̬a
     * @param b ״̬b
     * @param n ת�ƵĲ���
     * @return ״̬a��n��ת�Ƶ�״̬b�ĸ���
     */
    public double rate(int a, int b, int n) {
        if (n < 1) {
            throw new ParamErrException("n == 0");
        }
        if (transMap.get(n) != null) {
            return transMap.get(n).get(a, b);
        }
        IDMatrix tmp = trans.power(n);
        transMap.put(n, tmp);
        return tmp.get(a, b);
    }

    /**
     *
     * @param trans
     */
    public void setTrans(IDMatrix trans) {
        this.trans = trans;
        this.stateNum = trans.getRowNum();
    }

    /**
     *
     * @param init
     */
    public void setInit(IDMatrix init) {
        this.init = init;
        this.stateNum = init.getRowNum();
    }

    public int getStateNum() {
        return stateNum;
    }
}
