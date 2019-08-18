package di.uoa.gr.ecommerce.rest;

import java.io.ByteArrayInputStream;

public class myImage {
    public myImage(Integer id, ByteArrayInputStream[] image, Integer itemID) {
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

    public ByteArrayInputStream[] getImage() {
        return image;
    }

    public void setImage(ByteArrayInputStream[] image) {
        this.image = image;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    private Integer id;
    private ByteArrayInputStream[] image;
    private Integer itemID;
}
