import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {

    private int timeQuantum;
    private int numOfProcess;
    private Process[] processesArray;
    private ArrayList<Integer> timeSlot;
    private Queue<Process> readyQueue = new LinkedList<>();

    // constructor:
    Scheduler(int timeQuantum,int numOfProcess){
        this.timeQuantum = timeQuantum;
        this.numOfProcess = numOfProcess;
        processesArray = new Process[numOfProcess];
    }

    public void insertToScheduler(Process p){

        // insert all processes into the array:
        int i = 0;
        while(true){
            // if the array is empty:
            if(processesArray[i]==null){
                // insert it
                processesArray[i] = p;
                break;
            }
            i++;
        }
    }

    public void sortArray(){

        int length = processesArray.length;

        for(int i=0; i<length-1; i++){
            for(int j=0; j<length-i-1; j++){
                if(processesArray[j].arriveTime > processesArray[j+1].arriveTime) {
                    // basically a bubble sort
                    Process temp = processesArray[j];
                    processesArray[j] = processesArray[j + 1];
                    processesArray[j + 1] = temp;
                }
            }
        }
    }

    public void execute(){
        // we first call the sort method to sort the processes
        sortArray();
        // make a ArrayList call timeSlot
        timeSlot = new ArrayList<>();

        // insert them into the queue:
        // we first add all elements to the ready queue
        for (int i=0; i<processesArray.length;i++){
            readyQueue.add(processesArray[i]);
        }

        // by this time, the ready has all elements and ready to pop the first and execute:
        while (true){
            // we get the first elements:
            Process process = readyQueue.peek();
            // we subtract the burst time by the time quantum:
            int timeUsed = 0;
            // if the bustTime is greater than the assigned time
            if (process.getBurstTime() >= timeQuantum){
                timeUsed = timeQuantum;
            }
            // if the burst time is less than 4, then we will have negative burstTime after
            process.setBurstTime(process.getBurstTime()-timeQuantum);

            // hence we will add it back to get 0:
            if(process.getBurstTime()<0){
                // i.e. if the burstTime is 3, 3-4=-1, Then we have a negative BurstTime.
                // I called it extraTime, which you use 0 to subtract it to get a positive number
                int extraTime = 0 - process.getBurstTime();
                // then, we reset the burstTime to 0. We can done that by adding the opposite.
                process.setBurstTime(process.getBurstTime() + extraTime);
                // finally, the time used will be 4(timeQuantum)-1(extraTime) = 3, which is correct.
                timeUsed = timeQuantum-extraTime;
            }
            // we recorded it using the ArrayList [pid,timeUsed]
            timeSlot.add(process.getPid());
            timeSlot.add(timeUsed);
            readyQueue.remove();
            // finally, we add it back to the queue at the end if it has remaining burst time:
            if (process.getBurstTime()>0){
                // this way the process will be added to the end of the queue
                readyQueue.add(process);
            }
            // we should stop the process after all burst times are 0:
            if (readyQueue.isEmpty()){
                break;
            }
        }
    }

    public void calculateTime(){

        double contextSwitch = -1;
        double totalTurnAroundTime = 0;
        double totalWaitingTime = 0;
        double timeSlotIndex = 0;
        double responseTimeIndex = 0;
        double utilization = 0;
        double turnAroundTimeCal = 0;
        double waitTimeCal = 0;
        double throughput = 0;
        double response = 0;
        // first, we wil calculate all the wait times for each process:
        for (int j=0; j<processesArray.length; j++){

            int waitTime = 0;
            int pid = processesArray[j].getPid();
            int firstPosition = 0;
            int secondPosition = 0;
            int turnAroundTime = 0;

            for (int k=0; k<timeSlot.size(); k++){
                // first, we want to find out from 0 to the first time the pid appears:
                if (k%2 == 0){ // even number condition, which only determines the pid

                    if (pid == (timeSlot.get(k))){ // if they match,
                        secondPosition = k;
                        // then we do the calculation
                        if (secondPosition - firstPosition > 1){
                            // for firstPosition equals to 0, which is the first time
                            if (firstPosition == 0){
                                for (int l=firstPosition; l<=secondPosition; l++){
                                    if (l%2!=0){
                                        waitTime = waitTime + timeSlot.get(l);
                                    }
                                }
                            }else{
                                // if not the first time,
                                for (int l=firstPosition+2; l<=secondPosition; l++){
                                    if (l%2!=0){
                                        waitTime = waitTime + timeSlot.get(l);
                                    }
                                }
                            }
                            // calculate the turnAroundTime, which is the lastTime the process appears
                            turnAroundTime = 0;
                            for (int i=0; i<=secondPosition+1; i++){
                                if (i%2!=0){
                                    turnAroundTime = turnAroundTime + timeSlot.get(i);
                                }
                            }

                        }
                        // if not, we don't. And we always update the positions.
                        firstPosition = secondPosition;
                    }
                }
            }
            System.out.println("pid: " + j + " " + "wait time: " + waitTime + " " + "turn around time: " + turnAroundTime);
            totalTurnAroundTime = totalTurnAroundTime + turnAroundTime;
            totalWaitingTime = totalWaitingTime + waitTime;
        }

        // calculate the time slot index
        for (int i=0; i<=timeSlot.size(); i++){
            if (i%2!=0){
                timeSlotIndex = timeSlotIndex + timeSlot.get(i);
            }
        }

        // calculate the total context switch time
        for (int i=0; i<=timeSlot.size(); i++){
            if (i%2==0){
                contextSwitch = contextSwitch + 1;
            }
        }

        // calculate the first 4 time slot index:
        int count = 0;
        for (int i=1; i<=timeSlot.size(); i++){
            if (i%2!=0){
                for (int j=1; j<=i;j++){
                    if (j%2!=0){
                        responseTimeIndex = responseTimeIndex + timeSlot.get(j);
                    }
                }
                count++;
                if (count==3){
                    break;
                }
            }
        }

        // calculation:

        contextSwitch = contextSwitch*(0.1*timeQuantum); // where context Switch is around 10 percent of the quantum
        utilization = timeSlotIndex/(timeSlotIndex + contextSwitch);
        turnAroundTimeCal = totalTurnAroundTime/numOfProcess;
        waitTimeCal = totalWaitingTime/numOfProcess;
        throughput = numOfProcess/(timeSlotIndex + contextSwitch);
        response = responseTimeIndex/numOfProcess;
        System.out.println("---------------------------------");
        System.out.println("Context Switch: " + contextSwitch);
        System.out.println("Turn around time: " + turnAroundTimeCal);
        System.out.println("Total waiting time: " + waitTimeCal);
        System.out.println("Utilization: " + utilization);
        System.out.println("Throughput: " + throughput);
        System.out.println("Response: " + response);


    }
}
