/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.activites;

import com.Feather.forms.main.BaseForm;
import com.Feather.models.activite.Evenement;
import com.Feather.models.activite.Reunion;
import com.Feather.services.activites.MeetingService;
import com.codename1.ui.BrowserComponent;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.TextComponent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;

/**
 *
 * @author Freddy
 */
public class WebPage extends BaseForm{
    
    private Reunion reunion = null;
    private Evenement event = null;
    private MeetingForm parent;
    private MeetingService ms = new MeetingService();
    
    public WebPage(Resources theme,MeetingForm parent,String link) {
        super(theme,"Google",BoxLayout.y());
        this.parent = parent;
        
        
        Container contBrowser = new Container(new BorderLayout());
        
        BrowserComponent browser = new BrowserComponent();
        browser.setURL("https://accounts.google.com/signin/oauth/oauthchooseaccount?response_type=code&access_type=offline&client_id=189438683848-sg1o01sgtpihlqso3cc54a7okdocotdl.apps.googleusercontent.com&redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob&state&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar&prompt=select_account%20consent&o2v=1&as=O0oQScVFVpkICbdDWl95bw&flowName=GeneralOAuthFlow");
        //browser.setURL("https://mail.google.com/mail/u/1/#inbox");
        contBrowser.add(BorderLayout.CENTER,browser);
        
        this.add(contBrowser);
        contBrowser.setScrollableY(false);
        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
    }
    
    public WebPage displayAdd()
    {
        TextComponent token = new TextComponent().labelAndHint("TOKEN");
        Button addBtn = new Button("Ajouter à Calendar");
        FontImage.setMaterialIcon(addBtn, FontImage.MATERIAL_SAVE);
        
        addBtn.addActionListener((evt) -> {
            String type = "";
            int id = 0;
            if(reunion!=null)
            {
                type = "meeting";
                id = reunion.getId();
            }
            else if(event!=null)
            {
                type = "event";
                id = event.getId();
            }
            setTokenGoogle(token.getText());
            if(ms.addToCalendar((token.getText().isEmpty())?"4/0QFQmTkKE5JCfXpfxbfgJKd5rXGNEkbE0qkxkkUhQk1F1ZHCj5SnAvY":token.getText(),type,id))
                displayModal("ajouté à google Calendar");
            else
                displayModal("échec");
            
            //parent.showBack();
        });
        
        this.addAll(token,addBtn);
        return this;
    }

    public WebPage setReunion(Reunion reunion) {
        this.reunion = reunion;
        return this;
    }

    public WebPage setEvent(Evenement event) {
        this.event = event;
        return this;
    }
    
    
}
