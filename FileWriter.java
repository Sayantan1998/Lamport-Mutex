import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

class FileWriter {
    Node node;
    char type;             public FileWriter(Node node, char type) {
        this.node = node;
        this.type = type;
    }
        public void writeToFile() {
        String fileName = Main.OutputFile + "-" + node.nodeId + ".out";
        try {
            File file = new File(fileName);
            java.io.FileWriter fileWriter;
                        if(file.exists()){
                fileWriter = new java.io.FileWriter(file,true);
            }
                        else{
                fileWriter = new java.io.FileWriter(file);
            }
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        if(type == 'E')
                bufferedWriter.write(node.nodeId + " " + node.timestamp + " Enter critical session\n");
            else if(type == 'L')
                bufferedWriter.write(node.nodeId + " " + node.timestamp + " Leave critical session\n");
            else if(type == 'S'){
                Main.responseAvg = Main.responseAvg / Main.NumOfRequest;
                bufferedWriter.write(Mutex.messageSent.get() + " " + Main.responseAvg + " " + Main.timeExpense);
            }
                        bufferedWriter.close();
        }
        catch(IOException ex) {
            System.out.println("Error writing to file '" + fileName + "'");
        }
    }
}