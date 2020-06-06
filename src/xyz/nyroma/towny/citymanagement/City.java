package xyz.nyroma.towny.citymanagement;

import xyz.nyroma.towny.cityparts.*;
import xyz.nyroma.towny.enums.RelationStatus;

import java.io.Serializable;
import java.util.Random;

import static xyz.nyroma.towny.citymanagement.CityManager.log;

public class City implements Serializable {
    private String royaume;
    private String name;
    private String owner;
    private ClaimsManager claimsManager;
    private MembersManager membersManager;
    private MoneyManager moneyManager;
    private RelationsManager relationsManager;
    private long id;
    private boolean faillite = false;
    private WarManager warManager;


    public City(String name, String royaume, String owner) throws TownyException {
            if (new CityManager().isAlreadyOwner(owner)) {
                throw new TownyException("Tu es déjà l'owner d'une ville !");
            }
            if(CitiesCache.contains(name)){
                throw new TownyException("Cette ville existe déjà !");
            }
            if(name.equals("all")){
                throw new TownyException("Tu ne peux pas avoir ce nome de ville.");
            }

            long id = new Random().nextLong();
            while(CitiesCache.hasID(id)){
                id = new Random().nextLong();
            }

            this.id = id;
            this.name = name;
            this.royaume = royaume;
            this.owner = owner;
            this.claimsManager = new ClaimsManager();
            this.membersManager = new MembersManager();
            this.membersManager.addMember(owner);
            this.moneyManager = new MoneyManager();
            this.relationsManager = new RelationsManager();
            //this.warManager = new WarManager(this);

            CitiesCache.add(this);
            CitiesCache.addID(this.id);

            log("Ville " + this.name + " créée !");
    }

    public WarManager getWarManager() {
        return warManager;
    }

    public boolean getFaillite(){
        return this.faillite;
    }

    public void setFaillite(boolean faillite) {
        this.faillite = faillite;
    }

    public RelationsManager getRelationsManager() {
        return this.relationsManager;
    }

    public String getOwner(){
        return this.owner;
    }

    public String getRoyaume() {
        return this.royaume;
    }

    public String getName() {
        return this.name;
    }

    public long getID(){
        return this.id;
    }

    public ClaimsManager getClaimsManager() {
        return this.claimsManager;
    }

    public MembersManager getMembersManager() {
        return this.membersManager;
    }

    public MoneyManager getMoneyManager() {
        return this.moneyManager;
    }

    public City getCity(){
        return this;
    }

    public void rename(String name) {
        this.name = name;
    }

    public void changeOwner(String pseudo) {
        this.owner = pseudo;
    }

    private boolean isOwner(String pseudo) {
        return pseudo.equals(this.owner);
    }

    public boolean remove(String pseudo){
        if(isOwner(pseudo)){
            new CityManager().removeCity(this);
            return true;
        } else {
            return false;
        }
    }

    public RelationStatus getRelationStatus(String pseudo){
        if(getMembersManager().getMembers().contains(pseudo)){
            return RelationStatus.MEMBER;
        } else {
            if (getRelationsManager().getNice()) {
                return RelationStatus.ALLY;
            } else {
                if (getRelationsManager().getEvil()) {
                    return RelationStatus.ENEMY;
                } else {
                    if(!new CityManager().getCityOfMember(pseudo).isPresent()){
                        return RelationStatus.ENEMY;
                    } else {
                        City city = new CityManager().getCityOfMember(pseudo).get();
                        return getRelationsManager().getAllies().contains(city.getName()) ? RelationStatus.ALLY : RelationStatus.NEUTRAL;
                    }
                }
            }
        }
    }
}
