/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.utils.functions;

import com.codename1.ui.validation.Constraint;
import java.util.Date;

/**
 *
 * @author Freddy
 */
public class DateTimeConstraint implements Constraint {

    Date other;
    String message;
    Boolean avant;

    public DateTimeConstraint(Date other, String message, Boolean avant) {
        this.other = other;
        this.message = message;
        this.avant = avant;
    }

    @Override
    public boolean isValid(Object value) {
        Date date = (Date) value;
        if(other==null || !(other instanceof Date))
            return date.getTime() >= new Date().getTime();
        
        return avant? date.getTime() < other.getTime() : date.getTime() > other.getTime();
    }

    @Override
    public String getDefaultFailMessage() {
        return this.message;
    }

}
