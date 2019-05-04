package radio.exp.stream;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 写入文本数据（字符串）-文本流
 *
 * @author wzy
 */
public class WriteAFile {
    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("Foo.text");
            writer.write("Hello World!");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
