package radio.exp.stream.QuizCard;

import java.io.*;

/**
 * 缓存流读取文本文件
 *
 * @author wzy
 */
public class ReadAFile {
    public static void main(String[] args) {
        File file = new File("Foo.text");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedWriter = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedWriter.readLine()) != null) {
                System.out.println(line);
            }
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
