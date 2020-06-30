package xyz.nyroma.towny.citymanagement;

import xyz.nyroma.towny.main.SLocation;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class CityUtils {
    private static boolean logger = true;

    public static void log(String txt) {
        if (logger) {
            System.out.println(txt);
        }
    }

    public static boolean isAlreadyOwner(String name) {
        for (City city : CitiesCache.getCities()) {
            if (city.getOwner().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeCity(City city) {
        File cityFile = new File("data/towny/" + "cities/" + city.getID() + ".json");
        cityFile.delete();
        return CitiesCache.remove(city);
    }

    public static boolean isMemberOfACity(String pseudo) {
        for (City city : CitiesCache.getCities()) {
            for (String member : city.getMembers()) {
                if (member.equals(pseudo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Optional<City> getCityOfMember(String pseudo) {
        for (City city : CitiesCache.getCities()) {
            if (city.getMembers().contains(pseudo)) {
                return Optional.of(city);
            }
        }
        return Optional.empty();
    }


    public static boolean isAOwner(String pseudo) {
        return getOwnersCity(pseudo).isPresent();
    }

    public static Optional<City> getOwnersCity(String pseudo) {
        List<City> cities = CitiesCache.getCities();
        for (City city : cities) {
            if (city.getOwner().equals(pseudo)) {
                return Optional.of(city);
            }
        }
        return Optional.empty();
    }

    public static Optional<City> getClaimer(String world, int X, int Z) {
        SLocation chunk = new SLocation(world, X, Z);
        for (City city : CitiesCache.getCities()) {
            if (city.getClaims().contains(chunk)) {
                return Optional.of(city);
            }
        }
        return Optional.empty();
    }
}
