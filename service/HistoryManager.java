package Tracker_v4_5.service;

import Tracker_v4_5.model.Task;

import java.util.List;

public interface HistoryManager {
    void addTaskToHistory(Task task);

    void removeTaskInHistory(int id);

    List<Task> getHistory();
}