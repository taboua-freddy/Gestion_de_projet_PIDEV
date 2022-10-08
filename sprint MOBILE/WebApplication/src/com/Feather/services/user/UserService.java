/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Feather.services.user;

import com.Feather.models.activite.Evenement;
import com.Feather.models.user.User;
import com.Feather.utils.DataSource;
import com.Feather.utils.Statics;
import com.Feather.utils.functions.Functions;
import com.codename1.components.InfiniteProgress;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Freddy
 */
public class UserService {
    private ConnectionRequest request;

    private String urlBase = Statics.BASE_URL + "/api/user";
    private boolean responseResult;
    public ArrayList<User> users;
    public User user;
    public String response;

    public UserService() {
        request = DataSource.getInstance().getRequest();
        request.setTimeout(1000);
    }

    public User getUser(String username,String password) {
        String url = urlBase + "/login/"+username+"/"+password;
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                //System.err.println(response);
                users = parseTasks(new String(request.getResponseData()));
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);
        
        return (users.isEmpty()?null:users.get(0));
    }
    
    public void getRole() {
        String url = urlBase + "/getRole/" + Statics.user.getIdUser();
        request.setUrl(url);
        request.setPost(false);
        request.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                response = new String(request.getResponseData());
                response = response.substring(1, response.length() - 1);
                Statics.user.setTypeUser(response);
                request.removeResponseListener(this);
            }
        });
        InfiniteProgress prog = new InfiniteProgress();
        Dialog dlg = prog.showInifiniteBlocking();
        request.setDisposeOnCompletion(dlg);
        NetworkManager.getInstance().addToQueueAndWait(request);

    }

    public ArrayList<User> parseTasks(String jsonText) {
        try {
            users = new ArrayList<>();

            JSONParser jp = new JSONParser();
            Map<String, Object> tasksListJson = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");
            for (Map<String, Object> obj : list) {
                int id = (int) Float.parseFloat(obj.get("id").toString());
                
                String nom = obj.get("nom").toString();
                String prenom = obj.get("prenom").toString();
                String typeUser = obj.get("typeuser").toString();

                User ev = new User();
                ev.setNom(nom);
                ev.setIdUser(id);
                ev.setPrenom(prenom);
                ev.setTypeUser(typeUser);
                users.add(ev);
            }

        } catch (IOException ex) {
        }

        return users;
    }
}
