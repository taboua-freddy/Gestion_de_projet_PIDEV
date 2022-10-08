package com.Feather.forms.sprint;


import com.Feather.forms.main.BaseForm;
import com.Feather.models.projet.Projet;
import com.Feather.models.sprint.Sprint;
import com.Feather.services.sprint.SprintService;
import com.codename1.components.ImageViewer;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;

import java.util.Date;

public class SprintForm extends BaseForm {

    private Image imag;
    private EncodedImage enc;
    private SprintService ser = new SprintService();
    private Projet projet;

    public SprintForm(Resources res, Sprint sprint,Command back,Projet projet) {
        super(res,"Add/Edit", BoxLayout.y());
        Command test = new Command("Back") {

            @Override
            public void actionPerformed(ActionEvent evt) {
                showBack();
            }
        };

        getToolbar().setBackCommand(back);

        this.projet=projet;

        int color = 0x000000;
        Font materialFontBack = Font.createTrueTypeFont("fontello-back", "fontello.ttf");
        FontImage imageBack = FontImage.createFixed("\ue800", materialFontBack, color, 80, 80);
        getToolbar().findCommandComponent(back).setIcon(imageBack);

        String url = "http://localhost/scrum/sprint.png";

        try
        {
            enc = EncodedImage.create("/tennis_club.png");
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }

        imag = URLImage.createToStorage(enc, url, url, URLImage.RESIZE_SCALE);
        ImageViewer img = new ImageViewer(imag);

        add(img);

        TextField name = new TextField();
        if(sprint!= null) name.setText(sprint.getNomSprint());
        name.setUIID("TextFieldBlack");
        addStringValue("Sprint Name", name);


        Picker datePicker = new Picker();
        datePicker.setType(Display.PICKER_TYPE_DATE);
        if (sprint!=null) {
            try {
                Date date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sprint.getDateDebut());
                datePicker.setDate(date1);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        addStringValue("Starting Date", datePicker);
        Picker datePicker2 = new Picker();
        datePicker2.setType(Display.PICKER_TYPE_DATE);
        if (sprint!=null){
            try {
                Date date1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sprint.getDateFin());
                datePicker2.setDate(date1);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        addStringValue("Ending Date",datePicker2);
        Button confirm = new Button("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                if(sprint != null)
                {
                    String dateDebut = (new SimpleDateFormat("yyyy-MM-dd")).format(datePicker.getDate());
                    String dateFin = (new SimpleDateFormat("yyyy-MM-dd")).format(datePicker2.getDate());

                    ser.update(projet.getIdProjet(), name.getText(), dateDebut, dateFin);
                    new SprintsForm(res,projet).displayMenu().show();
                }
                else{
                        String Name="default";
                        String dateDebut = (new SimpleDateFormat("yyyy-MM-dd")).format(datePicker.getDate());
                        String dateFin = (new SimpleDateFormat("yyyy-MM-dd")).format(datePicker2.getDate());
                    if (name.getText().isEmpty())
                        ser.add(Name, dateDebut, dateFin, projet.getIdProjet());
                    else
                        ser.add(name.getText(), dateDebut, dateFin, projet.getIdProjet());

                    new SprintsForm(res,projet).displayMenu().show();


                }

            }
        });
        Container content = BoxLayout.encloseY(
                confirm
        );

        content.setScrollableY(true);
        add(content);
    }

    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PaddedLabel")).
                add(BorderLayout.CENTER, v));
        add(createLineSeparator(0xeeeeee));
    }
    public Component createLineSeparator(int color) {
        Label separator = new Label("", "WhiteSeparator");
        separator.getUnselectedStyle().setBgColor(color);
        separator.getUnselectedStyle().setBgTransparency(255);
        separator.setShowEvenIfBlank(true);
        return separator;
    }

}
