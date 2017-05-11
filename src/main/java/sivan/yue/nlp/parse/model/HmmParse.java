package sivan.yue.nlp.parse.model;

import sivan.yue.nlp.common.dataAlgo.matrix.towDimMatrix.SparseDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalDRate;
import sivan.yue.nlp.common.dataAlgo.viterbi.module.HmmViterbi;
import sivan.yue.nlp.common.tools.CConst;
import sivan.yue.nlp.common.tools.FileIteratorUtil;
import sivan.yue.nlp.parse.BaseParse;

import java.io.IOException;

/**
 * Created by xiwen.yxw on 2017/3/28.
 */
public class HmmParse extends BaseParse{

    private ConditionalDRate sRate = null;

    private ConditionalDRate tRate = null;

    private HmmViterbi viterbi = new HmmViterbi();

    public HmmParse() {}

    public HmmParse(ConditionalDRate sRate, ConditionalDRate tRate) {
        this.sRate = sRate;
        this.tRate = tRate;
        viterbi.setsRate(sRate);
        viterbi.settRate(tRate);
    }

    @Override
    public int[] parse(int[] words) {
        return viterbi.viterbi(words, stateNum);
    }

    @Override
    protected void loadModel(String path) {
        try {
            loadTRate(path);
            loadSRate1(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTRate(String path) throws IOException {
        tRate = new ConditionalDRate();
        SparseDMatrix matrix = new SparseDMatrix(stateNum, stateNum);
        tRate.setRate(matrix);
        String fileName = path + "/" + CConst.HMM_FILE_TRAN;
        for (String str : FileIteratorUtil.readLines(fileName)) {
            String[] strArr = str.split("\\|");
            if (strArr.length != 3) {
                continue;
            }
            int x = Integer.parseInt(strArr[0]);
            int y = Integer.parseInt(strArr[1]);
            double rate = Double.parseDouble(strArr[2]);
            tRate.setRate(x, y, rate, 0);
        }
        viterbi.settRate(tRate);
    }

    private void loadSRate(String path) throws IOException {
        sRate = new ConditionalDRate();
        SparseDMatrix matrix = new SparseDMatrix(stateNum, viewNum);
        sRate.setRate(matrix);
        String fileName = path + "/" + CConst.HMM_FILE_SHOW;
        for (String str : FileIteratorUtil.readLines(fileName)) {
            String[] strArr = str.split("\\|");
            if (strArr.length != 3) {
                continue;
            }
            int x = Integer.parseInt(strArr[0]);
            int y = Integer.parseInt(strArr[1]);
            double rate = Double.parseDouble(strArr[2]);
            sRate.setRate(x, y, rate, 0);
        }
        viterbi.setsRate(sRate);
    }

    private void loadSRate1(String path) {
        String fileName = path + "/" + CConst.HMM_FILE_SHOW;
        sRate = new ConditionalDRate();
        SparseDMatrix matrix = new SparseDMatrix(stateNum, viewNum);
        matrix.load(fileName);
        sRate.setRate(matrix);
        viterbi.setsRate(sRate);
    }

    public ConditionalDRate getsRate() {
        return sRate;
    }

    public void setsRate(ConditionalDRate sRate) {
        this.sRate = sRate;
        viterbi.setsRate(sRate);
    }

    public ConditionalDRate gettRate() {
        return tRate;
    }

    public void settRate(ConditionalDRate tRate) {
        this.tRate = tRate;
        viterbi.settRate(tRate);
    }

}
