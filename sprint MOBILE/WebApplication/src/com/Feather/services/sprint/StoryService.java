package com.Feather.services.sprint;

import com.Feather.models.sprint.Sprint;
import com.Feather.models.sprint.Story;
import com.codename1.io.*;
import com.codename1.l10n.ParseException;
import com.codename1.ui.events.ActionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoryService {
    public ArrayList<Story> parseStoryJson(String json) throws ParseException {

        ArrayList<Story> liststories = new ArrayList<>();

        try {
            JSONParser j = new JSONParser();// Instanciation d'un objet JSONParser permettant le parsing du résultat json

            /*
                On doit convertir notre réponse texte en CharArray à fin de
            permettre au JSONParser de la lire et la manipuler d'ou vient
            l'utilité de new CharArrayReader(json.toCharArray())

            La méthode parse json retourne une MAP<String,Object> ou String est
            la clé principale de notre résultat.
            Dans notre cas la clé principale n'est pas définie cela ne veux pas
            dire qu'elle est manquante mais plutôt gardée à la valeur par defaut
            qui est root.
            En fait Sprint'est la clé de l'objet qui englobe la totalité des objets
                    Sprint'est la clé définissant le tableau de tâches.
            */
            Map<String, Object> posts = j.parseJSON(new CharArrayReader(json.toCharArray()));


            /* Ici on récupère l'objet contenant notre liste dans une liste
            d'objets json List<MAP<String,Object>> ou chaque Map est une tâche
            */
            List<Map<String, Object>> list = (List<Map<String, Object>>) posts.get("root");

            //Parcourir la liste des tâches Json
            for (Map<String, Object> obj : list) {
                //Création des tâches et récupération de leurs données
                Story story = new Story();

                float id = Float.parseFloat(obj.get("idstory").toString());
               // float moderator = Float.parseFloat(obj.get("moderator").toString());
                story.setIdStory((int)id);
                story.setNomStory(obj.get("nomstory").toString());
                story.setStatus(obj.get("status").toString());
                story.setDescription(obj.get("description").toString());
                story.setBV((int)Float.parseFloat(obj.get("bv").toString()));
                story.setPriorite((int)Float.parseFloat(obj.get("priorite").toString()));
                story.setCap((int)Float.parseFloat(obj.get("c").toString()));
                story.setComplexite((int)Float.parseFloat(obj.get("complexite").toString()));
                story.setROI((int)Float.parseFloat(obj.get("roi").toString()));
             
                liststories.add(story);

            }

        } catch (IOException ex) {
        }


        return liststories;
    }

    ArrayList<Story> listStories = new ArrayList<>();
    int count =0; 

    public ArrayList<Story> getStories(int idSprint){
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost:8000/apiMob/stories/"+idSprint);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    listStories = parseStoryJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listStories;
    }
     public int getCount(String type){
         int value=0;
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost:8000/apiMob/countStatus/"+type);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                     String str = new String(con.getResponseData());
                    count = Integer.valueOf(str);
                   //return parseStoryJson(new String(con.getResponseData()));
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return count;
    }

    public void add(String name, String bv, String cap,String complexite,String roi,String description,String priorite,String status,int idProjet,int idSprint)
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost:8000/apiMob/addStory/" + name+ "/" + bv + "/" + cap+ "/"+ complexite + "/" + roi+ "/" +priorite+"/" +status+ "/" + description+  "/" + idProjet+ "/" + idSprint;
        System.out.println(Url);
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur

        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    public void delete(int id)
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost:8000/apiMob/delete_story/"+id;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur

        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    public void update(String name, String bv, String cap,String complexite,String roi,String description,String priorite,String status,int idStory)
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost:8000/apiMob/updateStory/" + name+ "/" + bv + "/" + cap+ "/"+ complexite + "/" + roi+ "/" +priorite+"/" +status+ "/" + description+  "/" + idStory;
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur

        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }





}
