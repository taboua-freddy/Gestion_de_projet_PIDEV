/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumeration;

/**
 *
 * @author Freddy
 */
public enum TypeSprint {
    PLANNIG("Sprint Planning"),STANDUP("Sprint StandUp"),REVIEW("Sprint Review"),RETRO("Sprint Retrospective");

    private String text;
    private TypeSprint() {
    }
    
    TypeSprint(String text)
    {
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
