package ObjectModel;

public class Product {
    private String name;
    private Data data;

    public Product() {
    }

    public Product(String name, Data data){
        this.name = name;
        this.data = data;
    }

    public Product(String name){
        this.name = name;
    }

    public Product(Data data){
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
