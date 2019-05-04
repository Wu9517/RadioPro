package radio.exp.innerClass;

import javax.swing.*;
import java.awt.*;

/**
 * 简单的动画轨迹移动
 * @author wzy
 */
public class SimpleAnimation {
    int x=70,y=70;
    public void go(){
        JFrame frame=new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyDrawPanel myDrawPanel=new MyDrawPanel();
        frame.getContentPane().add(myDrawPanel);
        frame.setSize(300,300);
        frame.setVisible(true);

        for(int i=0;i<130;i++){
            x++;
            y++;
            myDrawPanel.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class MyDrawPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            //清除移动轨迹，this.getWidth(),this.getHeight()为整块区域的宽高
            g.setColor(Color.black);
            g.fillRect(0,0,this.getWidth(),this.getHeight());

            //原点位置
            g.setColor(Color.green);
            g.fillOval(x,y,40,40);//使用外部的坐标值来更新
        }
    }

    public static void main(String[] args)  {
        SimpleAnimation simpleAnimation=new SimpleAnimation();
        simpleAnimation.go();
    }
}
