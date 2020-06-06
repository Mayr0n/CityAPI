package xyz.nyroma.towny.empiremanagement;

import xyz.nyroma.towny.citymanagement.City;
import xyz.nyroma.towny.cityparts.MoneyManager;

import java.util.ArrayList;
import java.util.List;

public class Empire {

    private String name;
    private long id;
    private List<City> cities = new ArrayList<>();
    private MoneyManager moneyManager;

}
