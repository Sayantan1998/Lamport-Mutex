import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Mutex {
    static Comparator<RequestMessage> priorityCalculator = new RequestPriorityCalculator();
    static PriorityQueue<RequestMessage> reqMessageQ = new PriorityQueue<RequestMessage>(10, priorityCalculator);
    static AtomicInteger replyRecieved = new AtomicInteger(0);
    static AtomicInteger releaseRecieved = new AtomicInteger(0);
    static AtomicInteger messageSent = new AtomicInteger(0);

    public static void csEnter(Node node) {
        synchronized (node) {
            replyRecieved.set(0);
            broadcastRequestMsg(node);
            RequestMessage message = new RequestMessage(node, 0);
            reqMessageQ.add(message);
            node.isCSRequestSent = true;
        }
    }

    public static void csLeave(Node node) {
        synchronized (node) {
            reqMessageQ.remove();
            broadcastReleaseMessage(node);
            replyRecieved.set(0);
            node.isCSRequestSent = false;
        }
    }

    public static void getRequestMsg(Node node, RequestMessage message) {
        synchronized (node) {
            node.timestamp = Math.max(node.timestamp, message.timestamp) + 1;
            reqMessageQ.add(message);
            sendReplyMessage(node, message.srcNodeID);
        }
    }

    public static void getReplyMessage(Node node, ReplyMessage message) {
        synchronized (node) {
            node.timestamp = Math.max(node.timestamp, message.timestamp) + 1;
            replyRecieved.incrementAndGet();
            checkCSEntry(node);
        }
    }

    public static void getReleaseMessage(Node node, ReleaseMessage message) {
        synchronized (node) {
            node.timestamp = Math.max(node.timestamp, message.timestamp) + 1;
            reqMessageQ.remove();
            checkCSEntry(node);
            releaseRecieved.incrementAndGet();
        }
    }

    public static void checkCSEntry(Node node) {
        synchronized (node) {
            if (reqMessageQ.size() != 0) {
                RequestMessage topMessage = reqMessageQ.peek();
                if (replyRecieved.get() == (Main.NumOfNode - 1) &&
                        topMessage.srcNodeID == node.nodeId) {
                    System.out.println("==== " + node.nodeId + " Enter critical session ====");
                    new FileWriter(node, 'E').writeToFile();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    ;
                    Main.responseTime = System.currentTimeMillis() - Main.responseTime;
                    Main.responseAvg += Main.responseTime;
                    new FileWriter(node, 'L').writeToFile();
                    csLeave(node);
                }
            }
        }
    }

    public static void broadcastRequestMsg(Node node) {
        RequestMessage message;
        synchronized (node) {
            node.timestamp++;
            for (int id = 0; id < Main.NumOfNode; id++) {
                try {
                    if (id != node.nodeId) {
                        message = new RequestMessage(node, id);
                        ObjectOutputStream oos = node.outputMap.get(id);
                        oos.writeObject(message);
                        oos.flush();
                        messageSent.incrementAndGet();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void sendReplyMessage(Node node, int dstID) {
        ReplyMessage message;
        synchronized (node) {
            try {
                node.timestamp++;
                message = new ReplyMessage(node, dstID);
                ObjectOutputStream oos = node.outputMap.get(dstID);
                oos.writeObject(message);
                oos.flush();
                messageSent.incrementAndGet();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void broadcastReleaseMessage(Node node) {
        ReleaseMessage message;
        synchronized (node) {
            node.timestamp++;
            for (int id = 0; id < Main.NumOfNode; id++) {
                try {
                    if (id != node.nodeId) {
                        message = new ReleaseMessage(node, id);
                        ObjectOutputStream oos = node.outputMap.get(id);
                        oos.writeObject(message);
                        oos.flush();
                        messageSent.incrementAndGet();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}