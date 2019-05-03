package radio;

import javax.sound.midi.*;

/**
 * 首个声音播放程序（执行后会发出一声钢琴音）
 * @author wzy
 */
public class MiniMusicApp {

    public void play(){
        try {
            //获取Sequencer,并将其打开。
            Sequencer player= MidiSystem.getSequencer();
            player.open();
            try {
                Sequence sequence=new Sequence(Sequence.PPQ,4);
                Track track=sequence.createTrack();
                //对Track加入MidiEvent
                ShortMessage a=new ShortMessage();
                a.setMessage(144,1,44,100);
                MidiEvent noteOn=new MidiEvent(a,1);
                track.add(noteOn);
                ShortMessage b=new ShortMessage();
                b.setMessage(128,1,44,100);
                MidiEvent noteOff=new MidiEvent(b,16);
                track.add(noteOff);
                //将sequence送到Sequencer里面。
                player.setSequence(sequence);
                player.start();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        MiniMusicApp mini=new MiniMusicApp();
        mini.play();
    }

}
