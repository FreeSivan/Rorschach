package sivan.yue.nlp.train;

/**
 * Created by xiwen.yxw on 2017/3/23.
 */
public interface ITrain {

    /**
     * description : ѵ�����Խ���ģ��
     * @param org �������ݵ�ַ
     * @param dst ��������ַ
     * @param aNum ����Ŀ
     * @param bNum ������Ŀ
     */
    public void train(String org, String dst, int aNum, int bNum);

    /**
     * description : ����ģ�����ݵ��ļ�
     * @param dst �����ļ���·��
     */
    public void export(String dst);
}
