import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Node {
    public int nodeId;
    public String hostName;
    public int port;
    public int timestamp;
    public boolean isCSRequestSent;
    FileReader reader = new FileReader(Main.ConfigFile);
    HashMap<Integer, Socket> channels = new HashMap<Integer, Socket>();
    HashMap<Integer, ObjectOutputStream> outputMap = new HashMap<Integer, ObjectOutputStream>();

    public Node(int nodeId) {
        this.nodeId = nodeId;
        this.hostName = reader.getHostByNodeID(nodeId);
        this.port = reader.getPortByNodeID(nodeId);
        this.timestamp = 0;
        this.isCSRequestSent = false;
    }

    public void socketListen() {
        new Server(this).start();
    }

    public void createChannels() throws IOException, UnknownHostException {
        for (int i = 0; i < Main.NumOfNode; i++) {
            if (i != Main.nodeID) {
                String hostName = reader.getHostByNodeID(i);
                int port = reader.getPortByNodeID(i);
                InetAddress address = InetAddress.getByName(hostName);
                Socket client = new Socket(address, port);
                channels.put(i, client);
                ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                outputMap.put(i, oos);
            }
        }
    }
}