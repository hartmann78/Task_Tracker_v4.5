package Tracker_v4_5.model;

public class Task {
    public int id;
    public String name;
    public String description;
    public Status status;

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(Status status, String taskType) {
        this.status = status;
        System.out.println("Cтатуc " + taskType + " обновлён!");
    }

    @Override
    public String toString() {
        return name + ',' + status + ',' + description;
    }
}
