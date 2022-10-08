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
import com.Feather.utils.functions.DateTimeConstraint;
import com.codename1.components.ImageViewer;
import com.codename1.components.SpanLabel;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.SOUTH;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.PickerComponent;
import com.codename1.ui.TextComponent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.TextModeLayout;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;
import java.util.Date;

/**
 *
 * @author Freddy
 */
class AddEvent extends BaseForm {

    private String pathAffiche = "";
    private ImageViewer l = new ImageViewer();
    private Evenement event = null;
    private String urlImage = Statics.BASE_URL + "/uploads/images/events/" ;

    public AddEvent(EventForm parent, Resources theme, Evenement ev) {
        super(theme,"Programmer", new BorderLayout());
        this.event = ev;
        setBgColor(this, 0xdcdde1, false);

        display(parent);

        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
    }

    private void display(EventForm parent) {
        TextModeLayout tm = new TextModeLayout(5, 2);
        Container container = new Container(tm);
        //setBgColor(container, 0xffffff, false);
        container.add(tm.createConstraint().horizontalSpan(2), new SpanLabel("Programmer votre événement ici"));

        TextComponent titre = new TextComponent().labelAndHint("Titre");
        
        container.add(tm.createConstraint().horizontalSpan(2), titre);

        PickerComponent dateDebut = PickerComponent.createDateTime((event==null)?new Date():event.getDateDebut()).label("Debut");
        container.add(tm.createConstraint(), dateDebut);

        PickerComponent dateFin = PickerComponent.createDateTime((event==null)?new Date():event.getDateFin()).label("Fin");
        container.add(tm.createConstraint(), dateFin);

        l = (event==null)?new ImageViewer():getImageEvent(urlImage+event.getAffiche(),event.getAffiche());
        
        Container aff = new Container(new BorderLayout());
        Button photo = new Button("Affiche");
        aff.add(CENTER, l);
        container.add(tm.createConstraint().horizontalSpan(2), photo);
        String affiche = "";
        photo.addActionListener((evt) -> {
            Display.getInstance().openGallery(e -> {
                if (e == null || e.getSource() == null) {
                    showToast("Annulé");
                    return;
                }
                String filePath = (String) e.getSource();
                setImage(filePath, l);
            }, Display.GALLERY_IMAGE);
        });
        FontImage.setMaterialIcon(photo, FontImage.MATERIAL_ADD_A_PHOTO);

        TextComponent description = new TextComponent().labelAndHint("Description").multiline(true).rows(3);
        container.add(tm.createConstraint().horizontalSpan(2), description);
        if(event!=null)
        {
            titre.text(event.getTitre());
            description.text(event.getDescription());
        }

        Button submit = new Button("Sauvegarder");
        FontImage.setMaterialIcon(submit, FontImage.MATERIAL_DONE);
        submit.addActionListener(e -> {
            //button ajouter
            String ti = titre.getText();
            String desc = description.getText();
            Date dateD = (Date) dateDebut.getPicker().getValue();
            Date dateF = (Date) dateFin.getPicker().getValue();
            
            Evenement ev = (event==null)?new Evenement():event;
            
            ev.setTitre(ti);
            ev.setDescription(desc);
            ev.setDateDebut(dateD);
            ev.setDateFin(dateF);
            if(!pathAffiche.isEmpty())
            {
                if(event!=null)
                    Storage.getInstance().deleteStorageFile(event.getAffiche());
                    
                ev.setAffiche(pathAffiche);
            }
            new EventsService().uploadEvent(ev);

            if (Dialog.show("Confirmation", ((event==null)?"Ajouter":"Modifié")+" avec succes", "ok", null)) {
                parent.removeAll();
                parent.displayEvents();
                parent.showBack();
            }
        });
        container.add(aff);
        this.add(CENTER, container);
        this.add(SOUTH, submit);
        this.setEditOnShow(titre.getField());
        container.setScrollableY(true);

        Validator val = new Validator();
        val.addConstraint(titre, new LengthConstraint(2, "Obligatoire")).
                addConstraint(dateDebut, new DateTimeConstraint((Date) dateFin.getPicker().getValue(), "Doit être superieur à la date  de fin", true)).
                addConstraint(dateFin, new DateTimeConstraint((Date) dateDebut.getPicker().getValue(), "Doit être superieur à la date  de debut", false)).
                addConstraint(dateDebut, new DateTimeConstraint(null, "Doit être superieur à la date actuelle", true)).
                addSubmitButtons(submit);
    }

    private void setImage(String filePath, ImageViewer iv) {
        try {
            Image i1 = Image.createImage(filePath);
            iv.setImage(i1);
            pathAffiche = filePath;
            this.revalidate();
            showToast("Ajouté");
        } catch (Exception ex) {
            Log.e(ex);
            Dialog.show("Error", "Erreur lors du chargement de l'image: " + ex, "OK", null);
        }
    }
  

}
