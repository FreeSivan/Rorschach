package sivan.yue.nlp.common.dataAlgo.matrix;

/**
 * Created by xiwen.yxw on 2017/3/24.
 */
public interface ITMatrix {
    /**
     * description : ��ȡ����ָ��λ�õ�ֵ
     * @param x �����x��
     * @param y �����y��
     * @param z �����z��
     * @return ����ָ��λ�õ�ֵ
     */
    public double get(int x, int y, int z);

    /**
     * description : ָ��λ�ô���ֵval
     * @param x ָ���ľ����xֵ
     * @param y ָ���ľ����yֵ
     * @param z ָ���ľ����zֵ
     * @param v ��������ֵ
     * @param v ��������Ĭ��ֵ
     */
    public void put(int x, int y, int z, double v, double d);

    /**
     * description : ��ȡx��
     * @return ���������
     */
    public int getXNum();

    /**
     * description : ��ȡy��
     * @return ���������
     */
    public int getYNum();

    /**
     * description : ��ȡz��
     * @return
     */
    public int getZNum();

    public double getDef();

    public void setDef(double val);

    /**
     * description : ����һ�����Լ�һ���Ŀվ���
     * @return �´����Ŀվ���
     */
    public ITMatrix cloneSelf();

    /**
     * description : �������ݵ�ָ���ļ�
     * @param fileName �ļ���
     */
    public void export(String fileName);

    /**
     * description : ���ļ��е�������
     * @param fileName �ļ���
     */
    public void load(String fileName);

}
