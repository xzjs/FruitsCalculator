package love.xzjs.fruitscalculator;

/**
 * Created by xzjs on 2017/12/12.
 */

public class Fruit {
    private int id;
    private String name;
    private double price;
    private String img;

    public Fruit(int id, String name, double price, String img) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public String getImg() {
        return img;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
