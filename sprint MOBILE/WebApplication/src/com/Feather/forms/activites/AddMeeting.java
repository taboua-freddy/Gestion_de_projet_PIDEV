/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.activites;

import com.Feather.forms.main.BaseForm;
import com.Feather.models.activite.Objectif;
import com.Feather.models.activite.Reunion;
import com.Feather.models.projet.Projet;
import com.Feather.models.sprint.Sprint;
import com.Feather.models.user.User;
import com.Feather.services.activites.MeetingService;
import com.Feather.utils.Statics;
import com.Feather.utils.enumeration.TypeImportance;
import com.Feather.utils.functions.DateTimeConstraint;
import com.Feather.utils.functions.Functions;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import static com.codename1.ui.CN.SOUTH;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.PickerComponent;
import com.codename1.ui.TextComponent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.TextModeLayout;
import com.codename1.ui.list.DefaultListCellRenderer;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.Validator;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Freddy
 */
public class AddMeeting extends BaseForm {

    private Reunion reunion = null;
    private MeetingService ms = new MeetingService();
    private Container contSprint = new Container();
    private ComboBox<Sprint> sprintsComboBox;

    public AddMeeting(MeetingForm parent, Resources theme, Reunion reunion) {
        super(theme, "Programmer", new BorderLayout());
        this.reunion = reunion;
        setBgColor(this, 0xdcdde1, false);

        display(parent);

        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
    }

