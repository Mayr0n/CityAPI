package xyz.nyroma.towny.empiremanagement;

import xyz.nyroma.towny.citymanagement.City;

import java.util.ArrayList;
import java.util.List;

public class Empire {

    private String name;
    private long id;
    private List<City> cities = new ArrayList<>();
    private float bankAmount = 0;
    private float taxes = 5;
    private ArrayList<String> allies = new ArrayList<>();
    private ArrayList<String> enemies = new ArrayList<>();
    private boolean allAllies = false;
    private boolean allEnemies = false;

    public Empire(String name, City mainCity) {
        this.name = name;
    }

    public boolean addAlly(City city) {
        this.setNice(false);
        this.setEvil(false);
        this.getEnemies().remove(city.getName());
        if (this.getAllies().contains(city.getName())) {
            return false;
        } else {
            return this.getAllies().add(city.getName());
        }
    }

    public boolean getNice() {
        return this.allAllies;
    }

    public void setNice(boolean b) {
        if (b) {
            this.getEnemies().clear();
            this.getAllies().clear();
            this.allEnemies = false;
        }
        this.allAllies = b;
    }

    public boolean getEvil() {
        return this.allEnemies;
    }

    public void setEvil(boolean b) {
        if (b) {
            this.getEnemies().clear();
            this.getAllies().clear();
            this.allAllies = false;
        }
        this.allEnemies = b;
    }

    public boolean removeAlly(City city) {
        if (this.getAllies().contains(city.getName())) {
            return this.getAllies().remove(city.getName());
        } else {
            return false;
        }
    }

    public ArrayList<String> getAllies() {
        return this.allies;
    }

    public boolean addEnemy(City city) {
        this.setNice(false);
        this.setEvil(false);
        this.getAllies().remove(city.getName());
        if (this.getEnemies().contains(city.getName())) {
            return false;
        } else {
            return this.getEnemies().add(city.getName());
        }
    }

    public boolean removeEnemy(City city) {
        System.out.println(this.getEnemies());
        if (this.getEnemies().contains(city.getName())) {
            boolean b = this.getEnemies().remove(city.getName());
            System.out.println(b);
            return b;
        } else {
            return false;
        }
    }

    public ArrayList<String> getEnemies() {
        return this.enemies;
    }

    public float getTaxes() {
        return this.taxes;
    }

    public void setTaxes(float taxes) {
        this.taxes = taxes;
    }

    public boolean removeMoney(float amount) {
        if (this.getAmount() - amount >= 0) {
            this.bankAmount -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void addMoney(float amount) {
        this.bankAmount += amount;
    }

    public float getAmount() {
        return this.bankAmount;
    }

    public String getName() {
        return this.name;
    }

    public List<City> getCities() {
        return this.cities;
    }

    public long getID() {
        return this.id;
    }
}
