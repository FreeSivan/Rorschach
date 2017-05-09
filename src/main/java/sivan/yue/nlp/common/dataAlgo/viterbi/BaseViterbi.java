package sivan.yue.nlp.common.dataAlgo.viterbi;

import sivan.yue.nlp.common.exception.ViterbiErrException;

import java.util.Vector;

/**
 * Created by xiwen.yxw on 2017/4/1.
 */
public abstract class BaseViterbi implements IViterbi{
    private final int INIT_STATE = 0;
    private final int FIRST_STATE = 1;
    private final int INIT_RATE = 1;

    /**
     * description ：观察序列-->状态序列
     * @param views 观察序列
     * @param stateNum 隐形状态的数目
     * @return 观察序列对应的状态序列
     */
    public int[] viterbi(int[] views, int stateNum) {
        int [] result = new int[views.length];
        Vector<Vector<ViterbiMeta>> vecArr = new Vector<>();
        // init 第一个观察值对应的观察序列的个状态的概率
        Vector<ViterbiMeta> initVec = new Vector<>();
        for (int state = FIRST_STATE; state < stateNum; ++state) {
            ViterbiMeta meta = new ViterbiMeta();
            // 计算观测值为views[0]的情况下，从INIT_STATE转到state的概率
            meta.curRate = countRate(INIT_RATE, views[0], state, INIT_STATE);
            meta.preState = INIT_STATE;
            initVec.add(meta);
        }
        vecArr.add(initVec);
        // 从第二个观测值到最后一个观测值，计算其对应的状态
        for (int i = 1; i < views.length; ++i) {
            Vector<ViterbiMeta> tmpVec = new Vector<>();
            Vector<ViterbiMeta> preVec = vecArr.get(i-1);
            // 对于观测值view[i]的所有可能的状态j
            for (int j = FIRST_STATE; j < stateNum; ++j) {
                ViterbiMeta meta = new ViterbiMeta();
                meta.curRate = 0;
                if (preVec == null) {
                    throw new ViterbiErrException("viterbi error!");
                }
                for (int k = FIRST_STATE; k < stateNum; ++k) {
                    // 找到前一个步骤的状态k对应的结构
                    ViterbiMeta m = preVec.get(k-1);
                    double rate = countRate(m.curRate, views[i], j, k);
                    if (rate > meta.curRate) {
                        meta.curRate = rate;
                        meta.preState = k;
                    }
                }
                tmpVec.add(meta);
            }
            vecArr.add(tmpVec);
        }
        // 从最后一个步骤回溯到第一个步骤，找到最佳状态序列
        Vector<ViterbiMeta> tmpVec = vecArr.get(views.length-1);
        double rate = 0;
        int state = 0;
        for (int i = FIRST_STATE; i < stateNum; ++i) {
            if (rate < tmpVec.get(i-1).curRate) {
                rate = tmpVec.get(i-1).curRate;
                state = i;
            }
        }
        result[views.length-1] = state;
        for (int i = views.length - 1; i > 0; --i) {
            ViterbiMeta meta = vecArr.get(i).get(result[i]-1);
            result[i-1] = meta.preState;
        }
        return result;
    }

    /**
     * 在前序节点概率确定，计算在观测值下的转移概率
     * @param preRate 前序节点的总概率
     * @param vState 观测序列当前位置的观测值
     * @param curState 序列当前位置的状态值
     * @param preState 序列前一个位置的状态值
     * @return 概率值
     */
    protected abstract double countRate(double preRate, int vState, int curState, int preState);

    private static class ViterbiMeta {
        public double curRate;
        public int preState;
    }
}
