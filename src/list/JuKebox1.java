package list;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wzy
 */
public class JuKebox1 {
    List<String> songList = new ArrayList<>();

    List<Song> songList2 = new ArrayList<>();

    public static void main(String[] args) {
        new JuKebox1().go();
    }

    public void go() {
        getSongs();
        System.out.println(songList2);
        Collections.sort(songList2);
        System.out.println(songList2);
    }

    void getSongs() {
        File file = new File("SongList");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("/");
                /*songList.add(tokens[0]);*/
                Song song = new Song();
                song.setTitle(tokens[0]);
                songList2.add(song);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
