package com.Feather.forms.sprint;

import com.Feather.forms.main.BaseForm;
import com.Feather.models.projet.Projet;
import com.Feather.models.sprint.Sprint;
import com.Feather.models.sprint.Story;
import com.Feather.services.sprint.SprintService;
import com.Feather.services.sprint.StoryService;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;

import java.io.IOException;
import java.util.ArrayList;

public class UserStorysForm extends BaseForm {

    private ArrayList<Story> stories = new ArrayList<Story>();
    private StoryService ser = new StoryService();
    private Resources resource;
    private Sprint sprint;
    Container ct = new Container();

    Command back = new Command("Back") {
        @Override
        public void actionPerformed(ActionEvent evt) {
            showBack();
        }
    };


    public UserStorysForm(Resources res,Sprint sprint) {
        super(res,"Stories", BoxLayout.y());
        Dialog ip = new InfiniteProgress().showInifiniteBlocking();
        resource = res;

        this.sprint=sprint;
        // super.addSideMenu(res);
        Tabs swipe = new Tabs();

        Label spacer1 = new Label();
        Label spacer2 = new Label();
        EncodedImage enc;
        EncodedImage enc1;
        try
        {
            enc = EncodedImage.create("/project4.jpg");
            addTab(swipe, enc, spacer1, "", "", "");
            enc1 = EncodedImage.create("/project5.jpg");
            addTab(swipe, enc1, spacer2, "", "", "");
        }
        catch (IOException ex)
        {
        }

        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();

        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }

        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });

        Component.setSameSize(radioContainer, spacer1, spacer2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
        stories.clear();
        stories = ser.getStories(this.sprint.getID());


        for(Story s : stories)
        {
            int deviceWidth = Display.getInstance().getDisplayWidth();
            Image placeholder = Image.createImage(deviceWidth, deviceWidth);
            EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
            String url = "http://localhost/scrum/story.png";
            placeholder = URLImage.createToStorage(encImage, url, url);
            addStoryButton(placeholder, s);
        }

        add(ct);
        Button create = new Button("Create a new Story");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                new UserStoryForm(resource, null,back,sprint).displayMenu().show();
            }
        });
        Container content = BoxLayout.encloseY(
                create
        );

        content.setScrollableY(true);
        add(content);
    }

    private void updateArrowPosition(Button b, Label arrow) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();
    }

    private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        Label likes = new Label(likesStr);
        Style heartStyle = new Style(likes.getUnselectedStyle());
        heartStyle.setFgColor(0xff2d55);
        FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, heartStyle);
        likes.setIcon(heartImage);
        likes.setTextPosition(RIGHT);

        Label comments = new Label(commentsStr);
        FontImage.setMaterialIcon(comments, FontImage.MATERIAL_CHAT);
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");

        Container page1 =
                LayeredLayout.encloseIn(
                        image,
                        overlay,
                        BorderLayout.south(
                                BoxLayout.encloseY(
                                        new SpanLabel(text, "LargeWhiteText"),
                                        spacer
                                )
                        )
                );

        swipe.addTab("", page1);
    }

    private void addStoryButton(Image img, Story story) {
        int height = Display.getInstance().convertToPixels(11.5f);
        int width = Display.getInstance().convertToPixels(14f);
        Button image = new Button(img.fill(width, height));
        image.setUIID("Label");
        image.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                System.out.println("selem");
                //new SprintForm(resource,sprint,back).show();
            }
        });
        Container cnt = BorderLayout.west(image);
        TextArea ta = new TextArea(story.getNomStory());
        ta.setUIID("NewsTopLine");
        ta.setEditable(false);


        Label update = new Label("edit");
        FontImage.setMaterialIcon(update, FontImage.MATERIAL_UPDATE);
        update.addPointerPressedListener((evt) -> {
            new UserStoryForm(resource,story,back,sprint).displayMenu().show();


        });

        Label delete = new Label("delete");
        FontImage.setMaterialIcon(delete, FontImage.MATERIAL_DELETE);
        delete.addPointerPressedListener((evt) -> {
            ser.delete(story.getIdStory());
            new UserStorysForm(resource,this.sprint).displayMenu().show();
        });
        cnt.add(BorderLayout.CENTER,
                BoxLayout.encloseY(
                        ta,
                        BoxLayout.encloseX(update, delete)
                ));

        ct.add(cnt);
    }

    private void bindButtonSelection(Button b, Label arrow) {
        b.addActionListener(e -> {
            if(b.isSelected()) {
                updateArrowPosition(b, arrow);
            }
        });
    }
}
