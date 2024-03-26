package menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    List<String> items = new ArrayList<>();

    public Menu(List<String> items) {
        this.items = items;
    }

    public void showMenu() {
        System.out.println("     Welcome to my hotel. find and enter your selected option.    ");
        System.out.println("    ----------------------------------------------------------    ");

        for (String item : items) {
            System.out.println(item + "\n");
        }

        System.out.println("-------------------------Have a good day-------------------------");
    }

    public void printYourOption(){
        System.out.println("Please enter your option: ");
    }
}
