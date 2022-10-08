/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.services.projet;

import com.Feather.services.activites.*;
import com.Feather.models.projet.Projet;
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
 * @author Aymen
 */
public class ProjectService {

    private ConnectionRequest request;

    private String urlBase = Statics.BASE_URL + "/api/projet";
    private boolean responseResult;
    public ArrayList<Projet> projets;
    public String response;

    public ProjectService() {
        request = DataSource.getInstance().getRequest();
        request.setTimeout(1000);
    }

    public ArrayList<Projet> getAllProjets() {
        String url = urlBase + "/getAll";
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                System.err.println(response);
                projets = parseTasks(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return projets;
    }

    public ArrayList<Projet> parseTasks(String jsonText) {
        try {
            projets = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                int id = (int) Float.parseFloat(obj.get("idprojet").toString());

                String nom = obj.get("nom").toString();
                String datedebut = obj.get("datedebut").toString();
                String datefin = obj.get("datefin").toString();
                String description = obj.get("description") != null ?obj.get("description").toString():"";
                String status = obj.get("status").toString();


                Projet ev = new Projet();
                ev.setNom(nom);
                ev.setIdProjet(id);
                ev.setDateDebut(Functions.jsonStringToDate(datedebut));
                ev.setDateFin(Functions.jsonStringToDate(datefin));
               
                ev.setStatus(Projet.status.stringToEnum(status));
                ev.setDescription(description);
                projets.add(ev);
            }

        } catch (IOException ex) {
        }

        return projets;
    }

    public boolean deleteProjet(int id) {
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

   

    public ProjectService addProjet(Projet ev) {
        
        
        
            String filestack = urlBase+"/add";
            MultipartRequest requestM = new MultipartRequest() {
                protected void readResponse(InputStream input) throws IOException {
                    JSONParser jp = new JSONParser();
                    Map<String, Object> result = jp.parseJSON(new InputStreamReader(input, "UTF-8"));
                    String url = (String) result.get("url");
                    if (url == null) {
                        System.err.println("erreur :"+result.toString());
                        return;
                    }
                    System.err.println("Succes :"+url);
                }
            };
            requestM.setUrl(filestack);
            requestM.setFailSilently(true);
            requestM.setTimeout(30000);
            try {
                
                
                if(!String.valueOf(ev.getIdProjet()).isEmpty() && ev.getIdProjet()>0)requestM.addArgument("idprojet", String.valueOf(ev.getIdProjet()));
                requestM.addArgument("nom", ev.getNom());
                requestM.addArgument("description", ev.getDescription());
                requestM.addArgument("datedebut", String.valueOf(ev.getDateDebut().getTime()));
                requestM.addArgument("datefin", String.valueOf(ev.getDateFin().getTime()));
                 requestM.addArgument("status", ev.getStatus().enumToString());
                InfiniteProgress prog = new InfiniteProgress();
                Dialog dlg = prog.showInifiniteBlocking();
                requestM.setDisposeOnCompletion(dlg);
                NetworkManager.getInstance().addToQueueAndWait(requestM);
            }catch(Exception err){
           
                err.printStackTrace();
            }

        
        return this;
    }
    
}
