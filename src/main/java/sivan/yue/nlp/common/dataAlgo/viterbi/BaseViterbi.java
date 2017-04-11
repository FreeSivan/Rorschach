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
     * description ���۲�����-->״̬����
     * @param views �۲�����
     * @param stateNum ����״̬����Ŀ
     * @return �۲����ж�Ӧ��״̬����
     */
    public int[] viterbi(int[] views, int stateNum) {
        int [] result = new int[views.length];
        Vector<Vector<ViterbiMeta>> vecArr = new Vector<>();
        // init ��һ���۲�ֵ��Ӧ�Ĺ۲����еĸ�״̬�ĸ���
        Vector<ViterbiMeta> initVec = new Vector<>();
        for (int state = FIRST_STATE; state < stateNum; ++state) {
            ViterbiMeta meta = new ViterbiMeta();
            // ����۲�ֵΪviews[0]������£���INIT_STATEת��state�ĸ���
            meta.curRate = countRate(INIT_RATE, views[0], state, INIT_STATE);
            meta.preState = INIT_STATE;
            initVec.add(meta);
        }
        vecArr.add(initVec);
        // �ӵڶ����۲�ֵ�����һ���۲�ֵ���������Ӧ��״̬
        for (int i = 1; i < views.length; ++i) {
            Vector<ViterbiMeta> tmpVec = new Vector<>();
            Vector<ViterbiMeta> preVec = vecArr.get(i-1);
            // ���ڹ۲�ֵview[i]�����п��ܵ�״̬j
            for (int j = FIRST_STATE; j < stateNum; ++j) {
                ViterbiMeta meta = new ViterbiMeta();
                meta.curRate = 0;
                if (preVec == null) {
                    throw new ViterbiErrException("viterbi error!");
                }
                for (int k = FIRST_STATE; k < stateNum; ++k) {
                    // �ҵ�ǰһ�������״̬k��Ӧ�Ľṹ
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
        // �����һ��������ݵ���һ�����裬�ҵ����״̬����
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
     * ��ǰ��ڵ����ȷ���������ڹ۲�ֵ�µ�ת�Ƹ���
     * @param preRate ǰ��ڵ���ܸ���
     * @param vState �۲����е�ǰλ�õĹ۲�ֵ
     * @param curState ���е�ǰλ�õ�״ֵ̬
     * @param preState ����ǰһ��λ�õ�״ֵ̬
     * @return ����ֵ
     */
    protected abstract double countRate(double preRate, int vState, int curState, int preState);

    private static class ViterbiMeta {
        public double curRate;
        public int preState;
    }
}
