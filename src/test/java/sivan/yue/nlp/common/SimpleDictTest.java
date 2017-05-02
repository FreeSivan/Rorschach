package sivan.yue.nlp.common;

import sivan.yue.nlp.common.tools.SampleDict;

import java.io.IOException;

/**
 * Created by xiwen.yxw on 2017/4/26.
 */
public class SimpleDictTest {

    public static void main(String[] args) {
        try {
            SampleDict.instance.clearState();
            SampleDict.instance.clearView();
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\0.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\1.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\2.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\3.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\4.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\5.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\6.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\7.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\8.txt");
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\9.txt");
            SampleDict.instance.loadSample("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample\\sample.txt",
                    "D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample\\1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
