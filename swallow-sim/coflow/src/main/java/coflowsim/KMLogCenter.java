package coflowsim;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public enum KMLogCenter {
    INSTANCE;

    private ArrayList<String> logList = new ArrayList<String>();

    public void addLog(String log) {
        this.logList.add(log);
    }

    public void description() {
        System.out.print("************* KMLogCenter ************* \n");
        for(int i = 0; i < logList.size(); i++){
            System.out.print(logList.get(i) + "\n");
        }
        System.out.print("*************** Log End *************** \n");
    }

    public void saveLog() {

        System.out.println("Save Log to File ...");

        FileOutputStream fos =null;
        try {
            fos = new FileOutputStream(new File("./KMCoflowAutoSimLog.txt"));
        }
        catch(Exception e){
            System.out.println(e.toString());
        }


        PrintStream p = new PrintStream(fos);

        p.print("************* KMLogCenter ************* \n");
        for(int i = 0; i < logList.size(); i++){
            p.print(logList.get(i) + "\n");
        }
        p.print("*************** Log End *************** \n");

        System.out.println("Save Log Finished.");
    }
}
