package Tracker_v4_5.service;

import Tracker_v4_5.model.Epic;
import Tracker_v4_5.model.Subtask;
import Tracker_v4_5.model.Task;
import Tracker_v4_5.utils.ManagerSaveException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public String filePath;
    InMemoryHistoryManager memoryHistoryManager = new InMemoryHistoryManager();

    public FileBackedTasksManager(String filePath) {
        this.filePath = filePath;
    }

    enum Types {
        TASK,
        EPIC,
        SUBTASK
    }

    public String read() throws IOException {
        return Files.readString(Path.of(filePath));
    }

    public void save() throws ManagerSaveException {
        try (Writer writer = new FileWriter(filePath)) {

            for (Task task : taskList.values()) {
                if (task instanceof Epic epic) {
                    writer.write(String.valueOf(epic.id) + ',' + Types.EPIC + ',' + epic + '\n');

                    for (Subtask subtask : epic.subtasks.values()) {
                        writer.write(String.valueOf(subtask.id) + ',' + Types.SUBTASK + ',' + subtask + ',' + epic.id + '\n');
                    }
                } else {
                    writer.write(String.valueOf(task.id) + ',' + Types.TASK + ',' + task + '\n');
                }
            }

            if (memoryHistoryManager.getTasks() != null) {
                StringBuilder text = new StringBuilder();
                for (Task task : memoryHistoryManager.getTasks()) {
                    text.append(task.id).append(',');
                }
                writer.write('\n' + text.substring(0, text.length() - 1));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    // Override super functions //

    @Override
    public void epicStatusCheck() {
        super.epicStatusCheck();
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public void getById() {
        super.getById();
        save();
    }

    @Override
    public void create() {
        super.create();
        save();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        save();
    }

    @Override
    public void updateStatus() {
        super.updateStatus();
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }
}
