package com.Feather.forms.sprint;


import com.Feather.forms.main.BaseForm;
import com.Feather.models.projet.Projet;
import com.Feather.models.sprint.Sprint;
import com.Feather.models.sprint.Story;
import com.Feather.services.sprint.SprintService;
import com.Feather.services.sprint.StoryService;
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

public class UserStoryForm extends BaseForm {

    private Image imag;
    private EncodedImage enc;
    private StoryService ser = new StoryService();
    private Sprint sprint;

    public UserStoryForm(Resources res, Story story, Command back, Sprint sprint) {
        super(res,"Add/Edit", BoxLayout.y());
        Command test = new Command("Back") {

            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("yo");
                //showBack();
            }
        };
        getToolbar().setBackCommand(back);

        this.sprint=sprint;

        int color = 0x000000;
        Font materialFontBack = Font.createTrueTypeFont("fontello-back", "fontello.ttf");
        FontImage imageBack = FontImage.createFixed("\ue800", materialFontBack, color, 80, 80);
        getToolbar().findCommandComponent(back).setIcon(imageBack);

        String url = "http://localhost/scrum/story.png";

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
        if(story!= null) name.setText(story.getNomStory());
        name.setUIID("TextFieldBlack");
        addStringValue("story Name", name);

        TextField status = new TextField();
        if(story!= null) status.setText(story.getStatus());
        status.setUIID("TextFieldBlack");
        addStringValue("story status", status);

        TextField bv = new TextField();
        if(story!= null) bv.setText(story.getBV()+"");
        bv.setUIID("TextFieldBlack");
        addStringValue("story bv", bv);

        TextField cap = new TextField();
        if(story!= null) cap.setText(story.getPriorite()+"");
        cap.setUIID("TextFieldBlack");
        addStringValue("story cap", cap);

        TextField complexite = new TextField();
        if(story!= null) complexite.setText(story.getComplexite()+"");
        complexite.setUIID("TextFieldBlack");
        addStringValue("Complexite", complexite);

        TextField roi = new TextField();
        if(story!= null) roi.setText(story.getROI()+"");
        roi.setUIID("TextFieldBlack");
        addStringValue("story roi", roi);

        TextField priorite = new TextField();
        if(story!= null) priorite.setText(story.getPriorite()+"");
        priorite.setUIID("TextFieldBlack");
        addStringValue("story priorite", priorite);

        TextArea description = new TextArea();
        if(story!= null) description.setText(story.getDescription());
        description.setUIID("TextAreaBlack");
        addStringValue("story description", description);

        Button confirm = new Button("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                if(story != null)
                {
                    ser.update(name.getText(), bv.getText(), cap.getText(), complexite.getText(),
                            roi.getText(),description.getText(),priorite.getText(),status.getText(),story.getIdStory());
                    new UserStorysForm(res,sprint).displayMenu().show();
                }
                else{
                        ser.add(name.getText(), bv.getText(), cap.getText(), complexite.getText(),
                                roi.getText(),description.getText(),priorite.getText(),status.getText(),sprint.getIdProjet(),sprint.getID());
                    new UserStorysForm(res,sprint).displayMenu().show();


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
