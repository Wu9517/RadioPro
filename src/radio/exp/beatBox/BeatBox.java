package radio.exp.beatBox;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author wzy
 */
public class BeatBox {
    JFrame theFrame;
    JPanel mainPanel;
    JList incomingList;
    JTextField userMessage;
    ArrayList<JCheckBox> checkBoxes;

    Sequencer sequencer;
    Sequence sequence;
    Track track;
    Sequence mySequence;

    int nextNum;
    Vector<String> listVector = new Vector<String>();
    String userName;
    HashMap<String, boolean[]> otherSeqsMap = new HashMap<String, boolean[]>();
    ObjectOutputStream out;
    ObjectInputStream in;




    //乐器名称
    String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal",
            "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell",
            "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};

    //实例的乐器的关键字
    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    public void startUp() {
        try {
            //设置网络、输入输出、并创建出reader的线程
            Socket socket = new Socket("127.0.0.1", 4242);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Thread remote = new Thread(new RemoteReader());
            remote.start();
            buildGUI();
        } catch (IOException e) {
            System.out.println("couldn't connection! you'll hava to play alone.");
            e.printStackTrace();
        }
    }

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }



    public void buildGUI() {
        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel backGround = new JPanel(layout);
        //设置面板上摆设组件时的空白边缘
        backGround.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        checkBoxes = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new MyStartListener());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new MyStopListener());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Temp Up");
        upTempo.addActionListener(new MyUpTempoListener());
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Temp Down");
        downTempo.addActionListener(new MyDownTempoListener());
        buttonBox.add(downTempo);

        JButton saveTempo = new JButton("save");
        saveTempo.addActionListener(new MySendListener());
        buttonBox.add(saveTempo);

        JButton readTempo = new JButton("read");
        readTempo.addActionListener(new MyReadInListener());
        buttonBox.add(readTempo);

        JButton sendIt = new JButton("sendIt");
        sendIt.addActionListener(new MySendListener2());
        buttonBox.add(sendIt);
        userMessage = new JTextField();
        buttonBox.add(userMessage);

        //收到信息的组件
        incomingList = new JList();
        incomingList.addListSelectionListener(new MyListSelectionListener());
        incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane theList = new JScrollPane(incomingList);
        buttonBox.add(theList);
        incomingList.setListData(listVector);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            nameBox.add(new Label(instrumentNames[i]));
        }

        backGround.add(BorderLayout.EAST, buttonBox);
        backGround.add(BorderLayout.WEST, nameBox);

        theFrame.getContentPane().add(backGround);

        GridLayout grid = new GridLayout(16, 16);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);
        backGround.add(BorderLayout.CENTER, mainPanel);

        for (int i = 0; i < 256; i++) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            checkBoxes.add(checkBox);
            mainPanel.add(checkBox);
        }

        setUpMidi();

        theFrame.setBounds(50, 50, 300, 300);
        //pack() 调整此窗口的大小，以适合其子组件的首选大小和布局,使用setSize()时，则不能使用该方法
        theFrame.pack();
        theFrame.setVisible(true);
    }

    //将复选框状态转换为midi事件并加到Track上
    public void buildTrackAndStart() {
        //创建出16个元素的数组来存储一项乐器的值，如果该节应该要演奏，其值会是关键字值，否则值为零
        int[] trackList = null;
        //清除掉旧的track创建一个新的
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        //对每个乐器都执行一次
        for (int i = 0; i < 16; i++) {
            trackList = new int[16];
            //设定代表乐器的关键字
            int key = instruments[i];

            for (int j = 0; j < 16; j++) {
                JCheckBox jc = checkBoxes.get(j + (16 * i));
                //如果有勾选，将关键字放在数组的该位置上，不然的话就补零
                if (jc.isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            }
            //创建此乐器的事件并加到track上
            makeTracks(trackList);
            track.add(makeEvent(176, 1, 127, 0, 16));
        }
        //确保第16拍有事件，否则beatBox不会重复播放
        track.add(makeEvent(192, 9, 1, 0, 15));

        try {
            sequencer.setSequence(sequence);
            //指定无穷的重复次数
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            //开始播放
            sequencer.start();
            //设置每分钟的节拍数量，BPM节拍数
            sequencer.setTempoInBPM(120);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }


    }

    //创建某项乐器的所有事件
    public void makeTracks(int[] list) {
        for (int i = 0; i < 16; i++) {
            int key = list[i];
            if (key != 0) {
                //创建NOTE ON和NOTE OFF事件并加入到Track上
                track.add(makeEvent(144, 9, key, 100, i));
                track.add(makeEvent(128, 9, key, 100, i + 1));
            }
        }
    }

    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
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


    class MyStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("enter");
            buildTrackAndStart();
        }
    }

    class MyStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.stop();
        }
    }

    class MyUpTempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("up");
            //节奏音子，预设为1.0，每次调整3%
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * 1.03));

        }
    }

    class MyDownTempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("down");
            //节奏音子，预设为1.0，每次调整3%
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * 0.97));
        }
    }

    class MySendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(theFrame);
            boolean[] checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                JCheckBox checkBox = checkBoxes.get(i);
                if (checkBox.isSelected()) {
                    checkboxState[i] = true;
                } else {
                    checkboxState[i] = false;
                }
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileSave.getSelectedFile()));
                for (int j = 0; j < checkboxState.length; j++) {
                    writer.write(String.valueOf(checkboxState[j]) + "\n");
                }
                writer.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }


        }
    }

    class MyReadInListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(theFrame);
                BufferedReader reader = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                String line = null;
                int index = 0;
                while ((line = reader.readLine()) != null) {
                    Boolean bol = Boolean.parseBoolean(line);
                    JCheckBox checkBox = checkBoxes.get(index);
                    checkBox.setSelected(bol);
                    index++;
                }
                reader.close();
                sequencer.stop();
                buildTrackAndStart();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }

    class MySendListener2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                JCheckBox checkBox = checkBoxes.get(i);
                if (checkBox.isSelected()) {
                    checkboxState[i] = true;
                }
            }
            String messageToSend = null;
            try {
                out.writeObject(userName + nextNum + "：" + userMessage.getText());
                out.writeObject(checkboxState);
            } catch (IOException e1) {
                System.out.println("Couldn't send it to server");
                e1.printStackTrace();
            }
            userMessage.setText("");
        }
    }

    class MyListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String selected = (String) incomingList.getSelectedValue();
                if (selected != null) {
                    //now got to the map,and change the sequence
                    boolean[] selectedState = otherSeqsMap.get(selected);
                    changeSequence(selectedState);
                    sequencer.stop();
                    buildTrackAndStart();
                }
            }
        }
    }

    class RemoteReader implements Runnable {
        boolean[] checkboxState = null;
        String nameToShow = null;
        Object obj = null;

        @Override
        public void run() {
            try {
                while ((obj = in.readObject()) != null) {
                    System.out.println("get an object from server");
                    System.out.println(obj.getClass());
                    nameToShow = (String) obj;
                    checkboxState = (boolean[]) in.readObject();
                    otherSeqsMap.put(nameToShow, checkboxState);
                    listVector.add(nameToShow);
                    incomingList.setListData(listVector);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeSequence(boolean[] checkboxState) {
        for (int i = 0; i < 256; i++) {
            JCheckBox check = checkBoxes.get(i);
            if (checkboxState[i]) {
                check.setSelected(true);
            } else {
                check.setSelected(false);
            }
        }
    }

    public static void main(String[] args) {
        new BeatBox().startUp();
    }

}
