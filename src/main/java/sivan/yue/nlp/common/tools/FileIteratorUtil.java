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

                //�����ļ���������ʵ�ֵ����ӿڡ�
                return new Iterator<String>() {

                    public boolean hasNext() {
                        //�ж���һ���Ƿ�Ϊ�ա�
                        return line != null;
                    }

                    //���ص�ǰ�У�ͬʱ��ȡ�ļ�����һ����Ϊ��ǰ�С�
                    public String next() {
                        String retVal = line;
                        line = getLine();
                        return retVal;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                    String getLine() {
                        String line = null;
                        try {
                            line = br.readLine();
                        }
                        catch (IOException ioEx) {
                            ioEx.printStackTrace();
                            line = null;
                        }
                        return line;
                    }

                    String line = getLine();
                };
            }
        };
        return result;
    }
}
