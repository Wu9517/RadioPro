package list;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author wzy
 */
public class JuKeBox5 {
    List<Song> songList = new ArrayList<>();

    public static void main(String[] args) {
        new JuKeBox5().go();
    }

    public void go() {
        getSongs();
        System.out.println(songList);
        //匿名内部类的使用
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        System.out.println(songList);
    }


    void getSongs() {
        File file = new File("SongList");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("/");
                Song song = new Song();
                song.setTitle(tokens[0]);
                songList.add(song);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
