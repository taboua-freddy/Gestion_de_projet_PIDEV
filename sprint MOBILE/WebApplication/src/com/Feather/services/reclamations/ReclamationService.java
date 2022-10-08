/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.services.reclamations;



import com.Feather.models.reclamation.Reclamation;
import com.Feather.models.user.User;
import com.Feather.utils.DataSource;
import com.Feather.utils.Statics;
import com.Feather.utils.functions.Functions;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




/**
 *
 * @author Legion
 */
public class ReclamationService {
    
    private ConnectionRequest request;
    private ConnectionRequest requestM;
    private String urlBase = Statics.BASE_URL + "/api/reclamation";
    private boolean responseResult;
    public ArrayList<Reclamation> reclamations;
    public String response;
    public ArrayList<User> users;
   
    public ReclamationService() {
        request = DataSource.getInstance().getRequest();
        requestM = DataSource.getInstance().getRequest();
        request.setTimeout(1000);
    }
    
    public ArrayList<Reclamation> getAllReclams() {
        String url = urlBase+ "/getAllrec";
        
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
              response = new String(request.getResponseData());
              reclamations = parseReclams((new String(request.getResponseData())));
              System.err.println(response);
               request.removeResponseListener(this);
            }
        });
        
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return reclamations;
    }
    
    
     public ArrayList<Reclamation> parseReclams(String jsonText)  {
         try {
            reclamations = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                 int idRec = (int) Float.parseFloat(obj.get("idrec").toString());
                
                 Reclamation rec = new Reclamation();
                 
                Map<String, Object> Userr = (Map<String, Object>) obj.get("user");
                if(Userr!=null && !Userr.isEmpty())
                {
                    User c = new User();
                    int iduser = (int) Float.parseFloat(Userr.get("id").toString());
                    String nomuser = Userr.get("nom").toString();
                    String prenomuser = Userr.get("prenom").toString();
                    c = new User(iduser,nomuser,prenomuser, "");
                    rec.setUser(c);
                }
                 String description = obj.get("description").toString();
                 int etat = (int) Float.parseFloat(obj.get("etat").toString());
                 String dateRec = obj.get("daterec").toString();
                 Object reponse = obj.get("reponse");
                 String reponsee;
                 if (reponse ==null){
                 reponsee="pas de reponse";
                 }else{
                 reponsee=reponse.toString();
                 }
                 
                 String typeRec = obj.get("typerec").toString();
                
                rec.setIdRec(idRec);
               
                rec.setDescription(description);
                rec.setEtat(etat);
                rec.setDateRec(Functions.jsonStringToDate(dateRec));
                rec.setReponse(reponsee);
                rec.settypeRec(typeRec);
                reclamations.add(rec);
             
            }
            
              } catch (IOException ex) {
        }
         return reclamations ;
     }
     
     
     public boolean deleteReclam(int id) {
        String url = urlBase + "/delete/" + id;
        request.setUrl(url);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                responseResult = request.getResponseCode() == 200; // Code HTTP 200 OK
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return responseResult;
    }
    public ReclamationService uploadReclam(Reclamation rec){
       String url = urlBase+"/addRec";
        
            requestM.setUrl(url);
            requestM.setFailSilently(true);
            requestM.setTimeout(30000);
           
            requestM.addArgument("idUser",String.valueOf(Statics.user.getIdUser()));
            requestM.addArgument("description", rec.getDescription());
            requestM.addArgument("typeRec", rec.gettypeRec());
            InfiniteProgress prog = new InfiniteProgress();
            Dialog dlg = prog.showInifiniteBlocking();
            requestM.setDisposeOnCompletion(dlg);
            NetworkManager.getInstance().addToQueueAndWait(requestM);
            return this;
    }
   
}
