import java.util.Comparator;

class EndComp implements Comparator<Events> {
    public int compare(Events e1, Events e2) {
        if (e1.getStart() > e2.getStart()) {
            return 1;
        } else if (e1.getStart() < e2.getStart()) {
            return -1;
        } else {
            if (e1.customerNum() > e2.customerNum()) {
                return 1;
            } else if (e1.customerNum() < e2.customerNum()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
