package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description : ��άfeather �����ӿ�
 * ֧����������͵��������Ĺ���
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface IDFeather<T> extends Iterable<T>{
    /**
     * description : ��ά������ӽӿ�
     * @param x ����x����
     * @param y ����y����
     */
    public void addFeather(int x, int y);
}
