public class Task {

    private Integer id;
    private String name;
    private String details;
    protected Status status;
    public enum Status { NEW, DONE, IN_PROGRESS; }

    public Task(Integer id, String name, String details, Status status) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.status = status;
    }

    public Integer getId() { return id; }

    public String getName() { return name; }

    public String getDetails() { return details; }

    public Status getStatus() { return status; }

    @Override
    public String toString() {
        return  (this.getClass().toString().substring(6)) + "{" +
                "taskId=" + id +
                ", taskName='" + name + '\'' +
                ", taskDetails.length()='" + details.length() + '\'' +
                ", taskStatus=" + status +
                '}';
    }

}
