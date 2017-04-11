package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description ����ά�������Ͻӿ�
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface IDFSets {
    /**
     * description ������x��y������ϵ��
     * @param x ������xά
     * @param y ������yά
     * @return x��y������������ϵ��
     */
    public double lambda(int x, int y);

    /**
     * description ������x��y������ϵ��
     * @param x ������xά
     * @param y ������yά
     * @param val x, y������ϵ��
     */
    public void setLam(int x, int y, double val);
}
