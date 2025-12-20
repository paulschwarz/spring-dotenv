package me.paulschwarz.springdotenv.testkit;

import java.util.List;

public class ListUtils {

    public static int firstIndexContaining(List<String> options, String option) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).contains(option)) {
                return i;
            }
        }

        return -1;
    }
}
