package cmsc125.lab3.services;

import java.util.Random;

import cmsc125.lab3.models.ProcessModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateRandomProcesses {
    private static Random rand = new Random();

    public static List<ProcessModel> generateRandom() {
        int count = rand.nextInt(18) + 3;
        return generateRandom(count);
    }

    public static List<ProcessModel> generateRandom(int count) {
        List<ProcessModel> list = new ArrayList<>();
        
        // Create a list of priorities 1-20 and shuffle for uniqueness
        List<Integer> priorities = new ArrayList<>();
        for (int i = 1; i <= 20; i++) priorities.add(i);
        Collections.shuffle(priorities);

        for (int i = 0; i < count; i++) {
            list.add(new ProcessModel(
                rand.nextInt(30) + 1, // Burst 1-30
                rand.nextInt(31),     // Arrival 0-30
                priorities.get(i)     // Unique priority 1-20
            ));
        }
        return list;
    }

}
