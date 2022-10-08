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
import com.codename1.components.SpanLabel;
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

/**
 *
 * @author Aymen
 */
class DetailProjet extends BaseForm {

    private ProjectForm parent;

    public DetailProjet(Projet ev, ProjectForm parent, Resources theme) {
        super(theme,"Détails", BoxLayout.y());
        this.parent = parent;
        

        Container main = new Container(BoxLayout.y());
        main.getAllStyles().setMargin(50, 50, 50, 50);
        Container cnt1 = new Container(BoxLayout.y());
        cnt1.getAllStyles().setMarginBottom(50);
        Label titre = new Label(ev.getNom());
        Label periode = new Label(ev.getDateDebut() + " à " + ev.getDateFin());
        periode.setIcon(FontImage.createMaterial(FontImage.MATERIAL_TIMER, periode.getUnselectedStyle()));
        periode.setAutoSizeMode(true);
        cnt1.addAll(titre, periode);

        Container cnt2 = new Container(BoxLayout.y());

       
            cnt2.add(addDetailItem(new Label(ev.getStatus().enumToString()), "Status"));
        
        if (!ev.getDescription().isEmpty()) {
            cnt2.add(addDetailItem(new SpanLabel(ev.getDescription()), "Description"));
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
                    new ProjectService().deleteProjet(ev.getIdProjet());
                    parent.removeAll();
                    parent.displayProjects();
                    parent.showBack();
                }
            });
            addButtonAdd(FontImage.MATERIAL_UPDATE, RIGHT, BOTTOM, (evt) -> {
                //page de mise a jour
                new AddProject(this.parent, getTheme(),ev).show();
            });
        }
        this.revalidate();
    }

    public Container addDetailItem(Component c, String label) {
        Container cnt = new Container(BoxLayout.x());
        Label l = new Label(label);
        l.setAutoSizeMode(true);
        cnt.addAll(l, new Label(":"), c);
        return cnt;
    }

    
}
