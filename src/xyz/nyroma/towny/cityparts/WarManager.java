package xyz.nyroma.towny.cityparts;

import xyz.nyroma.towny.citymanagement.City;
import xyz.nyroma.towny.enums.WarState;
import xyz.nyroma.towny.enums.WarType;

public class WarManager {
    private City city;
    private boolean inWar = false;
    private City enemy;
    private WarType type;
    private int prime;
    private int deaths;
    private int deathNeeded;

    public WarManager(City city){
        this.city = city;
    }

    public WarState declareWar(City enemy, WarType warType, int prime){
        if(this.inWar){
            return WarState.ALREADY_IN_WAR;
        } else {
            if(city.getMoneyManager().getAmount() >= prime && enemy.getMoneyManager().getAmount() >= prime){
                city.getMoneyManager().removeMoney(prime);
                city.getWarManager().setupWar(warType, prime*2);
                enemy.getMoneyManager().removeMoney(prime);
                enemy.getWarManager().setupWar(warType, prime*2);
                return WarState.WAR_DECLARED;
            } else {
                return WarState.NOT_ENOUGH_MONEY;
            }
        }
    }

    private void setupWar(WarType warType, int prime){
        this.inWar = true;
        this.enemy = city;
        this.type = warType;
        this.prime = prime;
        switch(warType){
            case SOFT:
                this.deathNeeded = 10;
                break;
            case HARD:
                this.deathNeeded = 20;
                break;
        }
    }

    private void setupPeace(){
        this.inWar = false;
        this.enemy = null;
        this.type = null;
        this.deaths = 0;
        this.deathNeeded = 0;
        this.prime = 0;
    }

    public void declarePeace(City winner){
        if(this.isInWar()) {
            if (winner.equals(this.city)) {
                this.city.getMoneyManager().addMoney(this.prime);
            } else {
                this.city.getWarManager().getEnemy().getMoneyManager().addMoney(this.prime);
            }
            this.city.getWarManager().getEnemy().getWarManager().setupPeace();
            this.city.getWarManager().setupPeace();
        }
    }

    public City getEnemy() {
        return enemy;
    }

    public int getPrime() {
        return prime;
    }

    public WarType getType() {
        return type;
    }

    public int getDeaths() {
        return deaths;
    }

    public boolean isInWar(){
        return inWar;
    }
}
