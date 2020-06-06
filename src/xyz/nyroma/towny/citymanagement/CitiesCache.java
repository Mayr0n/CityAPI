package xyz.nyroma.towny.citymanagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import static xyz.nyroma.towny.citymanagement.CityManager.log;

public class CitiesCache {
    private static List<City> cities = new ArrayList<>();
    private static List<Long> ids = new ArrayList<>();

    public static boolean contains(String name) {
        if(name.equals("The State")){
            return false;
        } else {
            return CitiesCache.get(name).isPresent();
        }
    }

    public static boolean hasID(long id) {
        return ids.contains(id);
    }

    public static void setup(Hashtable<String, Hashtable<Integer, ArrayList<Integer>>> claims) {
        File mainFolder = new File("data/towny/");
        File citiesFolder = new File("data/towny/cities/");
        if (!mainFolder.exists()) {
            mainFolder.mkdirs();
        }
        if (!citiesFolder.exists()) {
            citiesFolder.mkdirs();
        }

        log("Chargement des villes...");

        cities.addAll(CitiesCache.getCitiesFromFile());

        initializeState(claims);

        log("Villes chargées !");
    }

    public static void shutdown(){
        serializeAll();
        cities = new ArrayList<>();
    }

    public static void initializeState(Hashtable<String, Hashtable<Integer, ArrayList<Integer>>> claims) {
        City city = null;

        if (CitiesCache.get("The State").isPresent()) {
            city = CitiesCache.get("The State").get();
        } else {
            try {
                city = new City("The State", "DiscUniverse", "Xénée");
            } catch (TownyException e) {
                e.printStackTrace();
            }
        }

        if (city != null) {
            city.getMoneyManager().setTaxes(0);
            city.getClaimsManager().setMax(99999);
            city.getRelationsManager().setNice(false);
            city.getRelationsManager().setEvil(false);
            city.getMoneyManager().removeMoney(city.getMoneyManager().getAmount());
            city.getMoneyManager().addMoney(1000000000);

            if (CitiesCache.get("Warriors").isPresent()) {
                city.getRelationsManager().addAlly(CitiesCache.get("Warriors").get());
            }

            for (String s : claims.keySet()) {
                for (int X : claims.get(s).keySet()) {
                    for(int Z : claims.get(s).get(X)){
                        city.getClaimsManager().addClaim(s, X, claims.get(s).get(X).get(Z));
                    }
                }
            }
        }
    }

    public static Optional<City> get(String name) {
        for (City city : cities) {
            if (city.getName().equals(name)) return Optional.of(city);
        }
        return Optional.empty();
    }

    public static boolean remove(City city) {
        return cities.remove(city);
    }

    public static List<City> getCities() {
        return cities;
    }

    public static void add(City city) {
        cities.add(city);
    }

    public static void addID(long id) {
        ids.add(id);
    }

    public static void serializeAll() {
        log("Enregistrement des villes...");
        for (City city : cities) {
            try {
                serializeCity(city);
                backupCity(city);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log("Villes enregistrées !");
    }

    private static void backupCity(City city) throws IOException {
        writeInJson(new File("data/towny/" + "cities/" + city.getID() + "_backup.json"), city);
    }

    public static void serializeCity(City city) throws IOException {
        writeInJson(new File("data/towny/" + "cities/" + city.getID() + ".json"), city);
    }

    private static void writeInJson(File file, City city) throws IOException {
        if(file.delete()) {
            file.createNewFile();
        }

        GsonBuilder builder = new GsonBuilder();
        Gson g = builder.setPrettyPrinting().create();
        String json = g.toJson(city);
        FileWriter fw = new FileWriter(file);
        fw.write(json);
        fw.close();
    }

    public static List<City> getCitiesFromFile() {
        File citiesFolder = new File("data/towny/" + "cities/");
        List<City> cities = new ArrayList<>();
        File[] files = citiesFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if(file.getName().split("\\.").length == 2){
                    String name = file.getName().split("\\.")[0];
                    String extens = file.getName().split("\\.")[1];
                    if (extens.equals("json")) {
                        if (!name.contains("backup")) {
                            if (getCityFromFile(file).isPresent()) {
                                City city = getCityFromFile(file).get();
                                cities.add(city);
                                log("Ville \"" + city.getName() + "\"ajoutée.");
                            } else {
                                log("La ville n'a pas pu être ajoutée.");
                            }
                        } else {
                            System.out.println(file.getName() + ", backup détectée.");
                        }
                    } else {
                        System.out.println(file.getName() + " est un fichier inutile.");
                    }
                } else {
                    System.out.println(file.getName() + " est un fichier inutile.");
                }
            }
        } else {
            return new ArrayList<>();
        }
        return cities;
    }

    public static Optional<City> getCityFromFile(File file) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        log(file.getName());
        try {
            City city = gson.fromJson(new FileReader(file), City.class);
            if(city != null){
                return Optional.of(city);
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
