package xyz.nyroma.towny.enums;

public enum SubCommands {
    MONEY_ADD("add"),
    MONEY_REMOVE("remove"),
    MEMBERS_ADD("add"),
    MEMBERS_REMOVE("remove"),
    CLAIM_ADD("add"),
    CLAIM_REMOVE("remove"),
    CLAIM_MORE("more"),
    CLAIM_LESS("less"),
    ALLY_ADD("add"),
    ALLY_REMOVE("remove"),
    ALLY_NICE("nice"),
    ENEMY_ADD("add"),
    ENEMY_REMOVE("remove"),
    ENEMY_EVIL("evil"),
    WAR_DECLARE("declare"),
    WAR_PEACE("peace");

    private String sub;

    SubCommands(String sub){
        this.sub = sub;
    }

    public String toString() {
        return sub;
    }
}
