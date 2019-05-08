package rmi;

import javax.swing.*;
import java.io.Serializable;

/**
 * 定义任何通用服务都要实现的setGuiPanel()这个方法，因为继承Serializable,所以能够自动的序列化，
 * 而这也是通过网络传输服务所必需的机制
 *
 * @author wzy
 */
public interface Service extends Serializable {
    public JPanel getGuiPanel();

}
