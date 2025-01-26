package Tracker_v4_5.tests;

import Tracker_v4_5.model.Epic;
import Tracker_v4_5.model.Task;
import Tracker_v4_5.model.Subtask;
import Tracker_v4_5.model.Status;
import Tracker_v4_5.service.FileBackedTasksManager;
import Tracker_v4_5.service.InMemoryHistoryManager;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CreationTest {
    static FileBackedTasksManager fileBackedTasksManager;
    static InMemoryHistoryManager historyManager;

    static int id;
    static HashMap<Integer, Task> taskList;

    static String expected;
    static String sample;

    public static String getArrayListFromFile(String path) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        while (bufferedReader.ready()) {
            text.append(bufferedReader.readLine()).append('\n');
        }
        bufferedReader.close();
        return String.valueOf(text);
    }

    public void createSample() {
        taskList.put(++id, new Task(id, "1", "1", Status.NEW));

        Epic newEpic = new Epic(++id, "2", "2", Status.NEW);
        taskList.put(id, newEpic);

        for (int i = 0; i < 2; i++) {
            newEpic.subtasks.put(++id, new Subtask(id, Integer.toString(i + 3), Integer.toString(i + 3), Status.NEW));
        }

        taskList.put(++id, new Epic(id, "5", "5", Status.NEW));

        taskList.put(++id, new Task(id, "6", "6", Status.NEW));

        Epic newEpic2 = new Epic(++id, "7", "7", Status.NEW);
        taskList.put(id, newEpic2);

        newEpic2.subtasks.put(++id, new Subtask(id, "8", "8", Status.NEW));

        for (Task task : taskList.values()) {
            historyManager.addTaskToHistory(task);
            if (task instanceof Epic epic) {
                for (Subtask subtask : epic.subtasks.values())
                    historyManager.addTaskToHistory(subtask);
            }
        }
        fileBackedTasksManager.save();
    }

    @BeforeAll
    public static void createManagers() throws IOException {
        fileBackedTasksManager = new FileBackedTasksManager("C:\\Users\\Lenovo\\Desktop\\Java\\TaskTracker\\src\\Tracker_v4_5\\sample.csv");
        historyManager = new InMemoryHistoryManager();

        id = fileBackedTasksManager.id;
        taskList = fileBackedTasksManager.taskList;

        expected = getArrayListFromFile("C:\\Users\\Lenovo\\Desktop\\Java\\TaskTracker\\src\\Tracker_v4_5\\expected.csv");
        sample = getArrayListFromFile("C:\\Users\\Lenovo\\Desktop\\Java\\TaskTracker\\src\\Tracker_v4_5\\sample.csv");
    }

    @Test
    public void checkExpectedAndSample() {
        createSample();
        Assertions.assertEquals(expected, sample, "Files' contents don't match!");
    }
}