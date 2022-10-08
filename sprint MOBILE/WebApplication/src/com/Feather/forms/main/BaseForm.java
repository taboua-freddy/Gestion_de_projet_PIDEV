/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.main;

import com.Feather.forms.activites.EventForm;
import com.Feather.forms.activites.MeetingForm;
import com.Feather.forms.projet.ProjectForm;
import com.Feather.forms.reclamations.ReclamationForm;
import com.Feather.forms.sprint.ProjetsForm;
import com.Feather.forms.sprint.StatStory;
import com.Feather.models.user.User;
import com.Feather.utils.Statics;
import com.Feather.utils.functions.Functions;
import com.Feather.utils.functions.SoundMix;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.ToastBar;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Component;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Freddy
 */
public class BaseForm extends Form {

    private Resources theme;
    private HomeForm homeForm;
    private EventForm eventForm;
    private String title;
    private Media m;

    public BaseForm(Resources theme,String title,Layout contentPaneLayout) {
        super(title, contentPaneLayout);
        this.theme = theme;
        this.title = title;
        
        try {
            m = MediaManager.createMedia(theme.getData("clic.wav"),"");
        } catch (IOException ex) {
            
        }
            
    }
    
    public BaseForm(Resources theme,Layout contentPaneLayout)
    {
        super(contentPaneLayout);
        this.theme = theme;
    }

    public BaseForm addButtonAdd(char type, int orientation, int vAlign, ActionListener action) {
        FloatingActionButton fab = FloatingActionButton.createFAB(type);
        fab.bindFabToContainer(getContentPane(), orientation, vAlign);
        fab.addActionListener(action);

        return this;
    }

    public Component setBgColor(Component c, int color, boolean whiteFont) {
        c.getAllStyles().setBgColor(color);
        c.getAllStyles().setBgTransparency(255);
        if (whiteFont) {
            c.getAllStyles().setFgColor(0xFFFFFF);
        }
        return c;
    }
    
    public BaseForm displayMenu() {
        Toolbar tb = this.getToolbar();
        Image img = theme.getImage("fond.jpg");
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 3) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 3);
        }
        ScaleImageLabel sl = new ScaleImageLabel(img);
        sl.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        
        Label userName = new Label(Functions.capitalize(Statics.user.getNom()+""+Statics.user.getPrenom()));
        userName.getAllStyles().setFgColor(0xFFFFFF);
        tb.addComponentToSideMenu(LayeredLayout.encloseIn(sl,
                FlowLayout.encloseCenterBottom(userName )));
        tb.addCommandToOverflowMenu("Logout", null, (evt) -> {
            new LoginForm(theme).show();
        });
        tb.addCommandToOverflowMenu("Exit", null, (evt) -> {
            Display.getInstance().exitApplication();
        });
        tb.addMaterialCommandToSideMenu("HOME", FontImage.MATERIAL_HOME, (evt) -> {
            new HomeForm(getTheme()).displayMenu().show();
            clicSound();
        });
        tb.addMaterialCommandToSideMenu("EVENEMENTS", FontImage.MATERIAL_EVENT_AVAILABLE, (evt) -> {
            new EventForm(getTheme()).setHomeForm(homeForm).displayMenu().show();
        });
        tb.addMaterialCommandToSideMenu("REUNIONS", FontImage.MATERIAL_EVENT_SEAT , (evt) -> {
            new MeetingForm(getTheme()).displayMenu().show();
            clicSound();
        });
        tb.addMaterialCommandToSideMenu("RECLAMATIONS", FontImage.MATERIAL_REPORT , (evt) -> {
            new ReclamationForm(getTheme()).displayMenu().show();
        });
        tb.addMaterialCommandToSideMenu("PROJETS", FontImage.MATERIAL_EVENT_SEAT , (evt) -> {
            new ProjectForm(getTheme()).displayMenu().show();
        });
        tb.addMaterialCommandToSideMenu("SPRINT", FontImage.MATERIAL_EVENT_SEAT , (evt) -> {
            new ProjetsForm(theme,"Projets").displayMenu().show();
        });
         tb.addMaterialCommandToSideMenu("STATS", FontImage.MATERIAL_EVENT_SEAT , (evt) -> {
            new StatStory(theme).displayMenu().show();
        });
        tb.setTitle(title);
        return this;
    }
    
    public ImageViewer getImageEvent(String url ,String imageName) {
        EncodedImage enc = EncodedImage.createFromImage(Image.createImage(this.getWidth(), this.getWidth(), 0xffff0000), true);

        Image img = URLImage.createToStorage(enc, imageName, url, URLImage.RESIZE_SCALE);

        ImageViewer imgV = new ImageViewer(img);

        return imgV;
    }
    
    public void showToast(String text) {
        Image errorImage = FontImage.createMaterial(FontImage.MATERIAL_ERROR, UIManager.getInstance().getComponentStyle("Title"), 4);
        ToastBar.Status status = ToastBar.getInstance().createStatus();
        status.setMessage(text);
        status.setIcon(errorImage);
        status.setExpires(2000);
        status.show();
    }
    
    public Component createLineSeparator() {
        Label separator = new Label("", "WhiteSeparator");
        separator.setShowEvenIfBlank(true);
        return separator;
    }
    
    public boolean displayModal(String message)
    {
        return Dialog.show("Information", message, "ok", null);
    }
    
    public void addParticipant(Integer id,Boolean ajout)
    {
        if(ajout)
        {
            if(!Statics.listP.contains(id))
                Statics.listP.add(id);
        }
        else
            Statics.listP.remove(id);
        
    }

    public Resources getTheme() {
        return theme;
    }

    public BaseForm setTheme(Resources theme) {
        this.theme = theme;
        return this;
    }

    public HomeForm getHomeForm() {
        return homeForm;
    }

    public BaseForm setHomeForm(HomeForm homeForm) {
        this.homeForm = homeForm;
        return this;
    }

    public EventForm getEventForm() {
        return eventForm;
    }

    public BaseForm setEventForm(EventForm eventForm) {
        this.eventForm = eventForm;
        return this;
    }
    
    public User getUser()
    {
        return Statics.user;
    }
    public void setUser(User user)
    {
        Statics.user = user;
    }
    
    public ArrayList<Integer> getParticipants()
    {
        return Statics.listP;
    }
    
    public void setParticipants(ArrayList<Integer> list)
    {
        Statics.listP = list;
    }
    
    public String getTokenGoogle()
    {
        return Statics.token ;
    }
    
    public void setTokenGoogle(String token)
    {
        Statics.token = token;
    }
    
    public void clicSound()
    {
        m.play();
    }

}
