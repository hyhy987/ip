import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path file;

    public Storage(String filePath) {
        this.file = Paths.get(filePath);
    }

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
                    // Stretch goal behavior: skip corrupted lines quietly;
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new LilBirdException (
                    "Cannot read save file: " + file.toString()
            );
        }
    }

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
