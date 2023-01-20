import java.io.Serializable;

class ReplyMessage extends Message implements Serializable {
    String msg;
    int srcNodeID;
    int dstNodeID;
    int timestamp;

    public ReplyMessage(Node node, int dstNodeID){
        synchronized(node){
            this.msg = "Reply";
            this.srcNodeID = node.nodeId;
            this.dstNodeID = dstNodeID;
            this.timestamp = node.timestamp;
        }
    }
}