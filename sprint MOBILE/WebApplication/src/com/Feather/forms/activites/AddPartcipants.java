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
import com.codename1.components.MultiButton;
import com.codename1.components.ScaleImageLabel;
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
public class AddPartcipants extends BaseForm{
    
    public MeetingService ms = new MeetingService();
    private Boolean modify = true;
    private Reunion reunion = null;
    private ArrayList<User> participantsDB = new ArrayList<>() ;
    
    public AddPartcipants(BaseForm parent,Resources theme,Reunion reunion) {
        super(theme, "Participants", BoxLayout.y());
        this.participantsDB = new ArrayList<>();
        this.reunion = reunion;
        if(reunion!=null)
        {
            participantsDB = ms.getAllParticipants(reunion.getId());
            
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (User user : participantsDB) {
            
            list.add(user.getIdUser());
        }
        setParticipants(list);
        setBgColor(this, 0xdcdde1, false);
        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
        
    }
    
    public AddPartcipants displayUser()
    {
        ArrayList<User> listP;
        if(getModify())
            listP = ms.getAllUsers();
        else
            listP = participantsDB;
            
        int total = 0;
       
        for(User u : listP)
        {
            if(getModify())
            {
                this.add(addUser(u));
                total++;
            }   
            else
            {
                if(getParticipants().contains(u.getIdUser()))
                {
                    this.add(addUser(u));
                    total++;
                }  
            }
        }
        
        displayTotal(total);
        return this;
    }
    
    
    public void displayTotal(int total)
    {
        Container cnt = new Container(new FlowLayout(CENTER));
        cnt.add(new Label("Total : "+String.valueOf(total)));
        this.add(cnt);
    }
    
    public MultiButton addUser(User u)
    {
        //System.err.println(u.getPresence());
        Container cnt1 = new Container(new BorderLayout());
        Container cnt2 = new Container(BoxLayout.y());
        Image img = getTheme().getImage("icons8-utilisateur-masculin-128.png");
        img = img.scaledHeight(Display.getInstance().getDisplayWidth()/ 7);
        ScaleImageLabel sl = new ScaleImageLabel(img);
        Label nom = new Label(Functions.capitalize(u.getNom()+" "+u.getPrenom()));
        Label groupe = new Label("Groupe : "+ u.getIdGroupe().getNomGroupe());
        String titre = (u.getPresence()==1)?"etait présent":"etait absent";
        
        if(reunion!=null)
        {
            if( reunion.getDateDebut().getTime()/86400000 == (new Date().getTime()/86400000))
                titre = (u.getPresence()==1)?"est présent":"est absent";
            else if(reunion.getDateDebut().getTime() > new Date().getTime())
                titre = (u.getPresence()==1)?"sera présent":"sera absent"; 
        }
        
        Label presence = new Label("Présence : "+ titre);
        
        if(u.getPresence() == 1)
            setBgColor(presence, 0x2ecc71, true);
        else
            setBgColor(presence, 0xeb2f06, true);
        
        setBgColor(cnt2, 0xffffff, false);
        setBgColor(cnt1, 0xffffff, false);
        setBgColor(groupe, 0x0fbcf9, false);
        cnt2.addAll(nom,groupe);
        if(!modify)
            cnt2.add(presence);
        cnt1.add(BorderLayout.WEST,sl);
        cnt1.add(BorderLayout.CENTER,cnt2);
        
        MultiButton m = new MultiButton("");
        if(getModify())
            m.setCheckBox(true);
        if (getParticipants().contains(u.getIdUser())) {
            m.setSelected(true);
        }
        else
             m.setSelected(false);
        
        m.add(BorderLayout.SOUTH,cnt1);
        
        m.addActionListener((evt) -> {
            // ajout des participants dans le variable static
            addParticipant(Integer.valueOf(u.getIdUser()),m.isSelected());
        });
        return m;
        
    }

    public Boolean getModify() {
        return modify;
    }

    public AddPartcipants setModify(Boolean modify) {
        this.modify = modify;
        return this;
    }
    
}
