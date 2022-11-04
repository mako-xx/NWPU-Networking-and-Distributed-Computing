package bean;

import java.util.Date;

/**
 * @className: Meeting
 * @description: 会议类，包含会议的主办方、参与者、开始时间、截至时间、话题、单独id
 * @author: Yang Xixuan
 **/
public class Meeting {
    private String host;
    private String participant;
    private Date start;
    private Date end;
    private String title;
    private String uuid;

    public Meeting(String host, String participant, Date start, Date end, String title) {
        this.host = host;
        this.participant = participant;
        this.start = start;
        this.end = end;
        this.title = title;
        this.uuid = java.util.UUID.randomUUID().toString();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Date getStartDate() {
        return start;
    }

    public void setStartDate(Date start) {
        this.start = start;
    }

    public Date getEndDate() {
        return end;
    }

    public void setEndDate(Date end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String toString() {
        return "Meeting [host=" + host + ", participant=" + participant + ", start=" + start + ", end=" + end + ", title="
                + title + ", uuid=" + uuid + "]";
    }
}
