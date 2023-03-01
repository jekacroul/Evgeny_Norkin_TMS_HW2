package HM_Order;

public class Order {
    private String name;
    private int quantity;
    private double cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "Наименование продукта = '" + name + '\'' +
                ", Количество = " + quantity +
                ", Цена = " + cost +
                '}'+ '\n';
    }
}
