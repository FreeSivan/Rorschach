package sivan.yue.nlp.common.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by xiwen.yxw on 2017/4/10.
 */
public class FileIteratorUtil {


    public static Iterable<String> readLines(String filename) throws IOException {

        final FileReader fr = new FileReader(filename);

        final BufferedReader br = new BufferedReader(fr);

        Iterable<String> result =  new Iterable<String>() {

            public Iterator<String> iterator() {

                //返回文件迭代器，实现迭代接口。
                return new Iterator<String>() {

                    public boolean hasNext() {
                        //判断下一行是否为空。
                        return line != null;
                    }

                    //返回当前行，同时读取文件的下一行作为当前行。
                    public String next() {
                        String retVal = line;
                        line = getLine();
                        return retVal;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    String getLine() {
                        String line;
                        try {
                            line = br.readLine();
                        }
                        catch (IOException ioEx) {
                            line = null;
                        }
                        return line;
                    }

                    String line = getLine();
                };
            }
        };
        br.close();
        fr.close();
        return result;
    }
}
