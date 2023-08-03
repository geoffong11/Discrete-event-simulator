class Server {
    private final int numInQ;
    private final double freeTime;
    private final int id;
    private static final int NEW = 1;

    Server(int numInQ, double freeTime, int id) {
        this.numInQ = numInQ;
        this.freeTime = freeTime;
        this.id = id;
    }

    boolean free(double time) {
        return this.freeTime <= time;
    }

    boolean isSelfCheckout() {
        return false;
    }

    double getTime() {
        return this.freeTime;
    }

    boolean needRest() {
        return true;
    }

    int getId() {
        return this.id;
    }

    Server rest(double restTime) {
        return new Server(this.numInQ,
                this.freeTime + restTime, this.id);
    }

    int sizeOfQ() {
        return this.numInQ;
    }

    Server nextCustomer(double endTime) {
        return new Server(this.numInQ - NEW, endTime, this.id);
    }

    Server newCustomer(double time) {
        return new Server(this.numInQ + NEW, time, this.id);
    }

    Server addToQ() {
        return new Server(this.numInQ + NEW, this.freeTime, this.id);
    }

    Server reduceQ() {
        return new Server(this.numInQ - NEW, this.freeTime, this.id);
    }


    @Override
    public String toString() {
        return "" + (this.id + 1);
    }

}
    




