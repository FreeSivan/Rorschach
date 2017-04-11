package sivan.yue.nlp.common.dataAlgo.viterbi.module;

import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalDRate;
import sivan.yue.nlp.common.dataAlgo.viterbi.BaseViterbi;
import sivan.yue.nlp.common.exception.StructErrException;

/**
 * Created by xiwen.yxw on 2017/4/1.
 */
public class MeViterbi extends BaseViterbi{

    private ConditionalDRate rate;

    public MeViterbi() {}

    public MeViterbi(ConditionalDRate rate) {
        this.rate = rate;
    }

    @Override
    protected double countRate(double curRate, int x, int y1, int y0) {
        if (rate == null) {
            throw new StructErrException("Me viterbi error!");
        }
        return curRate * rate.rate(y1, x);
    }

    public ConditionalDRate getRate() {
        return rate;
    }

    public void setRate(ConditionalDRate rate) {
        this.rate = rate;
    }
}
