package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description : 三维特征容器接口
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface ITFeather<T> extends Iterable<T> {
    /**
     * description ： 添加一个三维特征到容器中
     * @param x 特征x维度
     * @param y 特征y维度
     * @param z 特征z维度
     */
    public void addFeather(int x, int y, int z);
}
