package lilbird.storage;

import lilbird.task.Task;
import lilbird.exception.LilBirdException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saving and loading tasks from a text file.
 * <p>
 * Each task is stored as a single line using the format defined by
 * {@link lilbird.task.Task#serialize()}.
 */
public class Storage {
    private final Path file;

    /**
     * Creates a new Storage instance bound to the given file path
     *
     * @param filePath Path of the file to load from and save to.
     */
    public Storage(String filePath) {
        this.file = Paths.get(filePath);
    }

    /**
     * Loads tasks from the save file.
     *
     * @return List of tasks read from the file.
     * @throws LilBirdException If the file cannot be read or parsed.
     */
    public List<Task> load() throws LilBirdException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) return tasks;
        try {
            List<String> lines =
                    Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line.isBlank()) continue;
                try {
                    tasks.add(Task.deserialize(line));
                } catch (LilBirdException corrupt) {
                    // Skip corrupted lines quietly;
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new LilBirdException (
                    "Cannot read save file: " + file.toString()
            );
        }
    }

    /**
     * Saves tasks to the save file.
     *
     * @param tasks Tasks to save.
     * @throws LilBirdException If the file cannot be written to.
     */
    public void save(List<Task> tasks) throws LilBirdException {
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>(tasks.size());
            for (Task t : tasks) {
                lines.add(t.serialize());
            }
            Files.write(
                    file, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new LilBirdException(
                    "Cannot write save file: " + file.toString());
        }
    }
}
