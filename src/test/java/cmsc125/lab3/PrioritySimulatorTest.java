package cmsc125.lab3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import cmsc125.lab3.models.ProcessModel;
import cmsc125.lab3.services.PrioritySimulator;
import static org.junit.jupiter.api.Assertions.*;

class PrioritySimulatorTest {

    private List<ProcessModel> processes;
    private PrioritySimulator simulator;

    @BeforeEach
    void setUp() {
        processes = new ArrayList<>();
    }

    @Test
    @DisplayName("Priority: Lower number should run before higher number")
    void testPrioritySelection() {
        // P1 arrives at 0. It starts immediately.
        processes.add(new ProcessModel("P1", 10, 0, 10)); // Priority 10
        
        // P2 and P3 arrive while P1 is running.
        // P2 has priority 1 (High), P3 has priority 5 (Medium).
        processes.add(new ProcessModel("P2", 2, 1, 1)); 
        processes.add(new ProcessModel("P3", 2, 2, 5)); 
        
        simulator = new PrioritySimulator(processes);

        // Run P1 for 10 ticks
        for (int i = 0; i < 10; i++) simulator.executeStep();
        
        // At Tick 10, P1 is done. Simulator must choose between P2 (1) and P3 (5).
        simulator.executeStep(); 
        assertEquals("P2", simulator.getActiveProcessId(), "Priority 1 should run before Priority 5");
    }

    @Test
    @DisplayName("Priority Tie-Breaker: Same priority should use FCFS")
    void testPriorityTieBreaker() {
        // Two processes with same priority arrive at different times
        processes.add(new ProcessModel("P1", 2, 0, 5));
        processes.add(new ProcessModel("P2", 2, 2, 1)); // High priority arrives at 2
        processes.add(new ProcessModel("P3", 2, 2, 1)); // Same high priority arrives at 2

        simulator = new PrioritySimulator(processes);

        // Run P1 to completion (2 ticks)
        simulator.executeStep();
        simulator.executeStep();

        // At Tick 2, P2 and P3 are in the queue with the same priority.
        // The one that was added to the arrival queue first should run.
        simulator.executeStep();
        assertEquals("P2", simulator.getActiveProcessId(), "Tie-breaker should pick the first arrival");
    }

    @Test
    @DisplayName("Table Validation: Priority Non-Preemptive (P1-P4)")
    void testPriorityTableFromImage() {
        // Mapping from image: Process(Burst, Arrival, Priority)
        // P0 -> P1: Burst 7, Arrival 0, Priority 3
        processes.add(new ProcessModel("P1", 7, 0, 3)); 
        // P1 -> P2: Burst 4, Arrival 2, Priority 2
        processes.add(new ProcessModel("P2", 4, 2, 2)); 
        // P2 -> P3: Burst 1, Arrival 4, Priority 4
        processes.add(new ProcessModel("P3", 1, 4, 4)); 
        // P3 -> P4: Burst 4, Arrival 5, Priority 1 (Highest)
        processes.add(new ProcessModel("P4", 4, 5, 1)); 

        simulator = new PrioritySimulator(processes);

        // Run the full simulation
        while (simulator.executeStep());

        // Retrieve processes to verify stats
        ProcessModel p1 = processes.get(0);
        ProcessModel p2 = processes.get(1);
        ProcessModel p3 = processes.get(2);
        ProcessModel p4 = processes.get(3);

        assertAll("Priority Table Values",
            // P1: Starts at 0, ends at 7 (Priority 3)
        () -> assertEquals(7, p1.getCompletionTime(), "P1 Completion"),
        () -> assertEquals(7, p1.getTurnaroundTime(), "P1 Turnaround"),
        () -> assertEquals(0, p1.getWaitingTime(), "P1 Waiting"),

            // P4: Highest Priority (1), starts at 7, ends at 11
        () -> assertEquals(11, p4.getCompletionTime(), "P4 Completion"),
        () -> assertEquals(6, p4.getTurnaroundTime(), "P4 Turnaround"),
        () -> assertEquals(2, p4.getWaitingTime(), "P4 Waiting"),

            // P2: Next Priority (2), starts at 11, ends at 15
        () -> assertEquals(15, p2.getCompletionTime(), "P2 Completion"),
        () -> assertEquals(13, p2.getTurnaroundTime(), "P2 Turnaround"),
        () -> assertEquals(9, p2.getWaitingTime(), "P2 Waiting"),

            // P3: Lowest Priority (4), starts at 15, ends at 16
        () -> assertEquals(16, p3.getCompletionTime(), "P3 Completion"),
        () -> assertEquals(12, p3.getTurnaroundTime(), "P3 Turnaround"),
        () -> assertEquals(11, p3.getWaitingTime(), "P3 Waiting")
        );
    }
}
