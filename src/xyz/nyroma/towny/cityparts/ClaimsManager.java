package xyz.nyroma.towny.cityparts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import static xyz.nyroma.towny.citymanagement.CityManager.log;

public class ClaimsManager implements Serializable {
    private Hashtable<String, Hashtable<Integer, ArrayList<Integer>>> claims = new Hashtable<>(); //{world, {x=[z1,z2]}}
    private int max = 5;

    public int getAmount(){
        int amount = 0;
        for(String world : claims.keySet()){
            for(ArrayList<Integer> y : this.claims.get(world).values()){
                amount+= y.size();
            }
        }
        log("Nombre de claims : " + amount);
        return amount;
    }

    public int getMax() {
        return max;
    }

    public boolean addClaim(String world, int X, int Z) {
        if(getAmount() < this.getMax()){
            if(contains(world, X, Z)){
                return false;
            } else {
                ArrayList<Integer> z = new ArrayList<>();
                z.add(Z);
                if(getClaims().containsKey(world)){
                    if(getClaims().get(world).containsKey(X)){
                        return getClaims().get(world).get(X).add(Z);
                    } else {
                        getClaims().get(world).put(X, z);
                        return true;
                    }
                } else {
                    Hashtable<Integer, ArrayList<Integer>> hash = new Hashtable<>();
                    hash.put(X, z);
                    getClaims().put(world, hash);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contains(String world, int X, int Z){
        if(getClaims().keySet().contains(world)){
            if(getClaims().get(world).keySet().contains(X)){
                return getClaims().get(world).get(X).contains(Z);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setMax(int max){
        this.max = max;
    }

    public boolean removeClaim(String world, int X, int Z){
        if(getClaims().keySet().contains(world)){
            if(getClaims().get(world).keySet().contains(X)){
                if(getClaims().get(world).get(X).contains(Z)){
                    return getClaims().get(world).get(X).remove(Integer.valueOf(Z));
                }
            }
        }
        return false;
    }

    public Hashtable<String, Hashtable<Integer, ArrayList<Integer>>> getClaims(){
        return this.claims;
    }
}
