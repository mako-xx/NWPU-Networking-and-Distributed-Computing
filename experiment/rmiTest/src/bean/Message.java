package bean;

import java.util.Date;

/**
 * @className: Message
 * @description: 留言类，用于存储留言信息，包括留言者，接收者，留言时间，留言内容
 * @author: Yang Xixuan
 **/
public class Message {
    /**
     * 发送者
     */
    private String sender;

    /**
     * 接受者
     */
    private String receiver;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 留言时间
     */
    private Date time;

    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = new Date();
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }

    public String toString() {
        return "Message [sender=" + sender + ", receiver=" + receiver + ", content=" + content + ", time=" + time + "]";
    }
}
