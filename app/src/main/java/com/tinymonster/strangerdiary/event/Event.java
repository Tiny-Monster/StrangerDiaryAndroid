package com.tinymonster.strangerdiary.event;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class Event {
    public enum Type{
        ITEM,LIST,INSERT
    }
    public Type type;
    public Object object;
    public Event(Type type,Object o){
        this.type=type;
        this.object=o;
    }
}
