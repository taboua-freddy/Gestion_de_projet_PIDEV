/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import enumeration.TypeImportance;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    public static Timestamp dateTimeToTimestamp(LocalDateTime d) {
        return Timestamp.valueOf(d);
    }

    public static LocalDateTime TimestampToDateTime(Timestamp t) {
        return t.toLocalDateTime();
    }

    public static boolean confitDate(LocalDateTime ddu, LocalDateTime dfu, LocalDateTime ddp, LocalDateTime dfp) {
        boolean conflit = false;

        long timddu = Timestamp.valueOf(ddu).getTime();
        long timddp = Timestamp.valueOf(ddp).getTime();
        long timdfu = Timestamp.valueOf(dfu).getTime();
        long timdfp = Timestamp.valueOf(dfp).getTime();

        if (timdfp >= timddu && timdfp <= timdfu) {
            return true;
        }
        if (timddp >= timddu && timddp <= timdfu) {
            return true;
        }
        if (timddp <= timddu && timdfp >= timdfu) {
            return true;
        }

        return conflit;
    }

    public static boolean matchDate(LocalDateTime ddu, LocalDateTime dfu, LocalDateTime dateVerifie) {
        boolean match = false;

        long timddu = Timestamp.valueOf(ddu).getTime();
        long timdfu = Timestamp.valueOf(dfu).getTime();
        long timdver = Timestamp.valueOf(dateVerifie).getTime();

        if (timdver >= timddu && timdver <= timdfu) {
            return true;
        }

        return match;
    }

    public static LocalDateTime dateRappel(LocalDateTime date, int intervalle, String unite) {
        LocalDateTime rappel = date;

        if (unite.equals("heures")) {
            rappel = rappel.minusHours(intervalle);
        } else if (unite.equals("minutes")) {
            rappel = rappel.minusMinutes(intervalle);
        } else if (unite.equals("jours")) {
            rappel = rappel.minusDays(intervalle);
        }

        return rappel;
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
