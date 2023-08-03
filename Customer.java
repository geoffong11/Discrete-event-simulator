import java.util.function.Supplier;


class Customer {
    private final double enterTime;
    private final int id;


    Customer(double enterTime, int id) {
        this.enterTime = enterTime;
        this.id = id;
    }

    double getArrive() {
        return this.enterTime;
    }

    @Override
    public String toString() {
        return "" + this.id;
    }

    int getId() {
        return this.id;
    }

}

