package di.uoa.gr.ecommerce.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class myItem {
    private Integer id;
    private String name;
    private Float currentPrice;
    private Float firstBid;
    private Integer numofbids;
    private String location;
    private String country;
    private Date startDate;
    private Date endDate;
    private String description;
    private Collection<myBid> bidsCollection;

    public Collection<myBid> getBidsCollection() {
        return bidsCollection;
    }

    public void setBidsCollection(Collection<myBid> bidsCollection) {
        this.bidsCollection = bidsCollection;
    }

    public Set<myCat> getCategoryCollection() {
        return categoryCollection;
    }

    public void setCategoryCollection(Set<myCat> categoryCollection) {
        this.categoryCollection = categoryCollection;
    }

    private Set<myCat> categoryCollection = new HashSet<myCat>();
    private User sellerID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Float getFirstBid() {
        return firstBid;
    }

    public void setFirstBid(Float firstBid) {
        this.firstBid = firstBid;
    }

    public Integer getNumofbids() {
        return numofbids;
    }

    public void setNumofbids(Integer numofbids) {
        this.numofbids = numofbids;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getSellerID() {
        return sellerID;
    }

    public void setSellerID(User sellerID) {
        this.sellerID = sellerID;
    }

    public Collection<myImage> getImagesCollection() {
        return imagesCollection;
    }

    public void setImagesCollection(Collection<myImage> imagesCollection) {
        this.imagesCollection = imagesCollection;
    }

    private Collection<myImage> imagesCollection;
}
