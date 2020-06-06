package xyz.nyroma.towny.cityparts;

import xyz.nyroma.towny.parts.MemberManager;

public class MembersManager extends MemberManager<String> {

    @Override
    public boolean add(String name){
        return getMembers().add(name);
    }

    @Override
    public boolean removeMember(String name) {
        if(getMembers().contains(name)){
            return getMembers().remove(name);
        } else {
           return false;
        }
    }

    public boolean isMember(String name){
        return getMembers().contains(name);
    }
}
