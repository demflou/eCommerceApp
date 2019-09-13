package di.uoa.gr.ecommerce.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BidsPK {
    private int itemID;
    private String bidderID;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date dateTime;

    public BidsPK() {
    }

    public BidsPK(int itemID, String bidderID, Date dateTime) {
        this.itemID = itemID;
        this.bidderID = bidderID;
        this.dateTime = dateTime;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getBidderID() {
        return bidderID;
    }

    public void setBidderID(String bidderID) {
        this.bidderID = bidderID;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
