package cmsc125.lab3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import cmsc125.lab3.models.ProcessModel;
import cmsc125.lab3.services.SJFSimulator;
import static org.junit.jupiter.api.Assertions.*;

class SJFSimulatorTest {

    private List<ProcessModel> processes;
    private SJFSimulator simulator;

    @BeforeEach
    void setUp() {
        processes = new ArrayList<>();
    }

    @Test
    @DisplayName("SJF: Shorter job in Ready Queue should run before longer job")
    void testSJFSelection() {
        // P1 arrives at 0, Burst 5. It will start immediately because the queue is empty.
        processes.add(new ProcessModel("P1", 5, 0, 1)); 
        // P2 and P3 arrive while P1 is running.
        processes.add(new ProcessModel("P2", 8, 1, 2)); // Longer
        processes.add(new ProcessModel("P3", 2, 2, 3)); // Shorter
        
        simulator = new SJFSimulator(processes);

        // Run P1 to completion (5 ticks)
        for (int i = 0; i < 5; i++) simulator.executeStep();
        
        // At Tick 5, P1 is done. Simulator must choose between P2 (8) and P3 (2).
        // It should pick P3 because it is Shortest Job First.
        simulator.executeStep(); 
        assertEquals("P3", simulator.getActiveProcessId(), "SJF should have picked P3 (Burst 2) over P2 (Burst 8)");
    }

    @Test
    @DisplayName("SJF Non-Preemptive: Current process must finish even if a shorter one arrives")
    void testNonPreemptiveBehavior() {
        processes.add(new ProcessModel("P1", 10, 0, 1)); // Starts at 0
        processes.add(new ProcessModel("P2", 2, 2, 2));  // Arrives at 2
        
        simulator = new SJFSimulator(processes);

        // Run 5 ticks. P1 is still running.
        for (int i = 0; i < 5; i++) {
            simulator.executeStep();
            assertEquals("P1", simulator.getActiveProcessId(), "P1 should not be preempted by P2");
        }
    }

    @Test
    @DisplayName("SJF Math: Verify Waiting and Turnaround times")
    void testSJFStats() {
        // P1: Burst 4, Arrival 0 -> Completion 4
        // P2: Burst 2, Arrival 0 -> Starts 4, Completion 6
        processes.add(new ProcessModel("P1", 4, 0, 1));
        processes.add(new ProcessModel("P2", 2, 0, 2));
        simulator = new SJFSimulator(processes);

        while (simulator.executeStep());

        ProcessModel p2 = processes.stream()
            .filter(p -> p.getProcessId().equals("P2"))
            .findFirst().get();

        ProcessModel p1 = processes.stream()
            .filter(p -> p.getProcessId().equals("P1"))
            .findFirst().get();

        // P2 stats:
        // Completion: 6
        // Turnaround: 6 - 0 = 6
        // Waiting: 6 - 4 = 2
        assertEquals(2, p2.getCompletionTime());
        assertEquals(2, p2.getTurnaroundTime());
        assertEquals(0, p2.getWaitingTime());

        assertEquals(6, p1.getCompletionTime());
        assertEquals(6, p1.getTurnaroundTime());
        assertEquals(2, p1.getWaitingTime());
    }
}
