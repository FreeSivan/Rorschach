package sivan.yue.nlp.common.dataAlgo.viterbi.module;

import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalDRate;
import sivan.yue.nlp.common.dataAlgo.viterbi.BaseViterbi;
import sivan.yue.nlp.common.exception.StructErrException;

/**
 * Created by xiwen.yxw on 2017/4/1.
 */
public class HmmViterbi extends BaseViterbi{

    private ConditionalDRate sRate;

    private ConditionalDRate tRate;

    public HmmViterbi() {}

    public HmmViterbi(ConditionalDRate sRate, ConditionalDRate tRate) {
        this.sRate = sRate;
        this.tRate = tRate;
    }


    @Override
    protected double countRate(double curRate, int x, int y1, int y0) {
        if (sRate == null || tRate == null) {
            throw new StructErrException("hmm viterbi error!");
        }
        return curRate * sRate.rate(y0, y1) * sRate.rate(y1, x);
    }

    public ConditionalDRate getsRate() {
        return sRate;
    }

    public void setsRate(ConditionalDRate sRate) {
        this.sRate = sRate;
    }

    public ConditionalDRate gettRate() {
        return tRate;
    }

    public void settRate(ConditionalDRate tRate) {
        this.tRate = tRate;
    }
}
