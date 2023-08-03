import java.util.function.Supplier;

class ServeEvents implements Events {
    private final Customer customer;
    private final double timeServe;
    private final int qNum;
    private final Server server;

    ServeEvents(Server server, Customer customer, double timeServe, int qNum) {
        this.customer = customer;
        this.timeServe = timeServe;
        this.qNum = qNum;
        this.server = server;
    }

    @Override
    public double getStart() {
        return 0.0;
    }

    @Override
    public int left() {
        return 0;
    }

    @Override
    public int customerNum() {
        return this.customer.getId();
    }

    @Override
    public boolean nextEvent() {
        return true;
    }

    @Override
    public double getWaitingTime() {
        return timeServe - this.customer.getArrive();
    }

    private int findingMainQ(ImList<Server> servers) {
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isSelfCheckout()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Pair<ImList<Server>, Events> process(ImList<Server> servers, 
            Supplier<Double> serviceTimes, Supplier<Double> restTimes) {
        double newEndTime = servers.get(this.qNum).getTime() + serviceTimes.get();
        
        if (servers.get(qNum).isSelfCheckout()) {
            servers = servers.set(this.findingMainQ(servers), 
                    servers.get(this.findingMainQ(servers)).reduceQ());
        }
        
        servers = servers.set(this.qNum, servers.get(this.qNum)
                .nextCustomer(newEndTime));
        return new Pair<ImList<Server>, Events>(servers, this.newEvent(newEndTime));
    }

    private Events newEvent(double newEndTime) {
        return new DoneEvents(this.server, this.customer, newEndTime, this.qNum);
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.timeServe) + " " + 
            this.customer.toString() + " serves by " +
            this.server.toString() + "\n";
    }

}
