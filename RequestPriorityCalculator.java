import java.util.Comparator;

public class RequestPriorityCalculator implements Comparator<RequestMessage> {
    @Override
    public int compare(RequestMessage m1, RequestMessage m2) {
        if (m1.timestamp < m2.timestamp) {
            return -1;
        }
        if (m1.timestamp > m2.timestamp) {
            return 1;
        }
        if (m1.timestamp == m2.timestamp) {
            if (m1.srcNodeID < m2.srcNodeID) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }
}
