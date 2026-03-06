package cmsc125.lab3.services;

import cmsc125.lab3.models.ProcessModel;
import java.util.Comparator;
import java.util.List;

public class SRTSimulator extends BaseSimulator {

    public SRTSimulator(List<ProcessModel> processes) {
        super(processes, Comparator.comparingInt(ProcessModel::getRemainingTime).thenComparingInt(ProcessModel::getArrivalTime));
    }

    @Override
    public boolean executeStep() {
        if (arrivalQueue.isEmpty() && processQueue.isEmpty() && currentRunningProcess == null) return false;

        // Handle arrivals
        boolean shorterJobArrived = false;
        while (!arrivalQueue.isEmpty() && arrivalQueue.peek().getArrivalTime() <= currentTime) {
            ProcessModel arrived = arrivalQueue.poll();
            processQueue.add(arrived);

            if (currentRunningProcess != null && arrived.getRemainingTime() < currentRunningProcess.getRemainingTime()) shorterJobArrived = true;
        }

        // Preemption logic
        if (shorterJobArrived && currentRunningProcess != null) {
            processQueue.add(currentRunningProcess);
            currentRunningProcess = null;
        }

        // Select Best Candidate (Shortest Remaining Time)
        if (currentRunningProcess == null && !processQueue.isEmpty()) currentRunningProcess = processQueue.poll();

        // 4. Run
        if (currentRunningProcess != null) {
            this.activeProcessId = currentRunningProcess.getProcessId();
            currentRunningProcess.setRemainingTime(currentRunningProcess.getRemainingTime() - 1);

            if (currentRunningProcess.getRemainingTime() <= 0) {
                currentRunningProcess.setCompletionTime(currentTime + 1);
                calculateStats(currentRunningProcess);
                currentRunningProcess = null;
            }
        } else this.activeProcessId = "IDLE";

        currentTime++;
        return true;
    }
}