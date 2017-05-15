package sivan.yue.nlp.parse.parseimpl;

import sivan.yue.nlp.common.dataAlgo.matrix.thrDimMatrix.SparseTMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalTRate;
import sivan.yue.nlp.parse.viterbiImpl.MemmViterbi;
import sivan.yue.nlp.common.tools.CConst;
import sivan.yue.nlp.common.tools.FileIteratorUtil;
import sivan.yue.nlp.parse.BaseParse;

import java.io.IOException;

/**
 * Created by xiwen.yxw on 2017/3/28.
 */
public class MemmParse extends BaseParse {

    private ConditionalTRate rateModule;

    private MemmViterbi viterbi = new MemmViterbi();

    public MemmParse(ConditionalTRate rate) {
        this.rateModule = rate;
        viterbi.setRate(rateModule);
    }

    @Override
    public int[] parse(int[] words) {
        return viterbi.viterbi(words, stateNum);
    }

    @Override
    protected void loadModel(String path) {
        rateModule = new ConditionalTRate();
        SparseTMatrix matrix = new SparseTMatrix(viewNum, stateNum, stateNum);
        rateModule.setRate(matrix);
        String fileName = path + "/"+ CConst.MEMM_NAME;
        try {
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length != 4) {
                    continue;
                }
                int x = Integer.parseInt(strArr[0]);
                int y1 = Integer.parseInt(strArr[1]);
                int y2 = Integer.parseInt(strArr[2]);
                double rate = Double.parseDouble(strArr[2]);
                rateModule.setRate(x, y1, y2, rate, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        viterbi.setRate(rateModule);
    }

    public ConditionalTRate getRateModule() {
        return rateModule;
    }

    public void setRateModule(ConditionalTRate rate) {
        this.rateModule = rate;
        viterbi.setRate(rateModule);
    }
}
