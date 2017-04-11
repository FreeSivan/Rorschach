package sivan.yue.nlp.train;

/**
 * Created by xiwen.yxw on 2017/3/23.
 */
public interface ITrain {

    /**
     * description : 训练词性解析模型
     * @param org 样本数据地址
     * @param dst 结果输出地址
     * @param aNum 词数目
     * @param bNum 词性数目
     */
    public void train(String org, String dst, int aNum, int bNum);

    /**
     * description : 导出模型数据到文件
     * @param dst 导出文件的路径
     */
    public void export(String dst);
}
