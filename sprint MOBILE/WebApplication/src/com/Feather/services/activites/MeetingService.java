/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.services.activites;

import com.Feather.models.activite.Objectif;
import com.Feather.models.activite.Reunion;
import com.Feather.models.groupe.Groupe;
import com.Feather.models.projet.Projet;
import com.Feather.models.sprint.Sprint;
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
 * @author Freddy
 */
public class MeetingService {
    private ConnectionRequest request;
    private ConnectionRequest requestP;

    private String urlBase = Statics.BASE_URL + "/api/meeting";
    private boolean responseResult;
    public ArrayList<Reunion> reunions;
    public ArrayList<Projet> projets;
    public ArrayList<Sprint> sprints;
    public ArrayList<Objectif> objectifs;
    public ArrayList<User> users;
    public String response;

    public MeetingService() {
        request = DataSource.getInstance().getRequest();
        requestP = DataSource.getInstance().getRequest();
        request.setTimeout(1000);
    }
    
    public String getLinkTokenCalendar() {
        String url = urlBase + "/link_token";
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);
        response = response.substring(1, response.length()-1);
        return response;
    }
    
    public MeetingService uploadMeeting(Reunion reunion) {
        
            String url = urlBase+"/add/"+String.valueOf(Statics.user.getIdUser());
            
            requestP.setUrl(url);
            //requestP.setPost(true);
            //requestM.setFailSilently(true);
            request.setTimeout(1000);
            
            if(reunion.getId()>0)requestP.addArgument("idReunion", String.valueOf(reunion.getId()));
            requestP.addArgument("titre", reunion.getTitre());
            requestP.addArgument("description", reunion.getDescription());
            requestP.addArgument("dateDebut", String.valueOf(reunion.getDateDebut().getTime()));
            requestP.addArgument("dateFin", String.valueOf(reunion.getDateFin().getTime()));
            requestP.addArgument("coordonateur", String.valueOf(reunion.getCoordonateur().getIdUser()));
            requestP.addArgument("sprint", String.valueOf(reunion.getSprint().getID()));
            int taille = 0;
            taille = reunion.getParticipants().size();
            String p = "";
            for (int i = 0; i < taille; i++) {
                p += String.valueOf(reunion.getParticipants().get(i));
                if(i!=taille-1)
                    p+=" ";
            }
            
            requestP.addArgument("participants",p) ;
            requestP.addArgument("theme", reunion.getThemeDuJour().toString());
            requestP.addArgument("importance", String.valueOf(reunion.getImportance().getValue()));
            taille = reunion.getObjectifs().size();
            String[] obj = new String[taille];
            for (int i = 0; i < taille; i++) {
                obj[i] = reunion.getObjectifs().get(i).getObjectif().toString();
            }
            
            request.addArgument("objectifs",obj);
            
            InfiniteProgress prog = new InfiniteProgress();
            Dialog dlg = prog.showInifiniteBlocking();
            requestP.setDisposeOnCompletion(dlg);
            NetworkManager.getInstance().addToQueueAndWait(requestP);

        
        return this;
    }
    
    public boolean deleteMeeting(int id) {
        String url = urlBase + "/delete/" + id +"/"+ String.valueOf(Statics.user.getIdUser());
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
    
    public ArrayList<Reunion> getAllMeetings() {
        String url = urlBase + "/get_allMeetings/"+Statics.user.getIdUser();
        request.setUrl(url);
        request.setPost(false);
        request.setTimeout(5000);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                reunions = parseMeetings(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return reunions;
    }
    
    public ArrayList<Projet> getAllProjets() {
        String url = urlBase + "/get_projets";
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                projets = parseProjets(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return projets;
    }
    public ArrayList<User> getAllUsers() {
        String url = urlBase + "/get_users";
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                users = parseUsers(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return users;
    }
    
    public ArrayList<User> getAllParticipants(int idReunion) {
        String url = urlBase + "/get_participants/"+String.valueOf(idReunion);
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                users = parseUsers(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        /*InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
*/
        NetworkManager.getInstance().addToQueueAndWait(request);

        return users;
    }
    
    public ArrayList<Sprint> getAllSprints(int idProjet) {
        String url = urlBase + "/get_sprints/"+String.valueOf(idProjet);
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                sprints = parseSprints(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

        return sprints;
    }
    
    public ArrayList<Objectif> getObjectifs(int idReunion) {
        String url = urlBase + "/get_objectifs/"+String.valueOf(idReunion);
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                objectifs = parseObjectifs(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(request);

        return objectifs;
    }
    
    public ArrayList<Reunion> parseMeetings(String jsonText) {
        try {
            reunions = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                int id = (int) Float.parseFloat(obj.get("idreunion").toString());

                String titre = obj.get("titre").toString();
                String dateDebut = obj.get("datedebut").toString();
                String dateFin = obj.get("datefin").toString();
                String theme = obj.get("themedujour") != null ? obj.get("themedujour").toString() : "";
                String description = obj.get("description") != null ?obj.get("description").toString():"";
                int importance = (int) Float.parseFloat(obj.get("importance").toString());
                
                Reunion r = new Reunion();
                Map<String, Object> sprint = (Map<String, Object>) obj.get("sprint");
                
                if(sprint!=null &&!sprint.isEmpty())
                {
                    int idSprint = (int) Float.parseFloat(sprint.get("idsprint").toString());
                    String nomSpr = sprint.get("nomsprint").toString();
                    Sprint s = new Sprint();
                    s.setID(idSprint);
                    s.setNomSprint(nomSpr);
                    r.setSprint(s);
                }
                else
                    r.setSprint(null);
                
                Map<String, Object> coordo = (Map<String, Object>) obj.get("coordonateur");
                if(coordo!=null && !coordo.isEmpty())
                {
                    User c = new User();
                    int idCoordo = (int) Float.parseFloat(coordo.get("id").toString());
                    String nomCoordo = coordo.get("nom").toString();
                    String prenomCoordo = coordo.get("prenom").toString();
                    c = new User(idCoordo, nomCoordo, prenomCoordo, "");
                    r.setCoordonateur(c);
                }
                else
                    r.setCoordonateur(null);
                
                
                
                r.setTitre(titre);
                r.setId(id);
                r.setDateDebut(Functions.jsonStringToDate(dateDebut));
                r.setDateFin(Functions.jsonStringToDate(dateFin));
                r.setDescription(description);
                r.setImportance(Functions.getImportance(importance));
                r.setThemeDuJour(theme);
                reunions.add(r);
            }

        } catch (IOException ex) {
        }

        return reunions;
    }
    
    public ArrayList<Projet> parseProjets(String jsonText) {
        try {
            projets = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                int id = (int) Float.parseFloat(obj.get("idprojet").toString());

                String nom = obj.get("nom").toString();
                projets.add(new Projet(id, nom));
            }

        } catch (IOException ex) {
        }

        return projets;
    }
    public ArrayList<Sprint> parseSprints(String jsonText) {
        try {
            sprints = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                int id = (int) Float.parseFloat(obj.get("idsprint").toString());

                String nom = obj.get("nomsprint").toString();

                Sprint r = new Sprint();
                r.setNomSprint(nom);
                r.setID(id);
                sprints.add(r);
            }

        } catch (IOException ex) {
        }

        return sprints;
    }
    public ArrayList<Objectif> parseObjectifs(String jsonText) {
        try {
            objectifs = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                //int id = (int) Float.parseFloat(obj.get("idsprint").toString());
                
                if(obj.get("objectif")!=null)
                {
                    String nom = obj.get("objectif").toString();

                    Objectif r = new Objectif();
                    r.setObjectif(nom);
                    objectifs.add(r);
                }
                
            }

        } catch (IOException ex) {
        }

        return objectifs;
    }
    public ArrayList<User> parseUsers(String jsonText) {
        try {
            users = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                int id;
                if(obj.get("iduser")!=null)
                    id = (int) Float.parseFloat(obj.get("iduser").toString());
                else
                    id = (int) Float.parseFloat(obj.get("id").toString());
                
                String nom = obj.get("nom").toString();
                String prenom = obj.get("prenom").toString();
                String groupe = (obj.get("groupe")!=null)?obj.get("groupe").toString():"";
                
                int presence = (obj.get("presence")!=null)?(int) Float.parseFloat(obj.get("presence").toString()):-1;
                System.err.println(presence);
                User r = new User();
                r.setNom(nom);
                r.setPrenom(prenom);
                r.setIdUser(id);
                r.setIdGroupe(new Groupe(groupe));
                r.setPresence(presence);
                users.add(r);
            }

        } catch (IOException ex) {
        }

        return users;
    }

    public boolean addToCalendar(String token, String type, int id) {
       
        String url = urlBase + "/add_google_calendar/"+Statics.user.getIdUser()+"/" +type+"/"+String.valueOf(id)+"?token="+token ;
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

    public boolean changePresence(int idReunion, int idUser) {
        String url = urlBase + "/change_presence/"+String.valueOf(idReunion)+"/"+Statics.user.getIdUser() ;
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
    
}