    private void display(MeetingForm parent) {
        TextModeLayout tm = new TextModeLayout(5, 2);
        Container container = new Container(tm);

        container.add(tm.createConstraint().horizontalSpan(2), new SpanLabel("Programmer votre Réunion ici"));

        ComboBox<String> titre = new ComboBox<>(Functions.getTypeSprint());
        Container c = addLabelToItem(titre, "Titre");
        setBgColor(titre, 0xffffff, true);
        setBgColor(c, 0xffffff, true);
        container.add(tm.createConstraint().horizontalSpan(2), c);

        PickerComponent dateDebut = PickerComponent.createDateTime((reunion == null) ? new Date() : reunion.getDateDebut()).label("Debut");
        container.add(tm.createConstraint(), dateDebut);

        PickerComponent dateFin = PickerComponent.createDateTime((reunion == null) ? new Date() : reunion.getDateFin()).label("Fin");
        container.add(tm.createConstraint(), dateFin);

        ArrayList<Projet> listP = ms.getAllProjets();
        ComboBox<Projet> projets = new ComboBox<>(listP.toArray());// insertion des projets
        projets.setRenderer(new DefaultListCellRenderer<Projet>(false));
        Container c1 = addLabelToItem(projets, "Projets");
        setBgColor(projets, 0x00a8ff, true);
        setBgColor(c1, 0x00a8ff, true);
        container.add(tm.createConstraint().horizontalSpan(2), c1);

        container.add(tm.createConstraint().horizontalSpan(2), contSprint);
        projets.addActionListener((evt) -> {
            int idProjet = projets.getSelectedItem().getIdProjet();
            contSprint.removeAll();
            // rafraichissement des sprints
            ArrayList<Sprint> listS = ms.getAllSprints(idProjet);
            sprintsComboBox = new ComboBox<>(listS.toArray());// insertion des sprints
            sprintsComboBox.addItem(new Sprint(0, 0, "aucun", "", ""));
            contSprint.add(addLabelToItem(sprintsComboBox, "Sprints"));
            //setBgColor(sprintsComboBox, 0xffffff, true);
            setBgColor(contSprint, 0xffffff, true);
            this.revalidate();
        });
        
        ArrayList<User> listCoordo = ms.getAllUsers();
        ComboBox<User> coordoBox = new ComboBox<>(listCoordo.toArray());// insertion des coordonateurs
        coordoBox.addItem(new User(0, "aucun", "", ""));
        projets.setRenderer(new DefaultListCellRenderer<User>(false));
        Container c2 = addLabelToItem(coordoBox, "Coordonateur");
        setBgColor(coordoBox, 0x00a8ff, true);
        setBgColor(c2, 0x00a8ff, true);
        container.add(tm.createConstraint().horizontalSpan(2), c2);

        Button ajouterPart = new Button("Ajouter des prticipants");
        FontImage.setMaterialIcon(ajouterPart, FontImage.MATERIAL_PERSON_ADD);
        ajouterPart.addActionListener((evt) -> {
            // page d'ajout des participants
            new AddPartcipants(this, getTheme(),(reunion==null)?null:reunion).displayUser().show();
                
        });

        container.add(tm.createConstraint().horizontalSpan(2), ajouterPart);

        TextComponent description = new TextComponent().labelAndHint("Description").multiline(true).rows(3);
        container.add(tm.createConstraint().horizontalSpan(2), description);
        
        ComboBox<TypeImportance> importanceBox = new ComboBox<>();// insertion des importances
        for (TypeImportance value : TypeImportance.values()) {
            importanceBox.addItem(value);
        }
        projets.setRenderer(new DefaultListCellRenderer<TypeImportance>(false));
        Container c3 = addLabelToItem(importanceBox, "Importance");
        setBgColor(importanceBox, 0x00a8ff, true);
        setBgColor(c3, 0x00a8ff, true);
        container.add(tm.createConstraint().horizontalSpan(2), c3);
        
        TextComponent themeDujour = new TextComponent().labelAndHint("Théme");
        container.add(tm.createConstraint().horizontalSpan(2), themeDujour);
        
        TextComponent objectifs = new TextComponent().labelAndHint("Objectifs").multiline(true).rows(3);
        container.add(tm.createConstraint().horizontalSpan(2), objectifs);
        
        if(reunion!=null)
        {
            if(reunion.getSprint()!=null)
            {
                sprintsComboBox = new ComboBox<>(reunion.getSprint());// insertion des sprints si modification
                contSprint.add(addLabelToItem(sprintsComboBox, "Sprints"));
                setBgColor(contSprint, 0xffffff, true);
            }
            
            if(reunion.getCoordonateur()!=null)
                coordoBox.setSelectedItem(reunion.getCoordonateur());
            
            titre.setSelectedItem(reunion.getTitre());
            themeDujour.text(reunion.getThemeDuJour());
            description.text(reunion.getDescription());
            if(!reunion.getObjectifs().isEmpty())
            {
                String text = "";
                for (Objectif ob : reunion.getObjectifs()) {
                    text += ob.getObjectif();
                }
                objectifs.text(text);
            }
            importanceBox.setSelectedItem(reunion.getImportance());
        }

        Button submit = new Button("Sauvegarder");
        FontImage.setMaterialIcon(submit, FontImage.MATERIAL_DONE);
        submit.addActionListener(e -> {
            //button Sauvegarder
            if(Statics.listP==null || Statics.listP.isEmpty())
                new AddPartcipants(this, getTheme(), reunion);
            
            String ti = titre.getSelectedItem();
            String desc = description.getText();
            Date dateD = (Date) dateDebut.getPicker().getValue();
            Date dateF = (Date) dateFin.getPicker().getValue();

            Reunion ev = (reunion == null) ? new Reunion() : reunion;

            ev.setTitre(ti);
            ev.setDescription(desc);
            ev.setDateDebut(dateD);
            ev.setDateFin(dateF);
            ev.setCoordonateur(coordoBox.getSelectedItem());
            ev.setSprint((sprintsComboBox==null)?new Sprint(0, 0, "aucun", "", ""):sprintsComboBox.getSelectedItem());
            ev.setParticipants(Statics.listP);
            ev.setThemeDuJour(themeDujour.getText());
            ev.setImportance(importanceBox.getSelectedItem());
            ArrayList<Objectif> obj = new ArrayList<>();
            obj.add(new Objectif(objectifs.getText()));
            ev.setObjectifs(obj);

            ms.uploadMeeting(ev);
            if (Dialog.show("Confirmation", ((reunion == null) ? "Ajouter" : "Modifié") + " avec succes", "ok", null)) {
                parent.removeAll();
                parent.displayMeetings();
                parent.showBack();
            }
        });
        this.add(CENTER, container);
        this.add(SOUTH, submit);
        container.setScrollableY(true);

        Validator val = new Validator();
        val.
                addConstraint(dateDebut, new DateTimeConstraint((Date) dateFin.getPicker().getValue(), "Doit être superieur à la date  de fin", true)).
                addConstraint(dateFin, new DateTimeConstraint((Date) dateDebut.getPicker().getValue(), "Doit être superieur à la date  de debut", false)).
                addConstraint(dateDebut, new DateTimeConstraint(null, "Doit être superieur à la date actuelle", true)).
                addSubmitButtons(submit);
    }

    public Container addLabelToItem(Component c, String label) {
        Container cnt = new Container(BoxLayout.x());
        Label l = new Label(label);
        l.setAutoSizeMode(true);
        cnt.addAll(l, new Label(" "), c);
        return cnt;
    }

}
