package radio.exp;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

/**
 * @author wzy
 */
public class MusicTest {
    public void play(){
        try {
            //MIDI(Musical Instrument Digital Interface)乐器数字接口
            //getSequencer()用于将mjdj信息组合成乐曲,sequencer类似一个发声装置
            Sequencer sequencer= MidiSystem.getSequencer();
            System.out.println("we get a sequencer");
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        MusicTest musicTest1=new MusicTest();
        musicTest1.play();
    }
}
