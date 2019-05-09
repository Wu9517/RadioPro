package radio.exp.beatBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author wzy
 */
public class BeanBoxServer {
    ArrayList<ObjectOutputStream> clientOutputStream;

    public void go() {
        clientOutputStream = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutputStream.add(out);

                Thread t = new Thread(new clientHandler(clientSocket));
                t.start();

                System.out.println("got a connection");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class clientHandler implements Runnable {
        ObjectInputStream in;
        Socket clientSocket;

        public clientHandler(Socket socket) {
            clientSocket = socket;
            try {
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Object o2 = null;
            Object o1 = null;
            try {
                while ((o1 = in.readObject()) != null) {
                    o2 = in.readObject();
                    System.out.println("read two objects");
                    tellEveryone(o1, o2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public void tellEveryone(Object one, Object two) {
            Iterator it = clientOutputStream.iterator();
            while (it.hasNext()) {
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                try {
                    out.writeObject(one);
                    out.writeObject(two);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new BeanBoxServer().go();
    }
}
