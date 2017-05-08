package sivan.yue.nlp.train;

import sivan.yue.nlp.train.model.HmmTrain;

/**
 * Created by xiwen.yxw on 2017/5/8.
 */
public class HmmTrainTest {
    public static void main(String[] args) {
        HmmTrain hmmTrain = new HmmTrain();
        hmmTrain.train("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample",
                "D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\model", 10, 1046550);
    }
}
