package radio.exp;

import javax.sound.midi.*;
import javax.swing.*;

/**
 * @author wzy
 */
public class MiniMusicPlayer3 implements ControllerEventListener {
    static JFrame f = new JFrame("My First Music Video");
    static MyDrawPanel ml;

    public static void main(String[] args) {
        MiniMusicPlayer3 miniMusicPlayer3 = new MiniMusicPlayer3();
        miniMusicPlayer3.go();
    }

    public void setUpGui() {
        ml = new MyDrawPanel();
        f.setContentPane(ml);
        f.setBounds(30, 30, 300, 300);
        f.setVisible(true);
    }

    public void go() {
        setUpGui();
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            //向sequencer注册事件，注册的方法取用监听者于代表着想要监听的事件的int数组，我们只需要127数组
            int[] eventsIWant = {127};
            sequencer.addControllerEventListener(ml, eventsIWant);
            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();

            int r = 0;
            for (int i = 0; i < 60; i += 4) {
                r = (int) ((Math.random() * 50) + 1);
                track.add(makeEvent(144, 1, r, 100, i));
                //插入事件编号为127的自定义的ControllerEvent(176),它不会做任何事情，只是让我们知道
                //有音符被播放，因为它的tick和NOTE ON是同时进行的。
                track.add(makeEvent(176, 1, 127, 0, i));
                track.add(makeEvent(128, 1, r, 100, i + 2));
            }
            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(220);
            sequencer.start();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        ShortMessage shortMessage = new ShortMessage();
        try {
            shortMessage.setMessage(comd, chan, one, two);
            event = new MidiEvent(shortMessage, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public void controlChange(ShortMessage event) {
        System.out.println("la");
    }
}
