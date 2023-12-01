package hw2;

import java.util.ArrayList;

public class Homework2 {
    private final ArrayList<Developer> developers = new ArrayList<>();

    public void startWork() {

        developers.add(new FrontEnder());
        developers.add(new BackEnder());
        developers.add(new FullStack());
        developers.add(new FrontEnder());

        for (Developer developer : developers) {
            if (developer instanceof FrontEnder) {
                developer.developGUI();
            }
        }
    }

    public static void main(String[] args) {
        new Homework2().startWork();
    }

}
