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
@DisplayName("Table Validation: SJF Non-Preemptive (P1-P4)")
void testTableFromImage() {
    // Mapping image P0->P1, P1->P2, P2->P3, P3->P4
    processes.add(new ProcessModel("P1", 7, 0, 1)); 
    processes.add(new ProcessModel("P2", 4, 2, 2)); 
    processes.add(new ProcessModel("P3", 1, 4, 3)); 
    processes.add(new ProcessModel("P4", 4, 5, 4)); 
    
    simulator = new SJFSimulator(processes);

    // Run the full simulation
    while (simulator.executeStep());

    // Retrieve processes to verify stats
    ProcessModel p1 = processes.get(0);
    ProcessModel p2 = processes.get(1);
    ProcessModel p3 = processes.get(2);
    ProcessModel p4 = processes.get(3);

    // Assertions based on the provided table math
    assertAll("SJF Table Values",
        // P1: Starts at 0, ends at 7
        () -> assertEquals(7, p1.getCompletionTime(), "P1 Completion"),
        () -> assertEquals(7, p1.getTurnaroundTime(), "P1 Turnaround"),
        () -> assertEquals(0, p1.getWaitingTime(), "P1 Waiting"),

        // P3: Shortest among arrived (Burst 1), starts at 7, ends at 8
        () -> assertEquals(8, p3.getCompletionTime(), "P3 Completion"),
        () -> assertEquals(4, p3.getTurnaroundTime(), "P3 Turnaround"),
        () -> assertEquals(3, p3.getWaitingTime(), "P3 Waiting"),

        // P2: Next shortest (Burst 4, arrived at 2), starts at 8, ends at 12
        () -> assertEquals(12, p2.getCompletionTime(), "P2 Completion"),
        () -> assertEquals(10, p2.getTurnaroundTime(), "P2 Turnaround"),
        () -> assertEquals(6, p2.getWaitingTime(), "P2 Waiting"),

        // P4: Last (Burst 4, arrived at 5), starts at 12, ends at 16
        () -> assertEquals(16, p4.getCompletionTime(), "P4 Completion"),
        () -> assertEquals(11, p4.getTurnaroundTime(), "P4 Turnaround"),
        () -> assertEquals(7, p4.getWaitingTime(), "P4 Waiting")
    );
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
