package sivan.yue.nlp.common.dataAlgo.matrix;

/**
 * description : ��ά��������ӿ�
 *
 * Created by xiwen.yxw on 2017/3/22.
 */
public interface IDMatrix {
    /**
     * description : ��ȡ����row��col�е�ֵ
     * @param row ���������
     * @param col ���������
     * @return ����ָ��λ�õ�ֵ
     */
    public double get(int row, int col);

    /**
     * description : row��col�д���ֵval
     * @param row ָ���ľ������
     * @param col ָ���ľ������
     * @param val ��������ֵ
     */
    public void put(int row, int col, double val);

    /**
     * description : �������ݣ������Ƿ���
     * @param n �ݵĴ���
     * @return ����Ԫ�����n����
     */
    public IDMatrix power(int n);

    /**
     * description : �������
     * @param matrix ����B
     * @return ����������˵Ľ������
     */
    public IDMatrix mul(IDMatrix matrix);

    /**
     * description : �������
     * @param matrix ����B
     * @return ����������ӵĽ������
     */
    public IDMatrix add(IDMatrix matrix);

    /**
     * description : �������
     * @param matrix ����B
     * @return ��ǰ�����ȥ����B�Ľ������
     */
    public IDMatrix sub(IDMatrix matrix);

    /**
     * description : ��ȡ����
     * @return ���������
     */
    public int getColNum();

    /**
     * description : ��ȡ����
     * @return ���������
     */
    public int getRowNum();

    /**
     * description : ����һ�����Լ�һ���Ŀվ���
     * @return �´����Ŀվ���
     */
    public IDMatrix cloneSelf();

    /**
     * description : ��ӡ��������
     */
    public void display();
}