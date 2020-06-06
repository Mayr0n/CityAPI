package xyz.nyroma.towny.cityparts;

import xyz.nyroma.towny.citymanagement.City;

import java.io.Serializable;
import java.util.ArrayList;

public class RelationsManager implements Serializable {
    private ArrayList<String> allies = new ArrayList<>();
    private ArrayList<String> enemies = new ArrayList<>();
    private boolean allAllies = false;
    private boolean allEnemies = false;

    public boolean addAlly(City city){
        this.setNice(false);
        this.setEvil(false);
        getEnemies().remove(city.getName());
        if(getAllies().contains(city.getName())){
            return false;
        } else {
            return getAllies().add(city.getName());
        }
    }

    public boolean getNice(){
        return this.allAllies;
    }

    public boolean getEvil(){
        return this.allEnemies;
    }

    public void setNice(boolean b){
        if(b){
            getEnemies().clear();
            getAllies().clear();
            this.allEnemies = false;
        }
        this.allAllies = b;
    }

    public void setEvil(boolean b){
        if(b){
            getEnemies().clear();
            getAllies().clear();
            this.allAllies = false;
        }
        this.allEnemies = b;
    }

    public boolean removeAlly(City city){
        if(getAllies().contains(city.getName())){
            return getAllies().remove(city.getName());
        } else {
            return false;
        }
    }

    public ArrayList<String> getAllies(){
        return this.allies;
    }

    public boolean addEnemy(City city){
        this.setNice(false);
        this.setEvil(false);
        getAllies().remove(city.getName());
        if(getEnemies().contains(city.getName())){
            return false;
        } else {
            return getEnemies().add(city.getName());
        }
    }

    public boolean removeEnemy(City city){
        System.out.println(getEnemies());
        if(getEnemies().contains(city.getName())){
            boolean b = getEnemies().remove(city.getName());
            System.out.println(b);
            return b;
        } else {
            return false;
        }
    }

    public ArrayList<String> getEnemies(){
        return this.enemies;
    }

}
