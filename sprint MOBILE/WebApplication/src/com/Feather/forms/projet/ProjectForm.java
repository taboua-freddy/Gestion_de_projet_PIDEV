/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.projet;

import com.Feather.forms.main.BaseForm;

import com.Feather.models.projet.Projet;
import com.Feather.services.projet.ProjectService;
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

/**
 *
 * @author Aymen
 */
public class ProjectForm extends BaseForm{
    
    public ProjectForm(Resources theme) {
        super(theme,"Project", BoxLayout.y());
        
        
        displayProjects();
        setBgColor(this, 0xdcdde1, false);
        if(Statics.user.isAdmin())
        {
            addButtonAdd(FontImage.MATERIAL_ADD, CENTER, BOTTOM,((evt) -> {
                //page de Ajout
                new AddProject(this, getTheme(),null).show();
            }));
        }
          addButtonAdd(FontImage.MATERIAL_DETAILS, CENTER, BOTTOM,((evt) -> {
                //page de Ajout
                new StatProjectForm(this, getTheme(),null).show();
            }));
    }
    
    public ProjectForm displayProjects()
    {
        
        
        ArrayList<Projet> listProjets = new ProjectService().getAllProjets();
        
        
        
        
        Container started,notstarted,pending,finished;
        finished = new Container(BoxLayout.y());
        finished.add(setBgColor(new Label("Projet Fini"), 0x34495e,true));
        notstarted = new Container(BoxLayout.y());
        notstarted.add(setBgColor(new Label("Projet à commencer"), 0x34495e,true));
        started = new Container(BoxLayout.y());
        started.add(setBgColor(new Label("Projet en cours"), 0x0fbcf9,true));
        pending = new Container(BoxLayout.y());
        pending.add(setBgColor(new Label("Projet en pause"), 0xe67e22,true));
        
        setBgColor(finished, 0xFFFFFF, false);
        setBgColor(notstarted, 0xFFFFFF, false);
        setBgColor(started, 0xFFFFFF, false);
        setBgColor(pending, 0xFFFFFF, false);
        
        for(Projet projet : listProjets)
        {
            switch(projet.getStatus()){
                case NOTSTARTED:
                    notstarted.add(addProject(projet));
                    break;
                case INPROGRESS :
                    started.add(addProject(projet));
                    break;
                case FINISHED :
                    finished.add(addProject(projet));
                    break;
                default :
                    pending.add(addProject(projet));
                    break;
                    
            }
        }
        
        
        if(notstarted.getComponentCount()!=1)
            this.add(notstarted);
        if(started.getComponentCount()!=1)
            this.add(started);
        if(pending.getComponentCount()!=1)
            this.add(pending);
        if(finished.getComponentCount()!=1)
            this.add(finished);
        
        displayTotal(listProjets.size());
        return this;
    }
    
    
    
    
    public void displayTotal(int total)
    {
        Container cnt = new Container(new FlowLayout(CENTER));
        cnt.add(new Label("Total : "+String.valueOf(total)));
        this.add(cnt);
    }
    
    public MultiButton addProject(Projet ev)
    {
        Container cnt1 = new Container(BoxLayout.x());
        Container cnt2 = new Container(BoxLayout.y());
        Image img = getTheme().getImage("icons8-calendrier-48.png");
        img = img.scaledHeight(Display.getInstance().getDisplayWidth()/ 7);
        ScaleImageLabel sl = new ScaleImageLabel(img);
        Label titre = new Label(Functions.capitalize(ev.getNom()));
        Label lieu = new Label("Description : "+ev.getDescription());
        Label periode = new Label(ev.getDateDebut()  +" à "+ ev.getDateFin());
        periode.setAutoSizeMode(true);
        
        cnt2.addAll(titre,lieu,periode);
        cnt1.addAll(sl,cnt2);
        
        MultiButton m = new MultiButton("");
          
        m.add(BorderLayout.SOUTH,cnt1);

        m.addActionListener((evt) -> {
            new DetailProjet(ev, this, getTheme()).show();
        });
        return m;
        
    }
   
}
