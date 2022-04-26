package service;

import model.Task;

import java.util.Comparator;

public class TaskDateComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {

        if (o1.getId() == o2.getId()) return 0;

        if ((o1.getStartTime() == null) & (o2.getStartTime() != null)) return 1;

        if ((o1.getStartTime() != null) & (o2.getStartTime() == null)) return -1;

        if ((o1.getStartTime() == null) & (o2.getStartTime() == null)) return (int) (o1.getId() - o2.getId());

        if (o1.getStartTime().isEqual(o2.getStartTime())) return (int) (o1.getId() - o2.getId());

        return o1.getStartTime().compareTo(o2.getStartTime());

    }
}
