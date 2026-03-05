package cmsc125.lab3;

import org.junit.jupiter.api.Test;

import cmsc125.lab3.models.ProcessModel;
import cmsc125.lab3.services.GenerateRandomProcesses;

import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class generateRandomProcessesTest {

    @Test
    @DisplayName("Constraint: Random process count must be between 3 and 20")
    void testRandomProcessCount() {
        // We test multiple times to ensure the randomness always stays in bounds
        for (int i = 0; i < 50; i++) {
            List<ProcessModel> processes = GenerateRandomProcesses.generateRandom(); 
            int size = processes.size();
            assertTrue(size >= 3 && size <= 20, 
                "Expected 3-20 processes, but got: " + size);
        }
    }

    @Test
    @DisplayName("Constraint: Burst, Arrival, and Priority ranges")
    void testDataRanges() {
        List<ProcessModel> processes = GenerateRandomProcesses.generateRandom();

        for (ProcessModel p : processes) {
            // Burst time 1-30
            assertTrue(p.getBurstTime() >= 1 && p.getBurstTime() <= 30, 
                "Burst time out of bounds for " + p.getProcessId());
            
            // Arrival time 0-30
            assertTrue(p.getArrivalTime() >= 0 && p.getArrivalTime() <= 30, 
                "Arrival time out of bounds for " + p.getProcessId());
            
            // Priority number 1-20
            assertTrue(p.getPriority() >= 1 && p.getPriority() <= 20, 
                "Priority out of bounds for " + p.getProcessId());
        }
    }

    @Test
    @DisplayName("Constraint: Priority numbers must be unique")
    void testUniquePriorities() {
        List<ProcessModel> processes = GenerateRandomProcesses.generateRandom();
        
        long uniqueCount = processes.stream()
            .map(ProcessModel::getPriority)
            .distinct()
            .count();

        assertEquals(processes.size(), uniqueCount, 
            "Priority numbers must be assigned without duplications");
    }
}
