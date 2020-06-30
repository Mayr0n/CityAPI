package xyz.nyroma.towny.citymanagement;

import xyz.nyroma.towny.empiremanagement.Empire;
import xyz.nyroma.towny.empiremanagement.EmpiresCache;
import xyz.nyroma.towny.enums.ClaimState;
import xyz.nyroma.towny.enums.RelationStatus;
import xyz.nyroma.towny.enums.TaxesState;
import xyz.nyroma.towny.main.SLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static xyz.nyroma.towny.citymanagement.CityUtils.log;

public class City implements Serializable {
    private List<String> members = new ArrayList<>();
    private List<SLocation> claims = new ArrayList<>();
    private ArrayList<String> allies = new ArrayList<>();
    private ArrayList<String> enemies = new ArrayList<>();
    private Empire empire;
    private City warOpponent = null;
    private String name;
    private String owner;
    private boolean faillite = false;
    private boolean inWar = false;
    private boolean allAllies = false;
    private boolean allEnemies = false;
    private int maxClaims = 5;
    private float bankAmount = 0;
    private float taxes = 5;
    private long id;

    public City(String name, String owner) throws TownyException {
        if (this.checkName(name)) {
            long id = new Random().nextLong();
            while (CitiesCache.hasID(id)) {
                id = new Random().nextLong();
            }

            this.id = id;
            this.name = name;
            if (EmpiresCache.get("DiscUniverse").isPresent()) {
                this.empire = EmpiresCache.get("DiscUniverse").get();
            } else {
                this.empire = null;
            }
            this.owner = owner;
            this.members.add(owner);
            this.empire = EmpiresCache.get("DiscUniverse").isPresent() ? EmpiresCache.get("DiscUniverse").get() : null;
            //this.warManager = new WarManager(this);

            CitiesCache.add(this);
            CitiesCache.addID(this.id);

            log("Ville " + this.name + " créée !");
        } else {
            throw new TownyException("This name can't be used.");
        }
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

    public ClaimState addClaim(String world, float x, float z) {
        if (this.claims.size() < this.maxClaims) {
            SLocation sloc = new SLocation(world, x, z);
            if (!this.claims.contains(sloc)) {
                this.claims.add(sloc);
                return ClaimState.CLAIMED;
            } else {
                return ClaimState.ALREADY_CLAIM;
            }
        } else {
            return ClaimState.MAX_CLAIM;
        }
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

    public void addMoney(float amount) {
        this.bankAmount += amount;
    }

    public boolean removeAlly(City city) {
        if (this.getAllies().contains(city.getName())) {
            return this.getAllies().remove(city.getName());
        } else {
            return false;
        }
    }

    public ClaimState removeClaim(String world, float x, float z) {
        SLocation sloc = new SLocation(world, x, z);
        if (this.claims.contains(sloc)) {
            this.claims.remove(sloc);
            return ClaimState.UNCLAIMED;
        } else {
            return ClaimState.NOT_CLAIMED;
        }
    }

    public boolean removeMoney(float amount) {
        if (this.getBankAmount() - amount >= 0) {
            this.bankAmount -= amount;
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getAllies() {
        return this.allies;
    }

    public float getBankAmount() {
        return this.bankAmount;
    }

    public float getTaxes() {
        return this.taxes;
    }

    public void setTaxes(float taxes) {
        this.taxes = taxes;
    }

    public int getMaxClaims() {
        return this.maxClaims;
    }

    public void setMaxClaims(int maxClaims) {
        this.maxClaims = maxClaims;
    }

    public List<SLocation> getClaims() {
        return this.claims;
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

    public boolean addMember(String pseudo) {
        if (!this.members.contains(pseudo)) {
            if (!CityUtils.isMemberOfACity(pseudo)) {
                return this.members.add(pseudo);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkName(String name) {
        return (!(CityUtils.isAlreadyOwner(this.owner) || CitiesCache.contains(name) || name.equals("all")));
    }

    public List<String> getMembers() {
        return this.members;
    }

    public TaxesState applyTaxes() {
        float taxes = this.getTaxes();
        if (this.removeMoney(taxes)) {
            this.setFaillite(false);
            log(this.getName() + " a été débité de " + taxes + " Nyr.");
            return TaxesState.PAYED;
        } else {
            if (this.getFaillite()) {
                CityUtils.removeCity(this);
                log(this.getName() + " a été supprimée.");
                return TaxesState.REMOVED;
            } else {
                this.setFaillite(true);
                log(this.getName() + " est passée en faillite.");
                return TaxesState.BROKEN;
            }
        }
    }

    public boolean getFaillite() {
        return this.faillite;
    }

    public void setFaillite(boolean faillite) {
        this.faillite = faillite;
    }

    public String getOwner() {
        return this.owner;
    }

    public Empire getEmpire() {
        return this.empire;
    }

    public String getName() {
        return this.name;
    }

    public long getID() {
        return this.id;
    }

    public City getCity() {
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

    public boolean remove(String pseudo) {
        if (this.isOwner(pseudo)) {
            CityUtils.removeCity(this);
            return true;
        } else {
            return false;
        }
    }

    public RelationStatus getRelationStatus(String pseudo) {
        if (this.getMembers().contains(pseudo)) {
            return RelationStatus.MEMBER;
        } else {
            if (this.getNice()) {
                return RelationStatus.ALLY;
            } else {
                if (this.getEvil()) {
                    return RelationStatus.ENEMY;
                } else {
                    if (!CityUtils.getCityOfMember(pseudo).isPresent()) {
                        return RelationStatus.ENEMY;
                    } else {
                        City city = CityUtils.getCityOfMember(pseudo).get();
                        return this.getAllies().contains(city.getName()) ? RelationStatus.ALLY : RelationStatus.NEUTRAL;
                    }
                }
            }
        }
    }
}
