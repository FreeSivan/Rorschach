package sivan.yue.nlp.train;

/**
 * Created by xiwen.yxw on 2017/3/23.
 */
public interface ITrain {

    /**
     * description : ѵ�����Խ���ģ��
     * @param org �������ݵ�ַ
     * @param dst ��������ַ
     * @param vNum ����Ŀ
     * @param sNum ������Ŀ
     */
    public void train(String org, String dst, int sNum, int vNum);

    /**
     * description : ����ģ�����ݵ��ļ�
     * @param dst �����ļ���·��
     */
    public void export(String dst);
}
