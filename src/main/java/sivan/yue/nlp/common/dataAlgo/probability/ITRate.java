package sivan.yue.nlp.common.dataAlgo.probability;

/**
 * description : ��������ѯ���ʽӿ�
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface ITRate {
    /**
     * description :��ȡ����x y z��ͬ�����ĸ���
     * @param x ����x
     * @param y ����y
     * @param z ����z
     * @return ����x y z��ͬ�����ĸ���
     */
    public double rate(int x, int y, int z);

    public void setRate(int x, int y, int z, double val, double def);

    public void setRate(int x, int y, int z, double val);
}
