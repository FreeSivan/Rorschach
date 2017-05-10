package sivan.yue.nlp.train;

import sivan.yue.nlp.train.model.MemmTrain;

/**
 * Created by xiwen.yxw on 2017/5/9.
 */
public class MemmTrainTest {
    public static void main(String[] args) {
        MemmTrain memmTrain = new MemmTrain();
        memmTrain.train("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample",
                "D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\model", 10, 1046550);
    }
}
