public class Epic extends Task {

    public Epic(Integer id, String name, String details, Status status) {
        super(id, name, details, status);
    }

    public void setTaskStatus(Status status) {
        this.status = status;
    }

}
