package com.Feather.services.sprint;

import com.Feather.models.sprint.Sprint;
import com.codename1.io.*;
import com.codename1.l10n.ParseException;
import com.codename1.ui.events.ActionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SprintService {
    public ArrayList<Sprint> parseSprintJson(String json) throws ParseException {

        ArrayList<Sprint> listSprints = new ArrayList<>();

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
                Sprint Sprint = new Sprint();

                float id = Float.parseFloat(obj.get("idsprint").toString());
                Map<String, Object> projet = (Map<String, Object>) obj.get("projet");
                float idProjet = Float.parseFloat(projet.get("idprojet").toString());
               // float moderator = Float.parseFloat(obj.get("moderator").toString());
                Sprint.setID((int)id);
                Sprint.setIdProjet((int)idProjet);
                Sprint.setNomSprint(obj.get("nomsprint").toString());
                Map<String, Object> datedebut = (Map<String, Object>) obj.get("datedebut");
                Sprint.setDateDebut(datedebut.get("date").toString());
                Map<String, Object> dateFin = (Map<String, Object>) obj.get("datefin");
                Sprint.setDateFin(dateFin.get("date").toString());
               

                listSprints.add(Sprint);

            }

        } catch (IOException ex) {
        }


        return listSprints;
    }

    ArrayList<Sprint> listSprints = new ArrayList<>();

    public ArrayList<Sprint> getSprints(int idProjet){
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost:8000/apiMob/sprints/"+idProjet);
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    listSprints = parseSprintJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listSprints;
    }

    public void add(String name, String dateDebut, String dateFin,int idProjet)
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost:8000/apiMob/addSprint/" + name+"/" + dateDebut + "/" + dateFin+ "/"+ idProjet ;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur
            System.out.println(dateDebut);

        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    public void delete(int id)
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost:8000/apiMob/delete_sprint/"+id;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur

        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }
    public void update(int id, String name, String dateDebut, String dateFin)
    {
        ConnectionRequest con = new ConnectionRequest();// création d'une nouvelle demande de connexion
        String Url = "http://localhost:8000/apiMob/updateSprint/" + id + "/" + name+"/" + dateDebut + "/" + dateFin;// création de l'URL
        con.setUrl(Url);// Insertion de l'URL de notre demande de connexion

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());//Récupération de la réponse du serveur

        });
        NetworkManager.getInstance().addToQueueAndWait(con);// Ajout de notre demande de connexion à la file d'attente du NetworkManager
    }





}
