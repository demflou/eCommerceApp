package di.uoa.gr.ecommerce.rest;

public class myBid {
    protected BidsPK bidsPK;
    private Float amount;
    private myItem item;
    private User user;

    public BidsPK getBidsPK() {
        return bidsPK;
    }

    public void setBidsPK(BidsPK bidsPK) {
        this.bidsPK = bidsPK;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public myItem getItem() {
        return item;
    }

    public void setItem(myItem item) {
        this.item = item;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public myBid(BidsPK bidsPK, Float amount, myItem item, User user) {
        this.bidsPK = bidsPK;
        this.amount = amount;
        this.item = item;
        this.user = user;
    }

    public myBid() {
    }
}
