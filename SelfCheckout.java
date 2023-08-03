class SelfCheckout extends Server {
    SelfCheckout(int qNum, double freeTime, int id) {
        super(qNum, freeTime, id);
    }

    @Override
    public boolean needRest() {
        return false;
    }

    @Override
    public boolean isSelfCheckout() {
        return true;
    }

    @Override
    public Server nextCustomer(double endTime) {
        return new SelfCheckout(super.sizeOfQ(), endTime, super.getId());
    }

    @Override
    public Server newCustomer(double time) {
        return new SelfCheckout(super.sizeOfQ(), time, super.getId());
    }

    @Override
    public Server addToQ() {
        return new SelfCheckout(super.sizeOfQ() + 1, super.getTime(), super.getId());

    }

    @Override
    public Server reduceQ() {
        return new SelfCheckout(super.sizeOfQ() - 1, super.getTime(), super.getId());
    }

    @Override
    public String toString() {
        return "self-check " + (super.getId() + 1);
    }
}
