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

    @Override
    public String serialize() {
        return String.join(" | ",
                    type.getSymbol(),
                    isDone ? "1" : "0",
                    escape(description)
                ) + serializeTagsSuffix();
    }

    @Override
    public String toString() {
        return super.toString() + formatTagsSuffix();
    }
}
