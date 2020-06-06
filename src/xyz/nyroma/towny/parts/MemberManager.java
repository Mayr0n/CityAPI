package xyz.nyroma.towny.parts;

import java.util.ArrayList;
import java.util.List;

public abstract class MemberManager<T> {
    private List<T> members = new ArrayList<>();

    protected abstract boolean add(String member);
    protected abstract boolean remove(String member);

    public List<T> get(){
        return this.members;
    }
}
