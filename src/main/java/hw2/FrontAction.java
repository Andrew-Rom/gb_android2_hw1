package hw2;
public interface FrontAction {
    void front();
    default void drinkCoffee() {
        System.out.println("Drink coffee");
    };
}
