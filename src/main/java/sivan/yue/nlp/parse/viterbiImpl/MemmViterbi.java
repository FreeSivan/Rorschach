package sivan.yue.nlp.parse.viterbiImpl;

import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalTRate;
import sivan.yue.nlp.parse.BaseViterbi;
import sivan.yue.nlp.common.exception.StructErrException;

/**
 * Created by xiwen.yxw on 2017/4/1.
 */
public class MemmViterbi extends BaseViterbi{

    private ConditionalTRate rate;

    public MemmViterbi() {}

    public MemmViterbi(ConditionalTRate rate) {
        this.rate = rate;
    }

    @Override
    protected double countRate(double curRate, int x, int y1, int y0) {
        if (rate == null) {
            throw new StructErrException("Memm viterbi error!");
        }
        return curRate * rate.rate(x, y0, y1);
    }

    public ConditionalTRate getRate() {
        return rate;
    }

    public void setRate(ConditionalTRate rate) {
        this.rate = rate;
    }
}
