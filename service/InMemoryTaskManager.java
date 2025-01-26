package Tracker_v4_5.service;

import Tracker_v4_5.model.Epic;
import Tracker_v4_5.model.Status;
import Tracker_v4_5.model.Subtask;
import Tracker_v4_5.model.Task;

import java.util.HashMap;
import java.util.Scanner;

public class InMemoryTaskManager implements TaskManager {
    public int id = 0;
    Scanner scanner = new Scanner(System.in);
    public HashMap<Integer, Task> taskList = new HashMap<>();
    public InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    public void returnId() {
        for (Task task : taskList.values()) {
            id = task.id;
            if (task instanceof Epic epic) {
                for (Subtask subtask : epic.subtasks.values()) {
                    id = subtask.id;
                }
            }
        }
    }

    @Override
    public void showFunctions() {
        System.out.println("Выберите функцию:");
        System.out.println("1 - получение списка задач");
        System.out.println("2 - удаление всех задач");
        System.out.println("3 - получение по идентификатору");
        System.out.println("4 - создание задач");
        System.out.println("5 - обновление данных");
        System.out.println("6 - обновление статуса");
        System.out.println("7 - удаление по идентификатору");
        System.out.println("8 - история просмотров задач");
        System.out.println("9 - завершить работу");
    }

