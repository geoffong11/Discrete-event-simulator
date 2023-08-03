import java.util.function.Supplier;

class LeaveEvents implements Events {
    private final Customer customer;
    private final double arrivalTime;

    LeaveEvents(Customer customer, double arrivalTime) {
        this.customer = customer;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public double getWaitingTime() {
        return 0.0;
    }

    @Override
    public double getStart() {
        return 0.0;
    }

    @Override
    public int customerNum() {
        return this.customer.getId();
    }

    @Override
    public int left() {
        return 1;
    }

    @Override
    public Pair<ImList<Server>, Events> process(ImList<Server> servers, 
            Supplier<Double> serviceTimes, Supplier<Double> restTimes) {
        return new Pair<ImList<Server>, Events>(servers, this);
    }


    @Override
    public boolean nextEvent() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.customer.getArrive()) + " " +
            customer.toString() + " leaves" + "\n";
    }

}
