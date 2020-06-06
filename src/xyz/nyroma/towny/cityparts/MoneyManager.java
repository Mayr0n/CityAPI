package xyz.nyroma.towny.cityparts;

import java.io.Serializable;

public class MoneyManager implements Serializable {
    private float amount = 0;
    private float taxes = 5;

    public void setTaxes(float taxes){
        this.taxes = taxes;
    }

    public float getTaxes(){
        return this.taxes;
    }

    public boolean removeMoney(float amount){
        if(getAmount() - amount >= 0){
            this.amount -= amount;
            return true;
        } else {
            return false;
        }
    }

    public void addMoney(float amount){
        this.amount += amount;
    }

    public float getAmount(){
        return this.amount;
    }
}
