import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<Task>();
    }

    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public int size() {
        return taskList.size();
    }

    public void add(Task t) {
        this.taskList.add(t);
    }

    public Task removeAt(int idx0) {
        return this.taskList.remove(idx0);
    }

    public Task get(int idx0) {
        return this.taskList.get(idx0);
    }

    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    public ArrayList<Task> raw() { return taskList; }              // fast, but leaks mutability
    public ArrayList<Task> copy() { return new ArrayList<>(taskList); }
}
