package xyz.nyroma.towny.main;

import xyz.nyroma.towny.citymanagement.CitiesCache;
import xyz.nyroma.towny.empiremanagement.EmpiresCache;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EmpiresCache.setup();

        List<SLocation> claims = Arrays.asList(
                new SLocation("world", 1, 1),
                new SLocation("world", 1, -1),
                new SLocation("world", -1, 1),
                new SLocation("world", -1, 1)
        );

        CitiesCache.setup(claims);
    }
}
