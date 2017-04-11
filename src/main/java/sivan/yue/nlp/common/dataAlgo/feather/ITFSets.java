package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description ����ά�������Ͻӿ�
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface ITFSets {
    /**
     * description ������x��y, z������ϵ��
     * @param x ������xά
     * @param y ������yά
     * @param z ������zά
     * @return x, y, z������������Ӧ��ϵ��
     */
    public double lambda(int x, int y, int z);

    /**
     * description ������x��y, z������ϵ��
     * @param x ������xά
     * @param y ������yά
     * @param z ������zά
     * @param val ����x, y, z������������ϵ��
     */
    public void setLam(int x, int y, int z, double val);
}
