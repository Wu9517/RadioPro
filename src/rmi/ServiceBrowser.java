package rmi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author wzy
 */
public class ServiceBrowser {
    JPanel panel;
    JComboBox serviceList;
    ServiceServer server;

    public void buildGUI() {
        JFrame frame = new JFrame("RMI Browser");
        panel = new JPanel();
        frame.getContentPane().add(BorderLayout.CENTER, panel);

        //此方法执行RMJ registry查询，取得stub并调用getServiceList()
        Object[] services = this.getServiceList();

        serviceList = new JComboBox(services);

        frame.getContentPane().add(BorderLayout.NORTH, serviceList);

        serviceList.addActionListener(new MyListListener());

        frame.setSize(500, 500);
        frame.setVisible(true);

    }

    void loadService(Object serviceSelection) {
        try {
            Service service = server.getService(serviceSelection);
            //把实际的服务添加到GUI的panel中
            panel.removeAll();
            panel.add(service.getGuiPanel());
            panel.validate();
            panel.repaint();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    Object[] getServiceList() {
        Object obj = null;
        Object[] services = null;
        try {
            //执行RMJ查询，取得stub
            obj = Naming.lookup("rmi://127.0.0.1/ServiceServer");
            server = (ServiceServer) obj;
            services = server.getServiceList();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return services;
    }

    class MyListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object selection = serviceList.getSelectedItem();
            loadService(selection);
        }
    }

    public static void main(String[] args) {
        new ServiceBrowser().buildGUI();
    }
}
