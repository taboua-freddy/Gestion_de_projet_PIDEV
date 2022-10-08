/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.forms.reclamations;
import com.Feather.forms.main.BaseForm;
import com.Feather.models.reclamation.Reclamation;
import com.Feather.services.reclamations.ReclamationService;
import com.Feather.utils.Statics;
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
import com.codename1.ui.TextComponent;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.TextModeLayout;
import com.codename1.ui.util.Resources;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.Validator;



/**
 *
 * @author Legion
 */
public class AddReclamation extends BaseForm {
    private Reclamation reclam=null;
    private ReclamationService rs = new ReclamationService();
    
    
    public AddReclamation(ReclamationForm parent, Resources theme,Reclamation reclam){
        super(theme, "Programmer", new BorderLayout());
        this.reclam=reclam;
        setBgColor(this, 0xdcdde1, false);
        

        display(parent);

        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            parent.showBack();
        });
    }
    
    private void display(ReclamationForm parent){
        TextModeLayout tm = new TextModeLayout(5, 2);
        Container container = new Container(tm);
               
         container.add(tm.createConstraint().horizontalSpan(2), new SpanLabel("Ajouter votre Reclamation "));
        
        ComboBox<String> type = new ComboBox<>(Functions.getTypeReclam());
        Container c = addLabelToItem(type, "type de reclamation");
        
        setBgColor(type, 0xffffff, true);
        setBgColor(c, 0xffffff, true);
        container.add(tm.createConstraint().horizontalSpan(2), c);
        
        TextComponent description = new TextComponent().labelAndHint("Description").multiline(true).rows(3);
        container.add(tm.createConstraint().horizontalSpan(2), description);
        
        if(reclam!=null)
        {
           type.setSelectedItem(reclam.gettypeRec());
           description.text(reclam.getDescription());
           
        }
        Button submit = new Button("Sauvegarder");
        FontImage.setMaterialIcon(submit, FontImage.MATERIAL_DONE);
        submit.addActionListener(e -> {
           
           
                //button Sauvegarder
                
                
                String ti = type.getSelectedItem();
                String desc = description.getText();
                Reclamation rc = (reclam == null) ? new Reclamation() : reclam;
                
                rc.setUser(Statics.user);
                rc.setDescription(desc);
                
               
                rc.settypeRec(ti);
                rs.uploadReclam(rc);
                if (Dialog.show("Confirmation", ((reclam == null) ? "Ajouter" : "Modifié") + " avec succes", "ok", null)) {
                    parent.removeAll();
                    parent.displayReclamations();
                    parent.showBack();
                }
          
            
        });
         this.add(CENTER, container);
        this.add(SOUTH, submit);
        container.setScrollableY(true);
        Validator val = new Validator();
        val.addConstraint(description, new LengthConstraint(2, "Obligatoire")).
                
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
