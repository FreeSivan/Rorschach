package sivan.yue.nlp.parse;

import sivan.yue.nlp.parse.model.HmmParse;

/**
 * Created by xiwen.yxw on 2017/5/8.
 */
public class HmmParseTest {
    public static void main(String[] args) {
        HmmParse hmmParse = new HmmParse();
        hmmParse.load("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\model");
        int[] input = new int[1];
        input[0] = 2;
        int[] tmp = hmmParse.parse(input);
        System.out.println(tmp[0]);
    }
}
