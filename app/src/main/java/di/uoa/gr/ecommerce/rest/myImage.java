package di.uoa.gr.ecommerce.rest;

public class myImage {
    public myImage(Integer id, String image, Integer itemID) {
        this.id = id;
        this.image = image;
        this.itemID = itemID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    private Integer id;
    private String image;
    private Integer itemID;
}
