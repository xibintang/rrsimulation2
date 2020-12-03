public class Process {

    int pid;
    int arriveTime;
    int burstTime;
    int waitingTime;
    // constructor:
    Process(int pid,int arriveTime,int burstTime){
        this.pid = pid;
        this.arriveTime = arriveTime;
        this.burstTime = burstTime;
    }

    // an empty constructor
    Process(){}

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(int arriveTime) {
        this.arriveTime = arriveTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
}
