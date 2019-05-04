package radio.exp;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.ShortMessage;
import javax.swing.*;
import java.awt.*;

/**
 * @author wzy
 */
public class MyDrawPanel extends JPanel implements ControllerEventListener {

    boolean msg = false;

    @Override
    protected void paintComponent(Graphics g) {
        //因为也有其他东西会引发重绘，所以要判断是否是ControllerEvent引发的
        if (msg) {
            Graphics2D g2 = (Graphics2D) g;

            /*g.setColor(Color.white);
            g.fillRect(0,0,this.getWidth(),this.getHeight());*/

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

    @Override
    public void controlChange(ShortMessage event) {
        msg = true;
        repaint();
    }
}
