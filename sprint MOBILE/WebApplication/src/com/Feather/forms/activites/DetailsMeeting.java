/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.activites;

import com.Feather.forms.main.BaseForm;
import com.Feather.models.activite.Objectif;
import com.Feather.models.activite.Reunion;
import com.Feather.services.activites.MeetingService;
import com.Feather.utils.Statics;
import com.Feather.utils.functions.Functions;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.LEFT;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import java.util.ArrayList;

/**
 *
 * @author Freddy
 */
public class DetailsMeeting extends BaseForm{
    
    private MeetingForm parent;
    private ArrayList<Objectif> objectifs;

    public DetailsMeeting(Reunion reunion, MeetingForm parent, Resources theme) {
        super(theme,"Détails", BoxLayout.y());
        this.parent = parent;
        this.objectifs = new MeetingService().getObjectifs(reunion.getId());
        reunion.setObjectifs(objectifs);

        Container main = new Container(BoxLayout.y());
        main.getAllStyles().setMargin(50, 50, 50, 50);
        Container cnt1 = new Container(BoxLayout.y());
        cnt1.getAllStyles().setMarginBottom(50);
        Label titre = (Label) setBgColor(new Label(reunion.getTitre()), Functions.getImportanceColor(reunion.getImportance().getValue()), true);
        Label periode = new Label(reunion.getDateDebut() + " à " + reunion.getDateFin());
        periode.setIcon(FontImage.createMaterial(FontImage.MATERIAL_TIMER, periode.getUnselectedStyle()));
        periode.setAutoSizeMode(true);
        cnt1.addAll(titre, periode);

        Container cnt2 = new Container(BoxLayout.y());
        
        Button afficherPart = new Button("Liste des prticipants");
        FontImage.setMaterialIcon(afficherPart, FontImage.MATERIAL_PERSON);
        afficherPart.addActionListener((evt) -> {
            // page d'ajout des participants
            new AddPartcipants(this, getTheme(), reunion).setModify(false).displayUser().show();
        });
        
        cnt2.add(afficherPart);

        if (reunion.getCoordonateur()!=null) {
            cnt2.add(addDetailItem(new Label(reunion.getCoordonateur().getNom()+" "+reunion.getCoordonateur().getNom() ), "Coordonateur"));
        }
        if (reunion.getSprint()!=null) {
            cnt2.add(addDetailItem(new Label(reunion.getSprint().getNomSprint()), "Sprint"));
        }
        
        cnt2.addAll(addDetailItem(new Label(reunion.getImportance().getText()), "Imporatnce"));
        
        if (!reunion.getThemeDuJour().isEmpty()) {
            cnt2.add(addDetailItem(new SpanLabel(reunion.getThemeDuJour()), "Theme"));
        }
        
        if (!reunion.getDescription().isEmpty()) {
            cnt2.add(addDetailItem(new SpanLabel(reunion.getDescription()), "Description"));
        }
        
        if (!reunion.getObjectifs().isEmpty()) {
            cnt2.add(new Label("Objectifs  :"));
            for(Objectif objectif: reunion.getObjectifs())
                cnt2.add(new Label("                ->"+objectif.getObjectif()));
        }

        main.addAll(cnt1, cnt2);
        this.add(main);
        setBgColor(this, 0xdcdde1, false);
        setBgColor(cnt1, 0xFFFFFF, false);
        setBgColor(cnt2, 0xFFFFFF, false);
        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
        if (Statics.user.isAdmin()) {
            addButtonAdd(FontImage.MATERIAL_DELETE, LEFT, BOTTOM, (evt) -> {
                //suprimer 
                if (Dialog.show("Confimation", "êtes vous sûre de vouloir supprimer ?", "oui", "annuler")) {
                    new MeetingService().deleteMeeting(reunion.getId());
                    parent.removeAll();
                    parent.displayMeetings();
                    parent.showBack();
                }
            });
            addButtonAdd(FontImage.MATERIAL_UPDATE, RIGHT, BOTTOM, (evt) -> {
                //page de mise a jour
                new AddMeeting(this.parent, getTheme(),reunion).show();
            });
        }
        this.revalidate();
    }

    public Container addDetailItem(Component c, String label) {
        Container cnt = new Container(BoxLayout.x());
        Label l = new Label(label);
        l.setAutoSizeMode(true);
        c.getAllStyles().setBgTransparency(255);
        c.getAllStyles().setFgColor(0x34495e,true);
        cnt.addAll(l, new Label(":"), c);
        return cnt;
    }

}
