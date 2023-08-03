import java.util.function.Supplier;
import java.util.Random;

class Simulator {
    private final int numOfServers;
    private final int numOfSelfChecks;
    private final int qmax;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTimes;
    private final Supplier<Double> restTimes;

    Simulator(int numOfServers, int numOfSelfChecks, int qmax, 
            ImList<Double> arrivalTimes,
            Supplier<Double> serviceTimes,
            Supplier<Double> restTimes) {
        this.numOfServers = numOfServers;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qmax = qmax;
        this.arrivalTimes = arrivalTimes;
        this.serviceTimes = serviceTimes;
        this.restTimes = restTimes;
    }

    String simulate() {
        ImList<Server> servers = new ImList<Server>();
        for (int j = 0; j < numOfServers; j++) {
            servers = servers.add(new Server(0, 0.0, j));
        }
        for (int k = 0; k < numOfSelfChecks; k++) {
            servers = servers.add(new SelfCheckout(0, 0.0, k + numOfServers));
        }
        PQ<Events> events = new PQ<Events>(new EndComp());
        String output = "";
        double waitingTime = 0.0;
        int left = 0;
        for (int i = 0; i < arrivalTimes.size(); i++) {
            Customer customer = new Customer(arrivalTimes.get(i),
                    i + 1);
            Events newArrival = new ArrivalEvents(customer, this.qmax, this.numOfServers);
            events = events.add(newArrival);
        }

        while (!events.isEmpty()) {
            Pair<Events, PQ<Events>> pr = events.poll();
            Events event = pr.first();
            events = pr.second();
            Pair<ImList<Server>, Events> pr1 = event.process(servers,
                    this.serviceTimes, this.restTimes);
            if (event.nextEvent()) {
                events = events.add(pr1.second());
            }
            servers = pr1.first();
            waitingTime = waitingTime + event.getWaitingTime();
            left = left + event.left();
            output = output + event.toString();
        }
        output = output + "[" + 
            String.format("%.3f", waitingTime / (arrivalTimes.size() - left)) + 
            " " + (arrivalTimes.size() - left) + " " + left + "]";
        return output;
    }
}
