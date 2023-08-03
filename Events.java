import java.util.function.Supplier;

interface Events {

    int customerNum();

    int left();

    double getStart();

    double getWaitingTime();

    boolean nextEvent();

    Pair<ImList<Server>, Events> process(ImList<Server> servers,
            Supplier<Double> serviceTimes, Supplier<Double> restTimes);
}
