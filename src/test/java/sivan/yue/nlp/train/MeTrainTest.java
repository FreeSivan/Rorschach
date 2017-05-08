package sivan.yue.nlp.train;

import sivan.yue.nlp.train.model.MeTrain;

/**
 * Created by xiwen.yxw on 2017/5/2.
 */
public class MeTrainTest {
    public static void main(String[] args) {
        MeTrain meTrain = new MeTrain();
        meTrain.train("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample",
                "D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\model", 10, 1046550);
    }
}
