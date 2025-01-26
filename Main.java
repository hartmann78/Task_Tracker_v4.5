package Tracker_v4_5;

import Tracker_v4_5.service.InMemoryHistoryManager;
import Tracker_v4_5.service.InMemoryTaskManager;
import Tracker_v4_5.utils.Managers;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        InMemoryTaskManager taskManager = Managers.loadFromFile();
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        System.out.println("Трекер задач");
        while (true) {
            taskManager.returnId();
            taskManager.epicStatusCheck();
            taskManager.showFunctions();
            String command = scanner.next();
            switch (command) {
                case "1":
                    taskManager.displayTasks();
                    break;
                case "2":
                    taskManager.deleteAll();
                    break;
                case "3":
                    taskManager.getById();
                    break;
                case "4":
                    taskManager.create();
                    break;
                case "5":
                    taskManager.updateTask();
                    break;
                case "6":
                    taskManager.updateStatus();
                    break;
                case "7":
                    taskManager.deleteTask();
                    break;
                case "8":
                    historyManager.showHistory(historyManager.getHistory());
                    break;
                case "9":
                    System.out.println("Завершение...");
                    return;
                default:
                    System.out.println("Такая команда отсутствует!");
                    break;
            }
        }
    }
}