/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.main;

import com.Feather.models.user.User;
import com.Feather.services.user.UserService;
import com.codename1.components.FloatingHint;
import com.codename1.components.ScaleImageLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;

/**
 *
 * @author Freddy
 */
public class LoginForm extends BaseForm{
    
    public UserService us = new UserService();
    
    public LoginForm(Resources theme) {
        super(theme,new BorderLayout());
 
        getTitleArea().setUIID("Container");
        getToolbar().setHidden(true);
        
        Image img = theme.getImage("fond.jpg");
        if(img.getHeight() > Display.getInstance().getDisplayHeight()) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight());
        }
        if(img.getWidth() > Display.getInstance().getDisplayWidth()) {
            img = img.scaledWidth(Display.getInstance().getDisplayWidth());
        }
        ScaleImageLabel sl = new ScaleImageLabel(img);
        sl.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        add(BorderLayout.CENTER,sl);
        //add(BorderLayout.OVERLAY, new Label(theme.getImage("fond.jpg"), "LogoLabel"));
        
        TextField username = new TextField("", "Username", 20, TextField.ANY);
        TextField password = new TextField("", "Password", 20, TextField.PASSWORD);
        username.setSingleLineTextArea(false);
        password.setSingleLineTextArea(false);
        Button signIn = new Button("Sign In");
        Label doneHaveAnAccount = new Label("");
        
        Container content = BoxLayout.encloseY(
                new FloatingHint(username),
                createLineSeparator(),
                new FloatingHint(password),
                createLineSeparator(),
                signIn,
                createLineSeparator(),
                doneHaveAnAccount
        );
        content.setScrollableY(false);
        this.setScrollableY(false);
        add(BorderLayout.SOUTH, content);
        signIn.requestFocus();
        signIn.addActionListener(e -> {
            String usernam = username.getText();
            String passwor = password.getText();
            if(usernam.isEmpty() || passwor.isEmpty())
            {
                showToast("Entrez un Login ou mot de passe");
            }
            else
            {
                User user = us.getUser(usernam, passwor);
                if(user==null)
                {
                    showToast("Username ou Mot de passe incorrect");
                }
                else
                {
                    setUser(user);
                    us.getRole();
                    new HomeForm(theme).displayMenu().show();
                }   
            }
            
        
        });
    }
    
    
}
