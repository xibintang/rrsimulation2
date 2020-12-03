import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Simulator {

    public static void main(String[] args) {
        // parameter: time Quantum
        read(3);
    }

    public static void read(int timeQ){

        Queue<Integer> list = new LinkedList<>();
        try{

            BufferedReader reader = new BufferedReader(new FileReader("src/processes.csv"));
            reader.readLine();
            String line = null;
            while((line = reader.readLine())!=null){
                // this is a line that you will read from the csv file:
                String item[] = line.split(",");
                // now you have a line of data such as the pid, arrive and burst, all in string
                for (int i=0; i<item.length; i++){
                    int value = Integer.parseInt(item[i]);
                    list.add(value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int length = list.size()/3;
        Process[] processesArray = new Process[length];

        // use a loop to create as many process as assigned
        for (int i=0; i<processesArray.length; i++){
            int pid = list.remove();
            int arrive = list.remove();
            int burst = list.remove();
            processesArray[i] = new Process(pid,arrive,burst);
        }

        Scheduler scheduler = new Scheduler(timeQ,processesArray.length);

        for (int i=0; i<processesArray.length; i++){
            scheduler.insertToScheduler(processesArray[i]);
        }

        scheduler.execute();
        scheduler.calculateTime();

    }

}
