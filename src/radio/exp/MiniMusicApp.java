package radio.exp;

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
                //创建message
                ShortMessage a=new ShortMessage();
                //置入指令（按顺序分别是指令、频道（代表不同乐器）、音符（0-127代表不同音高）
                // 、音道（用多大的音道按下，0代表听不见，100中等）），144代表打开,128代表关闭
                a.setMessage(144,1,44,100);
                //用message创建MidiEvent
                //第二个参数代表音长
                MidiEvent noteOn=new MidiEvent(a,1);
                //将MidiEvent加入到Track中
                track.add(noteOn);
                ShortMessage b=new ShortMessage();
                b.setMessage(128,1,44,100);
                MidiEvent noteOff=new MidiEvent(b,3);
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
