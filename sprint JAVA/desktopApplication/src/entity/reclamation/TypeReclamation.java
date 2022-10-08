/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.reclamation;

/**
 *
 * @author Legion
 */
public enum TypeReclamation {
    MEETING("meeting"),EVENT("event");

    private String text;
    

    private TypeReclamation(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
    

   
    
   
}
