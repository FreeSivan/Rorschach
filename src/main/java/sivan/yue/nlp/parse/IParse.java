package sivan.yue.nlp.parse;

/**
 * Created by xiwen.yxw on 2017/3/23.
 */
public interface IParse {
    /**
     * description : ���ݴ����н������Ա�ע����
     * @param words ����ע�Ĵ�����
     * @return ��ע����ԵĴ�����
     */
    public int[] parse(int[] words);

    /**
     *
     * @param path
     */
    public void load(String path);
}
