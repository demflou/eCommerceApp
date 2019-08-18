package di.uoa.gr.ecommerce.rest;

import java.util.HashSet;
import java.util.Set;

public class myCat {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<myItem> getItemCollection() {
        return itemCollection;
    }

    public myCat(String name) {
        this.name = name;
    }

    public void setItemCollection(Set<myItem> itemCollection) {
        this.itemCollection = itemCollection;
    }

    private Set<myItem> itemCollection= new HashSet<myItem>();

}
