package love.xzjs.fruitscalculator;

/**
 * Created by Administrator on 2017/12/14.
 */

public class Account {
    public Fruit getFruit() {
        return fruit;
    }

    private Fruit fruit;
    private int num;

    public Account(Fruit fruit, int num) {
        this.fruit = fruit;
        this.num = num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getTotal() {
        return fruit.getPrice() * num;
    }
}
