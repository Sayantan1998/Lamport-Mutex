import java.io.*;

public class FileReader {
    public String fileName;

    public FileReader(String fileName) {
        this.fileName = fileName;
    }
    public int[] getConfigVariables() {
        String line;
        String lineSplit[];
        int configVariables[] = null;
        int lineNumber = 0;
        try (
                InputStream fis = new FileInputStream(this.fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("#(.*)", "");
                if (line.matches("^\\s*$")) continue;
                line = line.replaceAll("^\\s+", "");
                lineSplit = line.split("\\s+");
                lineNumber++;

                if (lineNumber == 1) {
                    configVariables = new int[lineSplit.length];
                    for (int i = 0; i < lineSplit.length; i++)
                        configVariables[i] = Integer.parseInt(lineSplit[i]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return configVariables;
    }

    public String getHostByNodeID(int nodeID) {

        String line;
        int lineNumber = 0;
        int nodeNumber = 0;
        String hostName = null;
        String[] hostList;

        try (
                InputStream input = new FileInputStream(this.fileName);
                InputStreamReader streamReader = new InputStreamReader(input);
                BufferedReader buffer = new BufferedReader(streamReader);
        ) {
            while ((line = buffer.readLine()) != null) {
                line = line.replaceAll("#(.*)", "");
                if (line.matches("^\\s*$")) continue;
                line = line.replaceAll("^\\s+", "");
                hostList = line.split("\\s+");
                lineNumber++;
                if (lineNumber == 1) {
                    nodeNumber = Integer.parseInt(hostList[0]);
                } else if ((lineNumber - 1) <= nodeNumber && nodeID == Integer.parseInt(hostList[0])) {
                    hostName = hostList[1];
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return hostName;
    }

    public int getPortByNodeID(int nodeID) {
        String line;
        int port = 0;
        int lineNumber = 0;
        int nodeNumber = 0;
        String[] lineSplit;


        try (
                InputStream input = new FileInputStream(this.fileName);
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader buffer = new BufferedReader(reader);
        ) {
            while ((line = buffer.readLine()) != null) {
                line = line.replaceAll("#(.*)", "");
                if (line.matches("^\\s*$")) continue;
                line = line.replaceAll("^\\s+", "");
                lineSplit = line.split("\\s+");
                lineNumber++;
                if (lineNumber == 1) {
                    nodeNumber = Integer.parseInt(lineSplit[0]);
                } else if ((lineNumber - 1) <= nodeNumber && nodeID == Integer.parseInt(lineSplit[0])) {
                    port = Integer.parseInt(lineSplit[2]);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return port;
    }


}
