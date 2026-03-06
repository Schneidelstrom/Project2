package cmsc125.lab3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import cmsc125.lab3.models.ProcessModel;
import cmsc125.lab3.services.RoundRobinSimulator;
import static org.junit.jupiter.api.Assertions.*;

class RoundRobinSimulatorTest {

    private List<ProcessModel> processes;
    private RoundRobinSimulator simulator;

    @BeforeEach
    void setUp() {
        processes = new ArrayList<>();
    }

    @Test
    @DisplayName("RR Logic: Swapped IDs should not affect execution order")
    void testIDIndependence() {
        // We name the first arrival P99 and the second arrival P1
        // Standard RR follows Arrival Time, not ID alphabetical order
        processes.add(new ProcessModel("P2", 5, 0, 1)); // Arrives first
        processes.add(new ProcessModel("P1", 2, 1, 2));  // Arrives second
        
        simulator = new RoundRobinSimulator(processes, 2);

        // Tick 0-2: P99 should run despite 'higher' ID number
        simulator.executeStep();
        assertEquals("P2", simulator.getActiveProcessId(), "Should pick P2 based on arrival time 0");
    }

    @Test
    @DisplayName("RR: Verify Quantum Preemption with Manual IDs")
    void testQuantumPreemptionManual() {
        processes.add(new ProcessModel("P1", 5, 0, 1)); 
        processes.add(new ProcessModel("P2", 2, 0, 2)); 
        
        simulator = new RoundRobinSimulator(processes, 2);

        // Tick 0-2: P10 runs
        simulator.executeStep();
        simulator.executeStep();
        
        // Tick 2: P10 is preempted, P20 starts
        simulator.executeStep();
        assertEquals("P2", simulator.getActiveProcessId());
    }

    @Test
    @DisplayName("RR: Handover vs Preemption Timing (The P2 Arrival at T=2 Case)")
    void testHandoverPriority() {
        // P1 runs 0-3. P2 arrives at T=2.
        // At T=3, P2 should be at the front of the queue, P1 at the back.
        processes.add(new ProcessModel("P1", 6, 0, 1)); 
        processes.add(new ProcessModel("P2", 2, 2, 2)); 
        
        simulator = new RoundRobinSimulator(processes, 3);

        // Run P1 for 3 ticks
        for (int i = 0; i < 3; i++) simulator.executeStep();

        // Next step should pick P2
        simulator.executeStep();
        assertEquals("P2", simulator.getActiveProcessId(), "P2 (new arrival) should be ahead of P1 (preempted)");
    }

    @Test
    @DisplayName("RR Stats: Verification of Completion and Waiting Times")
    void testRoundRobinStatsManual() {
        // Data from a standard textbook example
        processes.add(new ProcessModel("P1", 4, 0, 1));
        processes.add(new ProcessModel("P2", 1, 0, 2));
        simulator = new RoundRobinSimulator(processes, 2);

        while (simulator.executeStep());

        ProcessModel p1 = processes.stream().filter(p -> p.getProcessId().equals("P1")).findFirst().get();
        ProcessModel p2 = processes.stream().filter(p -> p.getProcessId().equals("P2")).findFirst().get();

        // Timeline: 
        // 0-2: P1 (Rem: 2)
        // 2-3: P2 (Finishes, CT=3)
        // 3-5: P1 (Finishes, CT=5)
        
        assertAll("Round Robin Metrics",
            () -> assertEquals(5, p1.getCompletionTime(), "P1 CT"),
            () -> assertEquals(3, p2.getCompletionTime(), "P2 CT"),
            () -> assertEquals(1, p1.getWaitingTime(), "P1 WT"),
            () -> assertEquals(2, p2.getWaitingTime(), "P2 WT")
        );
    }

    @Test
    @DisplayName("RR: Textbook Table Validation (Quantum = 3ms)")
    void testRoundRobinFromImage() {
        // Data from image: Process(ID, Burst, Arrival, Priority)
        // Note: Priority is not used in Round Robin but included for constructor consistency
        processes.add(new ProcessModel("P0", 7, 0, 0)); 
        processes.add(new ProcessModel("P1", 4, 2, 0)); 
        processes.add(new ProcessModel("P2", 1, 4, 0)); 

        // Quantum = 3ms as shown in image
        simulator = new RoundRobinSimulator(processes, 3);

        // Run the full simulation
        while (simulator.executeStep());

        // Retrieve processes for verification
        ProcessModel p0 = processes.get(0);
        ProcessModel p1 = processes.get(1);
        ProcessModel p2 = processes.get(2);

        assertAll("Round Robin Textbook Results",
            // P2: Completion Time 10, Turnaround 6, Waiting 5
        () -> assertEquals(10, p2.getCompletionTime(), "P2 Completion"),
        () -> assertEquals(6, p2.getTurnaroundTime(), "P2 Turnaround"),
        () -> assertEquals(5, p2.getWaitingTime(), "P2 Waiting"),

            // P1: Completion Time 11, Turnaround 9, Waiting 5
        () -> assertEquals(11, p1.getCompletionTime(), "P1 Completion"),
        () -> assertEquals(9, p1.getTurnaroundTime(), "P1 Turnaround"),
        () -> assertEquals(5, p1.getWaitingTime(), "P1 Waiting"),

            // P0: Completion Time 12, Turnaround 12, Waiting 5
        () -> assertEquals(12, p0.getCompletionTime(), "P0 Completion"),
        () -> assertEquals(12, p0.getTurnaroundTime(), "P0 Turnaround"),
        () -> assertEquals(5, p0.getWaitingTime(), "P0 Waiting")
        );
    }
}
