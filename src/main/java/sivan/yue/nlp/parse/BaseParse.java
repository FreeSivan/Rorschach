package sivan.yue.nlp.parse;

import sivan.yue.nlp.common.tools.CConst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by xiwen.yxw on 2017/4/10.
 */
public abstract class BaseParse implements IParse{

    protected int stateNum;

    protected int viewNum;

    @Override
    public void load(String path) {
        // ��������������
        loadNumber(path);
        // ���ظ���ģ��
        loadModel(path);
    }

    protected abstract void loadModel(String path);

    private void loadNumber(String path) {
        try {
            String fileName = path + "/" + CConst.FILE_NUM;
            File file = new File(fileName);
            RandomAccessFile realFile = new RandomAccessFile(file, "r");
            stateNum = realFile.readInt();
            viewNum = realFile.readInt();
            System.out.println("stateNum = " + stateNum + " | viewNum = " + viewNum);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
