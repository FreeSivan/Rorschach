package sivan.yue.nlp.common.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ṩ���ֵ�����ֵ��ŵ�ӳ��
 * �ṩ�۲�ֵ���۲�ֵ��ŵ�ӳ��
 * Created by xiwen.yxw on 2017/4/20.
 */
public class SampleDict {

    public static SampleDict instance = new SampleDict();

    private Map<String, Integer> viewDict = new HashMap<>();

    private Map<String, Integer> stateDict = new HashMap<>();

    private Map<Integer, List<Integer>> invertMap = new HashMap<>();

    private int viewIndex = 0;

    private int stateIndex = 0;

    private SampleDict() {}

    /**
     * �ṩ�۲�ֵ���۲�ֵ��ŵ�ӳ��
     * @param view
     * @return
     */
    public int reflectView(String view){
        Integer value = viewDict.get(view);
        return value == null ? -1 : value;
    }

    /**
     * �ṩ���ֵ�����ֵ��ŵ�ӳ��
     * @param State
     * @return
     */
    public int reflectState(String State){
        Integer value = stateDict.get(State);
        return value == null ? -1 : value;
    }

    /**
     *
     * @param fName
     * @throws java.io.IOException
     */
    public void loadViewDict(String fName, int num) throws IOException {
        stateDict.put(fName, num);
        for (String str : FileIteratorUtil.readLines(fName)) {
            if (viewDict.get(str) != null) {
                continue;
            }
            System.out.println("str = " + str + " index = " + viewIndex);
            viewDict.put(str, viewIndex++);
        }
        for (String str : FileIteratorUtil.readLines(fName)) {
            Integer index = viewDict.get(str);
            if (index == null) {
                continue;
            }
            if (null == invertMap.get(index)) {
                List<Integer> lst = new ArrayList<>();
                invertMap.put(index, lst);
            }
            List<Integer> lst = invertMap.get(index);
            if (lst.contains(num)) {
                continue;
            }
            lst.add(num);
        }
    }

    public void clearView() {
        viewIndex = 0;
        viewDict.clear();
    }

    public void clearState() {
        stateIndex = 0;
        stateDict.clear();
    }

    public void loadSample(String fName, String dstName) throws IOException {
        FileLineWriter fw = new FileLineWriter(dstName);
        for (String str : FileIteratorUtil.readLines(fName)) {
            String[] strArr = str.split(" ");
            String preState = "0";
            for (String item : strArr) {
                item = item.substring(1, item.length()-1);
                String[] itemArr = item.split("\\|");
                String line = reflectView(itemArr[0]) + "|" + itemArr[1] + "|" + preState;
                fw.writeLine(line);
                preState = itemArr[1];
            }
        }
        fw.close();
    }

    public void exportNum(String numName) {

        try {
            File file = new File(numName);
            RandomAccessFile realFile = new RandomAccessFile(file, "rw");
            realFile.writeInt(stateDict.size());
            realFile.writeInt(viewDict.size());
            realFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportDict(String dictName) {
        FileLineWriter fw = new FileLineWriter(dictName);
        for (Map.Entry<String, Integer> entry : viewDict.entrySet()) {
            String line = entry.getKey() + "|" +entry.getValue();
            fw.writeLine(line);
        }
        fw.close();
    }

    public void exportInvert(String fileName) {
        FileLineWriter fw = new FileLineWriter(fileName);
        for (Map.Entry<Integer, List<Integer>> entry : invertMap.entrySet()) {
            String line = "";
            line += entry.getKey();
            line += "|";
            for (Integer val : entry.getValue()) {
                line += val;
                line += ",";
            }
            fw.writeLine(line);
        }
        fw.close();
    }
}
