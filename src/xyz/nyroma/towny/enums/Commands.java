package xyz.nyroma.towny.enums;

public enum Commands {
    CREATE("create"),
    LIST("list"),
    INFO("info"),
    MONEY("money"),
    MEMBERS("members"),
    CLAIM("claim"),
    OWNER("owner"),
    REMOVE("remove"),
    RENAME("rename"),
    ALLY("ally"),
    ENEMY("enemy"),
    WAR("war");

    private String cmd;

    Commands(String cmd){
        this.cmd = cmd;
    }

    public String getCmd() {
        return this.cmd;
    }
}
