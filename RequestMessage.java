import java.io.Serializable;

class RequestMessage extends Message implements Serializable {
    String msg;
    int srcNodeID;
    int dstNodeID;
    int timestamp;

    public RequestMessage(Node node, int dstNodeID){
        synchronized(node){
            this.msg = "Request";
            this.srcNodeID = node.nodeId;
            this.dstNodeID = dstNodeID;
            this.timestamp = node.timestamp;
        }
    }
}