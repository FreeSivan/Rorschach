package sivan.yue.nlp.common.dataAlgo.feather;

/**
 * description : 二维feather 容器接口
 * 支持添加特征和迭代特征的功能
 *
 * Created by xiwen.yxw on 2017/3/27.
 */
public interface IDFeather<T> extends Iterable<T>{
    /**
     * description : 二维特征添加接口
     * @param x 特征x参数
     * @param y 特征y参数
     */
    public void addFeather(int x, int y);
}
