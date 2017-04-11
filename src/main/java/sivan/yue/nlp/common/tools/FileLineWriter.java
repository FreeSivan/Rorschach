package sivan.yue.nlp.common.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xiwen.yxw on 2017/4/10.
 */
public class FileLineWriter {

    private FileWriter fw;

    private BufferedWriter bw;

    public FileLineWriter(String fileName) {
        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeLine(String str) {
        try {
            bw.write(str);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
