package rmi;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 获取service的通用服务
 *
 * @author wzy
 */
public class MiniMusicService implements Service {
    MyDrawPanel drawPanel;

    @Override
    public JPanel getGuiPanel() {
        JPanel panel = new JPanel();
        drawPanel = new MyDrawPanel();
        JButton playItButton = new JButton("Play it");
        playItButton.addActionListener(new PlayItListener());
        panel.add(drawPanel);
        panel.add(playItButton);
        return panel;
    }

    public class PlayItListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.addControllerEventListener(drawPanel, new int[]{127});
                Sequence seq = new Sequence(Sequence.PPQ, 4);
                Track track = seq.createTrack();
                for (int i = 0; i < 100; i += 4) {
                    int rNum = (int) ((Math.random() * 50) + 1);
                    if (rNum < 38) {
                        track.add(makeEvent(144, 1, rNum, 100, i));
                        track.add(makeEvent(176, 1, 127, 0, i));
                        track.add(makeEvent(128, 1, rNum, 100, i + 2));
                    }
                }
                sequencer.setSequence(seq);
                sequencer.start();
                //设置节拍速度
                sequencer.setTempoInBPM(220);
            } catch (MidiUnavailableException e1) {
                e1.printStackTrace();
            } catch (InvalidMidiDataException e1) {
                e1.printStackTrace();
            }

        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        ShortMessage a = new ShortMessage();
        try {
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return event;
    }

    class MyDrawPanel extends JPanel implements ControllerEventListener {
        //当获取到需要绘制的事件时
        boolean msg = false;

        @Override
        public void controlChange(ShortMessage event) {
            msg = true;
            repaint();
        }

        public Dimension getPreferedSize() {
            return new Dimension(300, 300);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (msg) {
                Graphics2D g2 = (Graphics2D) g;
                int r = (int) (Math.random() * 250);
                int gr = (int) (Math.random() * 250);
                int b = (int) (Math.random() * 250);

                g.setColor(new Color(r, gr, b));

                int ht = (int) ((Math.random() * 120) + 10);
                int width = (int) ((Math.random() * 120) + 10);
                int x = (int) ((Math.random() * 40) + 10);
                int y = (int) ((Math.random() * 40) + 10);
                g.fillRect(x, y, width, ht);
                msg = false;
            }
        }
    }

}
