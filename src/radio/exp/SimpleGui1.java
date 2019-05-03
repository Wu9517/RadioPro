package radio.exp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wzy
 */
public class SimpleGui1 implements ActionListener{
    private JButton button;
    public static void main(String[] args)  {
       SimpleGui1 simpleGui1=new SimpleGui1();
       simpleGui1.go();

    }

    private void go(){
        JFrame frame=new JFrame();
         button=new JButton();
        //向按钮注册，添加监听事件
         button.addActionListener(this);
        //这一行用于关闭window时，也会同时把程序结束掉
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(button);
        frame.setSize(300,300);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //处理事件
        button.setText("I've been clicked!");
    }
}
