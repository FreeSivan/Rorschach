package sivan.yue.nlp.common.dataAlgo.probability;

/**
 * description : ˫������ѯ���ʽӿ�
 *
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface IDRate {
    /**
     * description : ��ȡ����x��y��ͬ�����ĸ���
     * @param x ����x
     * @param y ����y
     * @return ����x��y��ͬ�����ĸ���
     */
    public double rate(int x, int y);

    public void setRate(int x, int y, double val, double def);

    public void setRate(int x, int y, double val);
}
