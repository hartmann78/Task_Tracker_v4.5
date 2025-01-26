package Tracker_v4_5.service;

import Tracker_v4_5.model.Epic;
import Tracker_v4_5.model.Subtask;
import Tracker_v4_5.model.Task;

public interface TaskManager {
    void epicStatusCheck();

    void showFunctions();

    void displayTasks();

    void deleteAll();

    void getById();

    void create();

    Task taskCreator(int taskId, boolean newCheck);

    Epic epicCreator(int epicId, boolean newCheck, boolean createSubtasks);

    Subtask subTaskCreator(int subTaskId, int count, boolean newCheck);

    void updateTask();

    void updateStatus();

    void deleteTask();
}