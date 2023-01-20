import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

class Client extends Thread {
    Socket cSocket;
    Node node;

    public Client(Socket cSocket, Node node) {
        this.cSocket = cSocket;
        this.node = node;
    }

    public void run() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(cSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Message message;
                message = (Message) ois.readObject();
                synchronized (node) {
                    if (message instanceof RequestMessage) {
                        Mutex.getRequestMsg(node, (RequestMessage) message);
                    }
                    if (message instanceof ReplyMessage) {
                        Mutex.getReplyMessage(node, (ReplyMessage) message);
                    }
                    if (message instanceof ReleaseMessage) {
                        Mutex.getReleaseMessage(node, (ReleaseMessage) message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}