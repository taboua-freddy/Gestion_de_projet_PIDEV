/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.utils.functions;

import com.Feather.utils.enumeration.TypeImportance;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Freddy
 */
public class Functions {

    public static TypeImportance getImportance(int importance) {
        TypeImportance importance1 = null;
        for (TypeImportance t : TypeImportance.values()) {
            if (importance == t.getValue()) {
                importance1 = t;
            }

        }
        return importance1;
    }
    
    public static Vector<String> getTypeSprint()
    {
       Vector<String> tab = new Vector<String>();
       tab.add("Sprint Planning");
       tab.add("Sprint StandUp");
       tab.add("Sprint Review");
       tab.add("Sprint Retrospective");
       return tab;
    }

    public static Date jsonStringToDate(String json)
    {
        Date fdate= new Date();
        String date = json.substring(json.indexOf("=")+1, json.indexOf(" ")).replace('-', '/');
        String annee = date.substring(0, 4);
        String mois = date.substring(5, 7);
        String jour = date.substring(8, 10);
        String temps = json.substring(json.indexOf(" ")+1, json.indexOf("."));
        date = jour+"/"+mois+"/"+annee+" "+temps;
        
        try {
            fdate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage() + "dans la classe "+ Functions.class.getName());
        }
        return fdate;
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static int getImportanceColor(int value) {
        if(value == 0 )
            return 0xFFC312 ;
        if(value == 3 )
            return 0x009432 ;
        if(value == 5 )
            return 0xEA2027 ;
        
        return 0xFFFFFF;
    }
    
    
  public static Date jsonStringToDatee(String json)
    
  {
        Date fdate= new Date();
        String date = json.substring(json.indexOf("=")+1, json.indexOf(" ")).replace('-', '-');
        String annee = date.substring(0, 10);
        
        
        date = annee;
        
        try {
            fdate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage() + "dans la classe ");
        }
        return fdate;
    }
  
      public static Vector<String> getTypeReclam()
    {
       Vector<String> tab = new Vector<String>();
       tab.add("event");
       tab.add("meeting");
       
       return tab;
    }

    
}
