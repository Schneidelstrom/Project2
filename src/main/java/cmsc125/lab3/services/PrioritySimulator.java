package cmsc125.lab3.services;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import cmsc125.lab3.models.ProcessModel;

/**
 * FCFSSimulator
 * This simulates the first come first served cpu scheduling algorithm
 */
public class PrioritySimulator extends BaseSimulator {

    public PrioritySimulator(List<ProcessModel> startingProcesses) {
        super(startingProcesses, (p1, p2) -> {
            // 1. Primary Sort: Priority Number (Lower is Higher)
            int priorityCompare = Integer.compare(p1.getPriority(), p2.getPriority());

            if (priorityCompare != 0) {
                return priorityCompare;
            }

            // 2. Secondary Sort: Arrival Time (FCFS tie-breaker)
            int arrivalCompare = Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());

            if (arrivalCompare != 0) {
                return arrivalCompare;
            }

            // 3. Final Tie-breaker: ID (Ensures stability/determinism)
            // This is why P2 was losing to P3 in your test!
            return p1.getProcessId().compareTo(p2.getProcessId());
        });
    }}
