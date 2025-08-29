package lilbird.model;

import lilbird.task.Task;
import java.util.ArrayList;

/**
 * Wraps a list of tasks and provides operations to modify it.
 */
public class TaskList {
    private final ArrayList<Task> taskList;

    /**
     * Creates empty list of tasks
     */
    public TaskList() {
        this.taskList = new ArrayList<Task>();
    }

    /**
     * Creates a task list from an existing list of tasks.
     *
     * @param taskList List of tasks to initialize with.
     */
    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Returns the number of tasks in the list
     *
     * @return Number of tasks.
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Add a new task to the list.
     *
     * @param t Task to add.
     */
    public void add(Task t) {
        this.taskList.add(t);
    }

    /**
     * Removes a task at the given index.
     *
     * @param idx0 Zero-based index of the task to remove.
     * @return Task that was removed.
     */
    public Task removeAt(int idx0) {
        return this.taskList.remove(idx0);
    }

    /**
     * Return the task at the given index.
     *
     * @param idx0 Zero-based index of the task to be returned.
     * @return Task that was specified.
     */
    public Task get(int idx0) {
        return this.taskList.get(idx0);
    }

    /**
     * Returns whether the taskList is empty.
     *
     * @return True if the taskList is empty.
     */
    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    public ArrayList<Task> raw() {
        return taskList;
    }

    /**
     * Returns a copy of the taskList stored.
     *
     * @return A copy of the list of tasks
     */
    public ArrayList<Task> copy() {
        return new ArrayList<>(taskList);
    }
}
