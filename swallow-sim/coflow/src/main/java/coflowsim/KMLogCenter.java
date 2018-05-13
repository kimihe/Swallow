package coflowsim;
import java.util.ArrayList;

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
}
