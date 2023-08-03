import java.util.function.Supplier;

class ArrivalEvents implements Events {
    private final Customer customer;
    private final int qmax;
    private final int numOfServers;

    ArrivalEvents(Customer customer, int qmax, int numOfServers) {
        this.customer = customer;
        this.qmax = qmax;
        this.numOfServers = numOfServers;
    }

    @Override
    public int left() {
        return 0;
    }

    @Override
    public double getStart() {
        return this.customer.getArrive();
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
        return 0.0;
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


    private Pair<Integer, Boolean> findQ(ImList<Server> servers, double time) {
        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).free(time)) {
                return new Pair<Integer, Boolean>(i, true);
            }
        }

        if (servers.size() < numOfServers) {
            return new Pair<Integer, Boolean>(servers.size() + 1, true);
        }

        for (int j = 0; j < servers.size(); j++) {
            if (servers.get(j).sizeOfQ() < this.qmax &&
                    !servers.get(j).isSelfCheckout()) {
                return new Pair<Integer, Boolean>(j, false);
            }
        }

        for (int k = 0; k < servers.size(); k++) {
            if (servers.get(k).isSelfCheckout()) {
                if (servers.get(k).sizeOfQ() < this.qmax) {
                    return new Pair<Integer, Boolean>(k, false);
                }
                break;
            }
        }
        return new Pair<Integer, Boolean>(-1, false);
    }

    @Override
    public Pair<ImList<Server>, Events> process(ImList<Server> servers, 
            Supplier<Double> serviceTimes, Supplier<Double> restTimes) {

        Pair<Integer, Boolean> pr1 = findQ(servers, this.customer.getArrive());
        int qNum = pr1.first();
        Events event = this;
        if (qNum > servers.size()) {
            double timeEnd = this.customer.getArrive() + serviceTimes.get();
            servers = servers.add(new Server(0, timeEnd, qNum));
            event = this.newEvent(servers.get(qNum), 
                    false, true, timeEnd, qNum);
        } else if (pr1.first() == -1) {
            event = this.newEvent(new Server(0, 0.0, -1), true, false,
                    this.customer.getArrive(), qNum);
        } else {
            if (pr1.second()) {
                double timeEnd = this.customer.getArrive();
                event = this.newEvent(servers.get(qNum), false, true,
                        timeEnd, qNum);
                if (servers.get(qNum).isSelfCheckout()) {
                    servers = servers.set(numOfServers, 
                            servers.get(numOfServers).addToQ());
                }
                servers = servers.set(qNum, servers.get(qNum).newCustomer(timeEnd));
            } else {
                if (servers.get(qNum).isSelfCheckout()) {
                    event = this.newEvent(servers.get(qNum), true, true,
                            this.shortestWaitingTime(servers), qNum);
                } else {
                    event = this.newEvent(servers.get(qNum), true, true,
                            servers.get(qNum).getTime(), qNum);
                }
                servers = servers.set(qNum, servers.get(qNum).addToQ());
            }
        }
        return new Pair<ImList<Server>, Events>(servers, event);
    }

    private Events newEvent(Server server, boolean waiting, boolean havePlace, 
            double waitTime, int queue) {
        if (waiting) {
            if (havePlace) {
                return new WaitEvents(server, this.customer, waitTime, queue);
            } else {
                return new LeaveEvents(this.customer, this.customer.getArrive());
            }
        } else {
            return new ServeEvents(server, this.customer, waitTime, queue);
        }        
    }



    @Override
    public String toString() {
        return String.format("%.3f", this.customer.getArrive()) + 
            " " + customer.toString() + " arrives" + "\n";
    }


}
