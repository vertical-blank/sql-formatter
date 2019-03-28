package vblank.sql_formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String... args) {
        List<String> input = new ArrayList<>();
        while (true) {
            Scanner in = new Scanner(System.in);
            if (!in.hasNext()) {
                break;
            }
            String str = in.nextLine();
            input.add(str);
        }

        String formatted = SqlFormatter.format(String.join("", input));
        System.out.println(formatted);
    }

}
