package com.app.sample.messenger.data;

/**
 * Created by dongheelee on 16. 9. 3..
 */
public class Singleton {

    private static Singleton uniqueInstance;
    private Singleton() {}
    public static Singleton getInstance() {
        if(uniqueInstance == null) {
            uniqueInstance = new Singleton();
        }
        return uniqueInstance;
    }

}
