import java.util.function.Supplier;

class WaitEvents implements Events {
    private final Server server;
    private final Customer customer;
    private final double timeServing;
    private final int qNum;
    private final boolean waitingLonger;
    private static final int FIRST = 0;

    WaitEvents(Server server, Customer customer, double timeServing, int qNum) {
        this.customer = customer;
        this.timeServing = timeServing;
        this.qNum = qNum;
        this.waitingLonger = false;
        this.server = server;
    }

    WaitEvents(Server server, Customer customer, double timeServing, 
            int qNum, boolean waitingLonger) {
        this.server = server;
        this.customer = customer;
        this.timeServing = timeServing;
        this.qNum = qNum;
        this.waitingLonger = waitingLonger;
    }

    @Override
    public int left() {
        return 0;
    }



    @Override
    public double getStart() {
        if (this.waitingLonger) {
            return this.timeServing;
        }
        return this.FIRST;
    }

    @Override
    public boolean nextEvent() {
        return true;
    }

    @Override
    public Pair<ImList<Server>, Events> process(ImList<Server> servers, 
            Supplier<Double> serviceTimes, Supplier<Double> restTimes) {
        Events events = this;
        if (this.getStart() == 0) {
            events = this.newEvent(this.server, false, true, 
                    this.getStart(), this.qNum);
        } else if (servers.get(this.qNum).isSelfCheckout()) {
            int result = processingSelfCheckout(servers);
            if (result != -1) {
                double newEndTime = servers.get(result).getTime();
                events = newEvent(servers.get(result), false, 
                        false, newEndTime, result);
            } else {
                events = this.newEvent(this.server, true, false, 
                        this.shortestWaitingTime(servers), this.qNum);
            }
        } else {
            if (this.getStart() == 0) {
                events = this.newEvent(this.server, false, 
                        true, this.getStart(), this.qNum);
            } else if (servers.get(this.qNum).free(this.getStart())) {
                double newEndTime = servers.get(this.qNum).getTime();
                events = this.newEvent(this.server, false, false, 
                        newEndTime, this.qNum);
            } else {
                events = this.newEvent(this.server, true, false, 
                        servers.get(this.qNum).getTime(), this.qNum);
            }
        }
        return new Pair<ImList<Server>, Events>(servers, events);
    }

    private double shortestWaitingTime(ImList<Server> servers) {
        double min = 0;
        boolean first = true;
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isSelfCheckout() && first) {
                min = servers.get(i).getTime();
                first = false;
                continue;
            }
            if (servers.get(i).isSelfCheckout()) {
                if (servers.get(i).getTime() < min) {
                    min = servers.get(i).getTime();
                }
            }
        }
        return min;
    }

    private int processingSelfCheckout(ImList<Server> servers) {
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).isSelfCheckout()) {
                if (servers.get(i).free(this.getStart())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private Events newEvent(Server servers, boolean waitMore, 
            boolean justPr, double newTime, int id) {
        if (justPr) {
            return new WaitEvents(this.server, this.customer, this.timeServing, this.qNum, true);
        }
        if (waitMore) {
            return new WaitEvents(this.server, this.customer, newTime, this.qNum, true);
        } else {
            return new ServeEvents(servers, this.customer, newTime, id);
        }
    }

    @Override
    public int customerNum() {
        return this.customer.getId(); 
    }

    @Override
    public double getWaitingTime() {
        return 0.0;
    }

    @Override
    public String toString() {
        if (this.waitingLonger) {
            return "";
        }
        return String.format("%.3f", this.customer.getArrive()) + " " + 
            this.customer.toString() + " waits at " + 
            this.server.toString() + "\n";
    }

}

