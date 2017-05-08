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
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\0.txt",0);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\1.txt",1);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\2.txt",2);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\3.txt",3);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\4.txt",4);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\5.txt",5);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\6.txt",6);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\7.txt",7);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\8.txt",8);
            SampleDict.instance.loadViewDict("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\dict\\9.txt",9);
            SampleDict.instance.loadSample("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample\\sample.txt",
                    "D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample\\me_test.txt");
            SampleDict.instance.exportNum("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample\\num.txt");
            SampleDict.instance.exportInvert("D:\\code\\myself\\Rorschach\\src\\main\\resources\\data\\sample\\invert.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
