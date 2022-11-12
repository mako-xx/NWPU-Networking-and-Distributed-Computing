package bean;

import java.util.Date;

/**
 * @className: Todo
 * @description:
 * @author: Yang Xixuan
 **/
public class TodoItem {
    private String uuid;
    private String description;
    private User owner;

    private Date start;

    private Date end;

    public TodoItem(String description, User owner, Date start, Date end) {
        this.uuid = java.util.UUID.randomUUID().toString();
        this.description = description;
        this.owner = owner;
        this.start = start;
        this.end = end;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object obj) {
        return ((TodoItem) obj).getUuid().equals(uuid);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "uuid='" + uuid + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

}
