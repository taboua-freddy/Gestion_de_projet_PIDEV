/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.projet;

import com.Feather.forms.activites.*;
import com.Feather.forms.main.BaseForm;
import com.Feather.models.projet.Projet;
import com.Feather.services.activites.EventsService;
import com.Feather.services.projet.ProjectService;
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
 * @author Aymen
 */
class AddProject extends BaseForm {

    private String pathAffiche = "";
    private ImageViewer l = new ImageViewer();
    private Projet projet = null;
    private String urlImage = Statics.BASE_URL + "/uploads/images/events/";

    public AddProject(ProjectForm parent, Resources theme, Projet ev) {
        super(theme, "Scrum Master", new BorderLayout());
        this.projet = ev;
        setBgColor(this, 0xdcdde1, false);

        display(parent);

        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
    }

    private void display(ProjectForm parent) {
        TextModeLayout tm = new TextModeLayout(5, 2);
        Container container = new Container(tm);
        //setBgColor(container, 0xffffff, false);
        container.add(tm.createConstraint().horizontalSpan(2), new SpanLabel("Ajouter les details du projet"));

        TextComponent titre = new TextComponent().labelAndHint("Nom");

        container.add(tm.createConstraint().horizontalSpan(2), titre);

        PickerComponent dateDebut = PickerComponent.createDateTime((projet == null) ? new Date() : projet.getDateDebut()).label("Debut");
        container.add(tm.createConstraint(), dateDebut);

        PickerComponent dateFin = PickerComponent.createDateTime((projet == null) ? new Date() : projet.getDateFin()).label("Fin");
        container.add(tm.createConstraint(), dateFin);


        Container aff = new Container(new BorderLayout());
        aff.add(CENTER, l);
       /* container.add(tm.createConstraint().horizontalSpan(2), photo);
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
        FontImage.setMaterialIcon(photo, FontImage.MATERIAL_ADD_A_PHOTO);*/

        TextComponent description = new TextComponent().labelAndHint("Description").multiline(true).rows(3);
        container.add(tm.createConstraint().horizontalSpan(2), description);
        if (projet != null) {
            titre.text(projet.getNom());
            description.text(projet.getDescription());
        }

        Button submit = new Button("Sauvegarder");
        FontImage.setMaterialIcon(submit, FontImage.MATERIAL_DONE);
        submit.addActionListener(e -> {
            //button ajouter
            String ti = titre.getText();
            String desc = description.getText();
            Date dateD = (Date) dateDebut.getPicker().getValue();
            Date dateF = (Date) dateFin.getPicker().getValue();

            Projet ev = (projet == null) ? new Projet() : projet;

            ev.setNom(ti);
            ev.setDescription(desc);
            ev.setDateDebut(dateD);
            ev.setDateFin(dateF);
            ev.setStatus(Projet.status.NOTSTARTED);
            new ProjectService().addProjet(ev);

            if (Dialog.show("Confirmation", ((projet == null) ? "Ajouter" : "Modifié") + " avec succes", "ok", null)) {
                parent.removeAll();
                parent.displayProjects();
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
               // addConstraint(dateDebut, new DateTimeConstraint(null, "Doit être superieur à la date actuelle", true)).
                addSubmitButtons(submit);
    }

   

}
