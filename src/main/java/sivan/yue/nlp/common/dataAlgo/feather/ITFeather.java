package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description : ��ά���������ӿ�
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface ITFeather<T> extends Iterable<T> {
    /**
     * description �� ���һ����ά������������
     * @param x ����xά��
     * @param y ����yά��
     * @param z ����zά��
     */
    public void addFeather(int x, int y, int z);
}
