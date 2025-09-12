package todoapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class TaskStorage {
    private static final Path FILE_PATH = Paths.get("tasks.json");
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Task> loadTasks() {
        try {
            if (Files.exists(FILE_PATH)) {
                return Arrays.asList(mapper.readValue(FILE_PATH.toFile(), Task[].class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(); // fallback: empty list
    }

    public static void saveTasks(List<Task> tasks) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(FILE_PATH.toFile(), tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
