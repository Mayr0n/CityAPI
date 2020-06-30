package xyz.nyroma.towny.main;

public class SLocation {
    private String world;
    private float x = 0;
    private float z = 0;

    public SLocation(String world, float x, float z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public float getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
