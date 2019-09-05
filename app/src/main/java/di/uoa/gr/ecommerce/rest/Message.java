package di.uoa.gr.ecommerce.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Message {

    private Integer id;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTime;
    private String message;
    private Boolean seen;
    private User fromUserID;
    private User toUserID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public User getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(User fromUserID) {
        this.fromUserID = fromUserID;
    }

    public User getToUserID() {
        return toUserID;
    }

    public void setToUserID(User toUserID) {
        this.toUserID = toUserID;
    }

    public Message() {
    }

    public Message(Integer id, Date dateTime, String message, Boolean seen, User fromUserID, User toUserID) {
        this.id = id;
        this.dateTime = dateTime;
        this.message = message;
        this.seen = seen;
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
    }
}
