package sivan.yue.nlp.common.dataAlgo.probability;

/**
 * description : ��������ѯ���ʽӿ�
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface ISRate {
    /**
     * description : ��ȡ����x�ĸ���
     * @param x ����x
     * @return ����x�ĸ���
     */
    public double rate(int x);

    public void setRate(int x, double val);
}
