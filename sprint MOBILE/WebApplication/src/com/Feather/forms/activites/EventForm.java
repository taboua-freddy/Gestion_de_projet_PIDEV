/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.activites;

import com.Feather.forms.main.BaseForm;
import com.Feather.models.activite.Evenement;
import com.Feather.services.activites.EventsService;
import com.Feather.utils.Statics;
import com.Feather.utils.functions.Functions;
import com.codename1.components.MultiButton;
import com.codename1.components.ScaleImageLabel;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Freddy
 */
public class EventForm extends BaseForm{
    
    public EventForm(Resources theme) {
        super(theme,"Evenement", BoxLayout.y());
        
        displayEvents();
        setBgColor(this, 0xdcdde1, false);
        if(Statics.user.isAdmin())
        {
            addButtonAdd(FontImage.MATERIAL_ADD, CENTER, BOTTOM,((evt) -> {
                //page de Ajout
                new AddEvent(this, getTheme(),null).show();
            }));
        }
    }
    
    public EventForm displayEvents()
    {
        ArrayList<Evenement> listEvenements = new EventsService().getAllEvents();
        Container passed,today,later;
        passed = new Container(BoxLayout.y());
        passed.add(setBgColor(new Label("Evénements Passés"), 0x34495e,true));
        today = new Container(BoxLayout.y());
        today.add(setBgColor(new Label("Aujourd'hui"), 0x0fbcf9,true));
        later = new Container(BoxLayout.y());
        later.add(setBgColor(new Label("Evénements à Venir"), 0xe67e22,true));
        setBgColor(today, 0xFFFFFF, false);setBgColor(passed, 0xFFFFFF, false);setBgColor(later, 0xFFFFFF, false);
        for(Evenement event : listEvenements)
        {
            if( event.getDateDebut().getTime()/86400000 == (new Date().getTime()/86400000))
                today.add(addEvent(event));
            else if(event.getDateDebut().getTime() > new Date().getTime())
                later.add(addEvent(event));
            else
                passed.add(addEvent(event));  
        }
        if(today.getComponentCount()!=1)
            this.add(today);
        if(later.getComponentCount()!=1)
            this.add(later);
        if(passed.getComponentCount()!=1)
            this.add(passed);
        
        displayTotal(listEvenements.size());
        return this;
    }
    
    
    public void displayTotal(int total)
    {
        Container cnt = new Container(new FlowLayout(CENTER));
        cnt.add(new Label("Total : "+String.valueOf(total)));
        this.add(cnt);
    }
    
    public MultiButton addEvent(Evenement ev)
    {
        Container cnt1 = new Container(BoxLayout.x());
        Container cnt2 = new Container(BoxLayout.y());
        Image img = getTheme().getImage("icons8-calendrier-48.png");
        img = img.scaledHeight(Display.getInstance().getDisplayWidth()/ 7);
        ScaleImageLabel sl = new ScaleImageLabel(img);
        Label titre = new Label(Functions.capitalize(ev.getTitre()));
        Label lieu = new Label("Lieu : "+ev.getLieu());
        Label periode = new Label(ev.getDateDebut()  +" à "+ ev.getDateFin());
        periode.setAutoSizeMode(true);
        
        cnt2.addAll(titre,lieu,periode);
        cnt1.addAll(sl,cnt2);
        
        MultiButton m = new MultiButton("");
          
        m.add(BorderLayout.SOUTH,cnt1);

        m.addActionListener((evt) -> {
            new DetailEvent(ev, this, getTheme()).show();
        });
        return m;
        
    }
    
}
