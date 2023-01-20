import java.io.Serializable;

class ReleaseMessage extends Message implements Serializable {
    String msg;
    int  srcNodeID;
    int dstNodeID;
    int timestamp;

    public ReleaseMessage(Node node, int dstNodeID){
        synchronized(node){
            this.msg = "Release";
            this.srcNodeID = node.nodeId;
            this.dstNodeID = dstNodeID;
            this.timestamp = node.timestamp;
        }
    }
}