    // обновление статуса эпика
    @Override
    public void epicStatusCheck() {
        for (Task task : taskList.values()) {
            int newCount = 0;
            int doneCount = 0;
            if (task instanceof Epic updateEpic && !updateEpic.subtasks.isEmpty()) {
                for (Subtask subtask : updateEpic.subtasks.values()) {
                    if (subtask.status == Status.NEW) {
                        newCount++;
                    } else if (subtask.status == Status.DONE) {
                        doneCount++;
                    }
                }

                if (newCount == updateEpic.subtasks.size()) {
                    updateEpic.setStatus(Status.NEW);
                } else if (doneCount == updateEpic.subtasks.size()) {
                    updateEpic.setStatus(Status.DONE);
                } else {
                    updateEpic.setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    // проверяет, отсутствуют ли задачи
    public Boolean isEmptyCheck() {
        if (taskList.isEmpty()) {
            System.out.println("Список задач пуст!");
            return true;
        }
        return false;
    }

    // выбор задачи
    public Task selector() {
        while (true) {
            System.out.println("Введите идентификатор:");
            int id = scanner.nextInt();
            if (id == 0) {
                System.out.println("Выход");
                return null;
            }
            Task getTask = getter(id);
            if (getTask == null) {
                System.out.println("Неверный идентификатор!");
            } else {
                return getTask;
            }
        }
    }

    // получение задачи
    public Task getter(int id) {
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else {
            for (Task task : taskList.values()) {
                if (task instanceof Epic epic && epic.subtasks.containsKey(id)) {
                    return epic.subtasks.get(id);
                }
            }
        }
        return null;
    }

    // возвращает строку в зависимости от типа задачи
    public String typeChecker(Task task) {
        return switch (task) {
            case Epic ignored -> "эпика";
            case Subtask ignored -> "подзадачи";
            default -> "задачи";
        };
    }

    // Функция 1
    @Override
    public void displayTasks() {
        if (isEmptyCheck()) return;
        for (Integer key : taskList.keySet()) {
            Task task = taskList.get(key);
            String taskType = typeChecker(task);
            displayTask(task, taskType, false);
            if (task instanceof Epic epic)
                for (int subTaskKey : epic.subtasks.keySet()) {
                    Subtask subtask = epic.subtasks.get(subTaskKey);
                    displayTask(subtask, "подзадачи", true);
                }
        }
    }

    // отображает задачу
    public void displayTask(Task task, String type, boolean isBlank) {
        String blank = (isBlank) ? "    " : "";
        System.out.println(blank + "Id " + type + ": " + task.id);
        System.out.println(blank + "Название " + type + ": " + task.name);
        System.out.println(blank + "Описание " + type + ": " + task.description);
        System.out.println(blank + "Статус " + type + ": " + task.status);
    }

    // Функция 2
    @Override
    public void deleteAll() {
        if (isEmptyCheck()) return;
        taskList.clear();
        historyManager.clearHistory();
        System.out.println("Все задачи удалены!");
    }

    // Функция 3
    @Override
    public void getById() {
        if (isEmptyCheck()) return;
        System.out.println("Получение по идентификатору");
        Task task = selector();
        if (task != null) {
            displayTask(task, typeChecker(task), false);
            historyManager.addTaskToHistory(task);
        }
    }

    // Функция 4
    @Override
    public void create() {
        while (true) {
            System.out.println("Что вы желаете создать?");
            System.out.println("1 - задача");
            System.out.println("2 - эпик");
            System.out.println("0 - выход");
            String command = scanner.next();
            switch (command) {
                case "1":
                    taskList.put(++id, taskCreator(id, false));
                    return;
                case "2":
                    taskList.put(++id, epicCreator(id, false, true));
                    return;
                case "0":
                    System.out.println("Выход");
                    return;
                default:
                    System.out.println("Неверная команда");
            }
        }
    }

    // создание задачи
    @Override
    public Task taskCreator(int taskId, boolean newCheck) {
        String isNew = (newCheck) ? " новое" : "";
        System.out.println("Введите" + isNew + " название задачи:");
        String taskName = scanner.next();
        System.out.println("Введите" + isNew + " описание задачи:");
        String taskDescription = scanner.next();
        return new Task(taskId, taskName, taskDescription, Status.NEW);
    }

    // создание эпика
    @Override
    public Epic epicCreator(int epicId, boolean newCheck, boolean createSubtasks) {
        String isNew = (newCheck) ? " новое" : "";
        System.out.println("Введите" + isNew + " название эпика:");
        String epicName = scanner.next();
        System.out.println("Введите" + isNew + " описание эпика:");
        String epicDescription = scanner.next();
        Epic newEpic = new Epic(epicId, epicName, epicDescription, Status.NEW);

        if (createSubtasks) {
            System.out.println("Сколько подзадач вы желаете добавить?");
            int subTasksCount = scanner.nextInt();
            for (int i = 1; i <= subTasksCount; i++) {
                Subtask newSubtask = subTaskCreator(++id, i, false);
                newEpic.subtasks.put(id, newSubtask);
            }
        }
        return newEpic;
    }

    // создание подзадачи
    @Override
    public Subtask subTaskCreator(int subTaskId, int count, boolean newCheck) {
        String isNew = (newCheck) ? " новое" : "";
        System.out.println("Подзадача " + count);
        System.out.println("Введите" + isNew + " название подзадачи:");
        String subTaskName = scanner.next();
        System.out.println("Введите" + isNew + " описание подзадачи:");
        String subtaskDescription = scanner.next();
        return new Subtask(subTaskId, subTaskName, subtaskDescription, Status.NEW);
    }

    // функция 5
    @Override
    public void updateTask() {
        if (isEmptyCheck()) return;
        System.out.println("Обновление данных");
        while (true) {
            System.out.println("Выберите старую задачу, которую желаете обновить.");
            Task oldTask = selector();
            if (oldTask instanceof Subtask) {
                for (Task task : taskList.values()) {
                    if (task instanceof Epic epic && epic.subtasks.containsValue(oldTask)) {
                        Subtask newSubtask = subTaskCreator(oldTask.id, 1, true);
                        epic.subtasks.replace(oldTask.id, epic.subtasks.get(oldTask.id), newSubtask);
                        return;
                    }
                }
            } else if (oldTask != null) {
                if (oldTask instanceof Epic) {
                    Epic newEpic = epicCreator(oldTask.id, true, false);
                    newEpic.subtasks = ((Epic) oldTask).subtasks;
                    taskList.replace(oldTask.id, taskList.get(oldTask.id), newEpic);
                } else {
                    taskList.replace(oldTask.id, taskList.get(oldTask.id), taskCreator(oldTask.id, true));
                }
                return;
            } else {
                return;
            }
        }
    }

    // Функция 6
    @Override
    public void updateStatus() {
        if (isEmptyCheck()) return;
        System.out.println("Обновление статуса");
        Task task = selector();
        if (task != null) {
            String type = typeChecker(task);
            while (true) {
                System.out.println("Введите новый статус " + type + ":");
                System.out.println("1 - Новый");
                System.out.println("2 - В процессе");
                System.out.println("3 - Выполнен");
                String newStatus = scanner.next();
                switch (newStatus) {
                    case "1":
                        task.setStatus(Status.NEW, type);
                        return;
                    case "2":
                        task.setStatus(Status.IN_PROGRESS, type);
                        return;
                    case "3":
                        task.setStatus(Status.DONE, type);
                        return;
                    case "0":
                        System.out.println("Выход");
                        return;
                    default:
                        System.out.println("Неверная команда!");
                        break;
                }
            }
        }
    }

    // Функция 7
    @Override
    public void deleteTask() {
        if (isEmptyCheck()) return;
        System.out.println("Удаление по идентификатору");
        Task task = selector();
        if (task instanceof Subtask) {
            for (Task task1 : taskList.values()) {
                if (task1 instanceof Epic epic && epic.subtasks.containsValue(task)) {
                    epic.subtasks.remove(task.id);
                }
            }
        } else if (task != null) {
            if (task instanceof Epic epic) {
                for (Subtask subtask : epic.subtasks.values()) {
                    historyManager.removeTaskInHistory(subtask.id);
                }
            }
            taskList.remove(task.id);
        } else {
            return;
        }
        historyManager.removeTaskInHistory(task.id);
        System.out.println("Удаление " + typeChecker(task) + " успешно выполнено!");
    }
}