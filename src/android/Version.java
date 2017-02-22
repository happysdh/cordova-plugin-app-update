package com.vaenow.appupdate.android;

/**
 * Created by LuoWen on 2015/12/14.
 */
public class Version {
    private int local;
    private int remote;
    private int lowest;
    public Version(int local, int remote,int lowest) {
        this.local = local;
        this.remote = remote;
        this.lowest = lowest;
    }

    public int getLocal() {
        return local;
    }

    public int getRemote() {
        return remote;
    }

    public int getLowest() {
        return lowest;
    }
}
