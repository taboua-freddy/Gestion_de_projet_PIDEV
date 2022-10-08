/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.activites;

import com.Feather.forms.main.BaseForm;
import com.Feather.models.activite.Reunion;
import com.Feather.models.user.User;
import com.Feather.services.activites.MeetingService;
import com.Feather.utils.Statics;
import com.Feather.utils.functions.Functions;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.MultiButton;
import com.codename1.components.ScaleImageLabel;
import com.codename1.ui.Button;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
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
public class MeetingForm extends BaseForm{

    private MeetingService ms = new MeetingService();
    Button presenceBtn;
    int presence;
    
    public MeetingForm(Resources theme) {
        super(theme,"Réunions",BoxLayout.y());
        Dialog ip = new InfiniteProgress().showInifiniteBlocking();
        setParticipants(new ArrayList<>());
        displayMeetings();
        setBgColor(this, 0xdcdde1, false);
        if(getUser().isAdmin())
        {
            addButtonAdd(FontImage.MATERIAL_ADD, CENTER, BOTTOM,((evt) -> {
                //page de Ajout
                clicSound();
               new AddMeeting(this, getTheme(),null).show();
            }));
        }
    }
    
    public Container addBtnApi(Reunion reunion)
    {
        ArrayList<User> part = ms.getAllParticipants(reunion.getId());
        boolean participant = false;
        presence = -1;
        String titre = "";
        for (User user : part) {
            if(getUser().getIdUser() == user.getIdUser())
            {
                participant = true;
                presence = user.getPresence();
            }
            
        }
        
        if(presence == 1)
        {
            titre = "Marquer absence";
        }
        else if(presence == 0)
            titre = "Marquer présent";
            
        Container contBtn = new Container(new BorderLayout());
        Button addCalBtn = new Button("CALENDAR");
        FontImage.setMaterialIcon(addCalBtn, FontImage.MATERIAL_ADD);
        
        presenceBtn = new Button(titre);
        FontImage.setMaterialIcon(presenceBtn, FontImage.MATERIAL_PRESENT_TO_ALL);
        
        presenceBtn.addActionListener((evt) -> {
            clicSound();
            if(reunion.getDateDebut().getTime() > new Date().getTime())
            {
                if(ms.changePresence(reunion.getId(),getUser().getIdUser()))
                {
                    presenceBtn.setText((presence==1)?"Marquer présent":"Marquer absent");
                    refreshTheme();
                    new MeetingForm(getTheme()).displayMenu().show();
                }
            }
            else
                displayModal("La réunion est deja passée");
            
        });
        
        addCalBtn.addActionListener((evt) -> {
            clicSound();
            if(getTokenGoogle().isEmpty())
            {
                String link = ms.getLinkTokenCalendar();
                new WebPage(getTheme(), this,link).setReunion(reunion).displayAdd().show();
            }
            else
            {
                if(ms.addToCalendar(getTokenGoogle(),"meeting",reunion.getId()))
                    displayModal("ajouté à google Calendar");
                else
                    displayModal("échec");
            }
            
        });
        if(participant)
        {
            contBtn.add(CENTER,addCalBtn);
            contBtn.add(RIGHT,presenceBtn);
        }
        else
        {
            contBtn.add(CENTER,addCalBtn);
        }
        
        return contBtn;
    }
    
    public MeetingForm displayMeetings()
    {
        ArrayList<Reunion> listReunion = ms.getAllMeetings();
        Container passed,today,later;
        passed = new Container(BoxLayout.y());
        passed.add(setBgColor(new Label("Réunions Passés"), 0x34495e,true));
        today = new Container(BoxLayout.y());
        today.add(setBgColor(new Label("Aujourd'hui"), 0x0fbcf9,true));
        later = new Container(BoxLayout.y());
        later.add(setBgColor(new Label("Réunions à Venir"), 0xe67e22,true));
        setBgColor(today, 0xFFFFFF, false);setBgColor(passed, 0xFFFFFF, false);setBgColor(later, 0xFFFFFF, false);
        for(Reunion reunion : listReunion)
        {
            if( reunion.getDateDebut().getTime()/86400000 == (new Date().getTime()/86400000))
                today.addAll(addEvent(reunion),addBtnApi(reunion));
            else if(reunion.getDateDebut().getTime() > new Date().getTime())
                later.addAll(addEvent(reunion),addBtnApi(reunion));
            else
                passed.addAll(addEvent(reunion),addBtnApi(reunion));  
        }
        if(today.getComponentCount()!=1)
            this.add(today);
        if(later.getComponentCount()!=1)
            this.add(later);
        if(passed.getComponentCount()!=1)
            this.add(passed);
        
        displayTotal(listReunion.size());
        return this;
    }
    
    
    public void displayTotal(int total)
    {
        Container cnt = new Container(new FlowLayout(CENTER));
        cnt.add(new Label("Total : "+String.valueOf(total)));
        this.add(cnt);
    }
    
    public MultiButton addEvent(Reunion r)
    {
        
        Container cnt1 = new Container(BoxLayout.x());
        Container cnt2 = new Container(BoxLayout.y());
        Image img = getTheme().getImage("icons8-salle-de-réunion-80.png");
        img = img.scaledHeight(Display.getInstance().getDisplayWidth()/ 7);
        ScaleImageLabel sl = new ScaleImageLabel(img);
        Label titre = new Label(Functions.capitalize(r.getTitre()));
        Label importance = new Label("Importance : "+r.getImportance().getText());
        Label periode = new Label(r.getDateDebut()  +" à "+ r.getDateFin());
        periode.setAutoSizeMode(true);
        
        setBgColor(importance, Functions.getImportanceColor(r.getImportance().getValue()), true);
        
        cnt2.addAll(titre,importance,periode);
        cnt1.addAll(sl,cnt2);
        
        MultiButton m = new MultiButton("");
          
        m.add(BorderLayout.SOUTH,cnt1);

        m.addActionListener((evt) -> {
            clicSound();
            new DetailsMeeting(r, this, getTheme()).show();
        });
        return m;
        
    }
    
    
}
