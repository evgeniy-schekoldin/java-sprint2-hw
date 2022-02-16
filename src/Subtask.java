public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, String name, String details, Status status, Integer epicId) {
        super(id, name, details, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

}
