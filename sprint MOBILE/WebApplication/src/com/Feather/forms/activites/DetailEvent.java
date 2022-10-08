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
 * @author Freddy
 */
class DetailEvent extends BaseForm {

    private EventForm parent;
    private String urlImage = Statics.BASE_URL + "/uploads/images/events/" ;

    public DetailEvent(Evenement ev, EventForm parent, Resources theme) {
        super(theme,"Détails", BoxLayout.y());
        this.parent = parent;
        

        Container main = new Container(BoxLayout.y());
        main.getAllStyles().setMargin(50, 50, 50, 50);
        Container cnt1 = new Container(BoxLayout.y());
        cnt1.getAllStyles().setMarginBottom(50);
        Label titre = new Label(ev.getTitre());
        Label periode = new Label(ev.getDateDebut() + " à " + ev.getDateFin());
        periode.setIcon(FontImage.createMaterial(FontImage.MATERIAL_TIMER, periode.getUnselectedStyle()));
        periode.setAutoSizeMode(true);
        cnt1.addAll(titre, periode);

        Container cnt2 = new Container(BoxLayout.y());

        if (!ev.getLieu().isEmpty()) {
            cnt2.add(addDetailItem(new Label(ev.getLieu()), "Lieu"));
        }
        if (!ev.getDescription().isEmpty()) {
            cnt2.add(addDetailItem(new SpanLabel(ev.getDescription()), "Description"));
        }
        if (!ev.getAffiche().isEmpty()) {
            cnt2.addAll(new Label("Affiche :"), getImageEvent(urlImage+ev.getAffiche(),ev.getAffiche()));
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
                    new EventsService().deleteEvent(ev.getId());
                    parent.removeAll();
                    parent.displayEvents();
                    parent.showBack();
                }
            });
            addButtonAdd(FontImage.MATERIAL_UPDATE, RIGHT, BOTTOM, (evt) -> {
                //page de mise a jour
                new AddEvent(this.parent, getTheme(),ev).show();
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
