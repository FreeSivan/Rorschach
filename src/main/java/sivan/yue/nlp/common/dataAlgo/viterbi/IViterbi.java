package sivan.yue.nlp.common.dataAlgo.viterbi;

/**
 * Created by xiwen.yxw on 2017/4/1.
 */
public interface IViterbi {
    /**
     * description ：观察序列-->状态序列
     * @param views 观察序列
     * @param stateNum 隐形状态的数目
     * @return 观察序列对应的状态序列
     */
    public int[] viterbi(int[] views, int stateNum);
}
