package sivan.yue.nlp.common.dataAlgo.viterbi;

/**
 * Created by xiwen.yxw on 2017/4/1.
 */
public interface IViterbi {
    /**
     * description ���۲�����-->״̬����
     * @param views �۲�����
     * @param stateNum ����״̬����Ŀ
     * @return �۲����ж�Ӧ��״̬����
     */
    public int[] viterbi(int[] views, int stateNum);
}
