import java.util.function.Supplier;

class DoneEvents implements Events {
    private final Customer customer;
    private final double timeDone;
    private final int qNum;
    private final Server server;

    DoneEvents(Server server, Customer customer, 
            double timeDone, int qNum) {
        this.customer = customer;
        this.timeDone = timeDone;
        this.qNum = qNum;
        this.server = server;
    }


    @Override
    public double getStart() {
        return this.timeDone;
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
        return false;
    }

    @Override
    public Pair<ImList<Server>, Events> process(ImList<Server> servers, 
            Supplier<Double> serviceTimes, Supplier<Double> restTimes) {
        if (servers.get(this.qNum).needRest()) {
            servers = servers.set(this.qNum, 
                    servers.get(this.qNum).rest(restTimes.get()));
        }
        return new Pair<ImList<Server>, Events>(servers, this);
    }

    @Override
    public double getWaitingTime() {
        return 0.0;
    }

    @Override
    public String toString() {
        return String.format("%.3f", this.timeDone) + " " + this.customer.getId() + 
            " done serving by " + this.server.toString() + "\n"; 
    }
}
