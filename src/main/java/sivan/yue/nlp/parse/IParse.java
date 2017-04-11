package sivan.yue.nlp.parse;

/**
 * Created by xiwen.yxw on 2017/3/23.
 */
public interface IParse {
    /**
     * description : 根据词序列解析词性标注序列
     * @param words 待标注的词序列
     * @return 标注完词性的词序列
     */
    public int[] parse(int[] words);

    /**
     *
     * @param path
     */
    public void load(String path);
}
