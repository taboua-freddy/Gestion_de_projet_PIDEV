/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.services.activites;

import com.Feather.models.activite.Evenement;
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
 * @author Freddy
 */
public class EventsService {

    private ConnectionRequest request;

    private String urlBase = Statics.BASE_URL + "/api/event";
    private boolean responseResult;
    public ArrayList<Evenement> events;
    public String response;

    public EventsService() {
        request = DataSource.getInstance().getRequest();
        request.setTimeout(1000);
    }

    public ArrayList<Evenement> getAllEvents() {
        String url = urlBase + "/getAll";
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                System.err.println(response);
                events = parseTasks(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return events;
    }

    public ArrayList<Evenement> parseTasks(String jsonText) {
        try {
            events = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                int id = (int) Float.parseFloat(obj.get("idevent").toString());

                String titre = obj.get("titre").toString();
                String dateDebut = obj.get("datedebut").toString();
                String dateFin = obj.get("datefin").toString();
                String lieu = obj.get("lieu") != null ? obj.get("lieu").toString() : "";
                String affiche = obj.get("imageName") != null ? obj.get("imageName").toString() : "";
                String description = obj.get("description") != null ?obj.get("description").toString():"";

                Evenement ev = new Evenement();
                ev.setTitre(titre);
                ev.setId(id);
                ev.setDateDebut(Functions.jsonStringToDate(dateDebut));
                ev.setDateFin(Functions.jsonStringToDate(dateFin));
                ev.setLieu(lieu);
                ev.setAffiche(affiche);
                ev.setDescription(description);
                events.add(ev);
            }

        } catch (IOException ex) {
        }

        return events;
    }

    public boolean deleteEvent(int id) {
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

    public EventsService uploadEvent(Evenement ev) {
        
        String picture = ev.getAffiche();
        
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
                if (!picture.isEmpty() && picture.indexOf("tmp")!=-1) {
                    String imageName = ev.getTitre()+picture.substring(picture.indexOf('.')+1, picture.length());
                    requestM.addData("fileUpload", picture, "image/jpeg");
                    requestM.setFilename("fileUpload", imageName);
                }
                
                if(!String.valueOf(ev.getId()).isEmpty() && ev.getId()>0)requestM.addArgument("idEvent", String.valueOf(ev.getId()));
                requestM.addArgument("titre", ev.getTitre());
                requestM.addArgument("description", ev.getDescription());
                requestM.addArgument("dateDebut", String.valueOf(ev.getDateDebut().getTime()));
                requestM.addArgument("dateFin", String.valueOf(ev.getDateFin().getTime()));
                requestM.addArgument("lieu", ev.getLieu());
                InfiniteProgress prog = new InfiniteProgress();
                Dialog dlg = prog.showInifiniteBlocking();
                requestM.setDisposeOnCompletion(dlg);
                NetworkManager.getInstance().addToQueueAndWait(requestM);
                
            } catch (IOException err) {
                err.printStackTrace();
            }

        
        return this;
    }
    
}
