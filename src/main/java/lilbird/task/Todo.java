package lilbird.task;

/**
 * Represents a simple to-do task with only a description.
 */
public class Todo extends Task {

    /**
     * Creates a Todo task with the given description.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
