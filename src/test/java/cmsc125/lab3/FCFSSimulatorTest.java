package cmsc125.lab3;

import org.junit.jupiter.api.Test;

import cmsc125.lab3.models.ProcessModel;
import cmsc125.lab3.services.FCFSSimulator;
import cmsc125.lab3.services.GenerateRandomProcesses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FCFSSimulatorTest {

    private List<ProcessModel> processes;
    private FCFSSimulator simulator;

    @BeforeEach
    void setUp() {
        processes = new ArrayList<>();
    }

    @Test
    @DisplayName("Basic FCFS: P1 arrives before P2")
    void testBasicFCFS() {
        processes.add(new ProcessModel("P1", 3, 0, 1)); // Ends at 3
        processes.add(new ProcessModel("P2", 2, 1, 2)); // Starts at 3, Ends at 5
        simulator = new FCFSSimulator(processes);

        // Run until completion
        while (simulator.executeStep());

        ProcessModel p1 = processes.get(0);
        ProcessModel p2 = processes.get(1);

        assertEquals(0, p1.getWaitingTime(), "P1 should have 0 waiting time");
        assertEquals(3, p1.getTurnaroundTime(), "P1 turnaround should be 3");
        assertEquals(2, p2.getWaitingTime(), "P2 waiting time should be 2 (Starts at 3, Arrived at 1)");
    }

    @Test
    @DisplayName("Video Example")
    void testBasicFCFS2() {
        processes.add(new ProcessModel("P1", 5, 0, 1)); // Ends at 3
        processes.add(new ProcessModel("P2", 3, 1, 2)); // Ends at 3
        processes.add(new ProcessModel("P3", 8, 2, 3)); // Ends at 3
        processes.add(new ProcessModel("P4", 6, 3, 5)); // Ends at 3
        simulator = new FCFSSimulator(processes);

        while (simulator.executeStep());

        ProcessModel p1 = processes.get(0);
        ProcessModel p2 = processes.get(1);
        ProcessModel p3 = processes.get(2);
        ProcessModel p4 = processes.get(3);


        assertEquals(5, p1.getCompletionTime(), "P1 should have 5 completion time");
        assertEquals(5, p1.getTurnaroundTime(), "P1 turnaround should be 5");
        assertEquals(0, p1.getWaitingTime(), "P1 should have 0 waiting time");


        assertEquals(8, p2.getCompletionTime(), "P2 should have 8 completion time");
        assertEquals(7, p2.getTurnaroundTime(), "P2 turnaround should be 7");
        assertEquals(4, p2.getWaitingTime(), "P2 should have 4 waiting time");

        assertEquals(16, p3.getCompletionTime(), "P3 should have 16 completion time");
        assertEquals(14, p3.getTurnaroundTime(), "P3 turnaround should be 14");
        assertEquals(6, p3.getWaitingTime(), "P3 should have 6 waiting time");

        assertEquals(22, p4.getCompletionTime(), "P4 should have 22 completion time");
        assertEquals(19, p4.getTurnaroundTime(), "P4 turnaround should be 19");
        assertEquals(13, p4.getWaitingTime(), "P4 should have 13 waiting time");
    }

    @Test
    @DisplayName("Simultaneous Arrival: Ties broken by Queue order")
    void testSimultaneousArrival() {
        // Both arrive at 0. P1 is added first.
        processes.add(new ProcessModel("P1", 2, 0, 1));
        processes.add(new ProcessModel("P2", 2, 0, 2));
        simulator = new FCFSSimulator(processes);

        simulator.executeStep(); // Tick 0
        simulator.executeStep(); // Tick 1
        assertEquals("P1", simulator.getActiveProcessId(), "P1 should finish before P2 starts");
        
        simulator.executeStep(); // Tick 2
        assertEquals("P2", simulator.getActiveProcessId(), "P2 should start after P1 finishes");
    }

    @Test
    @DisplayName("CPU Idle: Gap between processes")
    void testCpuIdle() {
        processes.add(new ProcessModel("P1", 2, 0, 1)); // Finishes at 2
        processes.add(new ProcessModel("P2", 2, 5, 2)); // Arrives at 5
        simulator = new FCFSSimulator(processes);

        // Finish P1
        simulator.executeStep(); // 0
        simulator.executeStep(); // 1
        
        // Tick 2, 3, 4 should be IDLE
        simulator.executeStep(); 
        assertEquals("IDLE", simulator.getActiveProcessId());
        
        simulator.executeStep();
        simulator.executeStep();
        assertEquals("IDLE", simulator.getActiveProcessId());

        // Tick 5: P2 should start
        simulator.executeStep();
        assertEquals("P2", simulator.getActiveProcessId());
    }

    @Test
    @DisplayName("Random Generation Constraint: Count 3-20")
    void testRandomGenerationConstraints() {
        // Assuming you have a DataFactory or similar utility
        List<ProcessModel> randomProcesses = GenerateRandomProcesses.generateRandom(10); 
        
        assertTrue(randomProcesses.size() >= 3 && randomProcesses.size() <= 20, 
            "Process count must be between 3 and 20");
            
        for (ProcessModel p : randomProcesses) {
            assertTrue(p.getBurstTime() >= 1 && p.getBurstTime() <= 30, "Burst time range 1-30");
            assertTrue(p.getArrivalTime() >= 0 && p.getArrivalTime() <= 30, "Arrival time range 0-30");
        }
    }
}
