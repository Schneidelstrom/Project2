package cmsc125.lab3.services;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import cmsc125.lab3.models.ProcessModel;

/**
 * FCFSSimulator
 * This simulates the first come first served cpu scheduling algorithm
 */
public class SJFSimulator extends NonPreemptiveSimulator {

    public SJFSimulator(List<ProcessModel> startingProcesses) {
        super(startingProcesses, (p1, p2) -> {
            int burstCompare = Integer.compare(p1.getBurstTime(), p2.getBurstTime());
            // If Bursts are equal, the one that arrived first (FCFS) wins the tie
            if (burstCompare == 0) {
                return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
            }
            return burstCompare;
        });
    }
}
