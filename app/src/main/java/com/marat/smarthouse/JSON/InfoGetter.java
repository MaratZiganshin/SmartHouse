package com.marat.smarthouse.JSON;

/**
 * Created by админ on 27.10.2017.
 */

public class InfoGetter {

    public static String getPassword(String login){
        if (login.equals("marat"))
            return "krasava";
        return null;
    }
}
