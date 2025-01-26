package Tracker_v4_5.utils;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(IOException message) {
        super(message);
    }
}
