import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server extends Thread {
    Node node;
    int port;
    String hostName;
    int nodeID;

    public Server(Node node) {
        this.node = node;
        this.port = node.port;
        this.hostName = node.hostName;
        this.nodeID = node.nodeId;
    }

    public void run() {
        try {
            ServerSocket serverSock = new ServerSocket(port);
            System.out.println(hostName + "(" + nodeID + ")" + " server socket listening...");

            while (true) {
                Socket sock = serverSock.accept();
                new Client(sock, node).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}