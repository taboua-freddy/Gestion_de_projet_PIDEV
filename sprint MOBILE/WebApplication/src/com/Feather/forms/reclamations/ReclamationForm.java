/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.reclamations;


import com.Feather.forms.main.BaseForm;
import com.Feather.services.reclamations.ReclamationService;
import com.Feather.models.reclamation.Reclamation;

import com.codename1.components.MultiButton;
import com.codename1.components.ScaleImageLabel;

import com.codename1.ui.Button;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;

import java.util.ArrayList;

/**
 *
 * @author Legion
 */
public class ReclamationForm extends BaseForm  {
    private ReclamationForm parent;
    private ReclamationService rs = new ReclamationService();
  public static final String ACCOUNT_SID = "AC775b59d80330510612b00706a9b5ad78";
  public static final String AUTH_TOKEN = "be2a0f9f7906392ef149c1a70723e768";
    
    public ReclamationForm(Resources theme) {
        super(theme,"Reclamations", BoxLayout.y());
       displayReclamations();
        setBgColor(this, 0xdcdde1, false);
       
         addButtonAdd(FontImage.MATERIAL_ADD, CENTER, BOTTOM,((evt) -> {
                //page de Ajout
                new AddReclamation(this, getTheme(),null).show();
            }));
         addButtonAdd(FontImage.MATERIAL_PIE_CHART, CENTER, BOTTOM,((evt) -> {
                //page de stats
               new Stats(this, getTheme()).show();
            }));
     
    }
    public Container addBtnsupp (Reclamation reclam)
    {
        Container contBtn = new Container(new BorderLayout());
        Button addCalBtn = new Button("Supprimmmer");
        FontImage.setMaterialIcon(addCalBtn, FontImage.MATERIAL_DELETE);
        
        
        //setBgColor(addCalBtn, 0x2f3542, true);
        contBtn.add(CENTER,addCalBtn);
        
        
        addCalBtn.addActionListener((evt) -> {
            if (Dialog.show("Confimation", "êtes vous sûre de vouloir supprimer ?", "oui", "annuler")) {
                    new ReclamationService().deleteReclam(reclam.getIdRec());
                    this.removeAll();
                    this.displayReclamations();
                    this.showBack();
             }
       });
        
        return contBtn;
    }
    
    public ReclamationForm displayReclamations(){
        ArrayList<Reclamation>listreclamation= rs.getAllReclams();
        Container myreclam = new Container(new BorderLayout());
     
        myreclam = new Container(BoxLayout.y());
        myreclam .add(setBgColor(new Label("Mes reclamations"), 0x34495e,true));
  
        
        
      for(Reclamation rec:listreclamation){
      
          myreclam.addAll(addRec(rec),addBtnsupp(rec));
         
        
            if(rec.getReponse()!= null){
           
       
            }
      }
     
         displayTotal(listreclamation.size());
         this.add(myreclam);
        
  // Find your Account Sid and Token at twilio.com/user/account

/*
 
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message = Message.creator(new PhoneNumber("+21699370174"),
        new PhoneNumber("+13344713116"), 
        "Votre reclamation est traitée ! ").create();

    System.out.println(message.getSid());
  
  */  
        return this;
    }
    
     public void displayTotal(int total)
    {
        Container cnt = new Container(new FlowLayout(CENTER));
        cnt.add(new Label("Total : "+String.valueOf(total)));
        this.add(cnt);
        
    }
     public MultiButton addRec(Reclamation rec){
         Container cnt1 = new Container(BoxLayout.x());
        Container cnt2 = new Container(BoxLayout.y());
        setBgColor(cnt2, 0xFFFFFF, false);
       Image img = getTheme().getImage("icons8-salle-de-réunion-80.png");
        img = img.scaledHeight(Display.getInstance().getDisplayWidth()/ 7);
        ScaleImageLabel sl = new ScaleImageLabel(img);
        
        Label type = new Label("Type de reclamation: "+(rec.gettypeRec()));
        Label description = new Label("description: "+rec.getDescription());
        Label date = new Label("date: "+rec.getDateRec());
        date.setAutoSizeMode(true);
        Label reponse = new Label("reponse: "+rec.getReponse());
      
        cnt2.addAll(type,description,date,reponse);
        cnt1.addAll(sl,cnt2);
         
        
       
            
       
        MultiButton ml = new MultiButton("");  
        ml.add(BorderLayout.SOUTH,cnt1);
         
        return ml;
        
        
     }
     
     
     
}
