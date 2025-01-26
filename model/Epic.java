package Tracker_v4_5.model;

import java.util.HashMap;

public class Epic extends Task {
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }
}