import java.io.*;
import java.util.*;

public class Main {
    public static int nodeID, NumOfNode, meanOfd, meanOfc, NumOfRequest;
    public static String ConfigFile, OutputFile;
    public static int[][] connectTable;
    public static boolean runMAP = true;
    public static ArrayList<int[]> output = new ArrayList<int[]>();
    public static long responseTime;
    public static long responseAvg = 0;
    public static long timeExpense = System.currentTimeMillis();

    public static void main(String args[]) throws IOException {
        Main.ConfigFile = args[1];
        FileReader R = new FileReader(Main.ConfigFile);
        Main.OutputFile = Main.ConfigFile.substring(0, Main.ConfigFile.lastIndexOf('.'));
        nodeID = Integer.parseInt(args[0]);
        Node node = new Node(nodeID);
        Main.NumOfNode = R.getConfigVariables()[0];
        Main.meanOfd = R.getConfigVariables()[1];
        Main.meanOfc = R.getConfigVariables()[2];
        Main.NumOfRequest = R.getConfigVariables()[3];
        int request = Main.NumOfRequest;

        node.socketListen();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        ;
        node.createChannels();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        ;
        while (true) {
            synchronized (node) {
                if (request > 0 && node.isCSRequestSent == false) {
                    responseTime = System.currentTimeMillis();
                    Mutex.csEnter(node);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    ;
                    request--;
                } else if (Mutex.releaseRecieved.get() == ((NumOfNode - 1) * NumOfRequest) &&
                        node.isCSRequestSent == false && !(request > 0)) {
                    timeExpense = System.currentTimeMillis() - timeExpense;
                    new FileWriter(node, 'S').writeToFile();
                    System.out.println(node.nodeId + " exit program");
                    System.exit(0);
                }
            }
        }
    }
}
