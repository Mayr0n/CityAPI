package xyz.nyroma.towny.empiremanagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.nyroma.towny.citymanagement.CitiesCache;
import xyz.nyroma.towny.citymanagement.CityUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpiresCache {
    private static List<Empire> empires = new ArrayList<>();
    private static List<Long> ids = new ArrayList<>();
    private File empiresFolder = new File("data/towny/empires/");

    public static boolean contains(String name) {
        if (name.equals("DiscUniverse")) {
            return false;
        } else {
            return EmpiresCache.get(name).isPresent();
        }
    }

    public static boolean hasID(long id) {
        return ids.contains(id);
    }

    public static void setup() {
        File mainFolder = new File("data/towny/");
        File empiresFolder = new File("data/towny/empires/");
        if (!mainFolder.exists()) {
            mainFolder.mkdirs();
        }
        if (!empiresFolder.exists()) {
            empiresFolder.mkdirs();
        }

        CityUtils.log("Chargement des villes...");

        empires.addAll(EmpiresCache.getEmpiresFromFile());

        initialize();

        CityUtils.log("Villes chargées !");
    }

    public static void shutdown() {
        serializeAll();
        empires = new ArrayList<>();
    }

    public static void initialize() {
        if (!EmpiresCache.get("DiscUniverse").isPresent()) {
            if (CitiesCache.get("The State").isPresent()) {
                new Empire("DiscUniverse", CitiesCache.get("The State").get());
            }
        }
    }

    public static Optional<Empire> get(String name) {
        for (Empire empire : empires) {
            if (empire.getName().equals(name)) return Optional.of(empire);
        }
        return Optional.empty();
    }

    public static List<Empire> getEmpires() {
        return empires;
    }

    public static void add(Empire empire) {
        empires.add(empire);
        ids.add(empire.getID());
    }

    public static void serializeAll() {
        CityUtils.log("Enregistrement des villes...");
        for (Empire empire : empires) {
            serializeEmpire(empire);
            backupEmpire(empire);
        }
        CityUtils.log("Villes enregistrées !");
    }

    private static void backupEmpire(Empire empire) {
        writeInJson(new File("data/towny/" + "cities/" + empire.getID() + "_backup.json"), empire);
    }

    public static void serializeEmpire(Empire empire) {
        writeInJson(new File("data/towny/" + "cities/" + empire.getID() + ".json"), empire);
    }

    private static void writeInJson(File file, Empire empire) {
        try {
            if (file.delete()) {
                file.createNewFile();
            }

            GsonBuilder builder = new GsonBuilder();
            Gson g = builder.setPrettyPrinting().create();
            String json = g.toJson(empire);
            FileWriter fw = new FileWriter(file);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Empire> getEmpiresFromFile() {
        File empiresFolder = new File("data/towny/" + "empires/");
        List<Empire> empires = new ArrayList<>();
        File[] files = empiresFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().split("\\.").length == 2) {
                    String name = file.getName().split("\\.")[0];
                    String extens = file.getName().split("\\.")[1];
                    if (extens.equals("json")) {
                        if (!name.contains("backup")) {
                            if (getEmpireFromFile(file).isPresent()) {
                                Empire empire = getEmpireFromFile(file).get();
                                empires.add(empire);
                                CityUtils.log("Ville \"" + empire.getName() + "\"ajoutée.");
                            } else {
                                CityUtils.log("La ville n'a pas pu être ajoutée.");
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
        return empires;
    }

    public static Optional<Empire> getEmpireFromFile(File file) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        CityUtils.log(file.getName());
        try {
            Empire empire = gson.fromJson(new FileReader(file), Empire.class);
            if (empire != null) {
                return Optional.of(empire);
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
