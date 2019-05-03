package radio.exp.innerClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 内部类实例
 *
 * @author wzy
 */
public class TwoButtons {
    JFrame frame;
    JLabel label;

    public void go() {
        frame = new JFrame();
        //相对于this传给监听的注册方法，现在传的是对应的实例
        JButton labelButton = new JButton("Change Label");
        labelButton.addActionListener(new LabelListener());

        JButton colorButton = new JButton("Change Circle");
        colorButton.addActionListener(new colorListener());

        label = new JLabel("I'm a label");
        frame.getContentPane().add(BorderLayout.SOUTH, colorButton);
        frame.getContentPane().add(BorderLayout.CENTER, labelButton);
        frame.getContentPane().add(BorderLayout.WEST, label);
        frame.setSize(300, 300);
        frame.setVisible(true);

    }

    //内部类，监听labelButton
    class LabelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            label.setText("Ouch!");
        }
    }

    //内部类，监听colorButton
    class colorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.repaint();
        }
    }

    public static void main(String[] args) {
        TwoButtons t = new TwoButtons();
        t.go();
    }
}
