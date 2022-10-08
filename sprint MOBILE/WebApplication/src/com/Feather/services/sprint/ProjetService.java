package com.Feather.services.sprint;

import com.Feather.models.projet.Projet;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.ui.events.ActionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjetService {
    public ArrayList<Projet> parseProjectJson(String json) throws ParseException {

        ArrayList<Projet> listProjets = new ArrayList<>();

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
            En fait projet'est la clé de l'objet qui englobe la totalité des objets
                    projet'est la clé définissant le tableau de tâches.
            */
            Map<String, Object> posts = j.parseJSON(new CharArrayReader(json.toCharArray()));


            /* Ici on récupère l'objet contenant notre liste dans une liste
            d'objets json List<MAP<String,Object>> ou chaque Map est une tâche
            */
            List<Map<String, Object>> list = (List<Map<String, Object>>) posts.get("root");

            //Parcourir la liste des tâches Json
            for (Map<String, Object> obj : list) {
                //Création des tâches et récupération de leurs données
                Projet projet = new Projet();

                float id = Float.parseFloat(obj.get("idprojet").toString());
               // float moderator = Float.parseFloat(obj.get("moderator").toString());
                projet.setIdProjet((int)id);
                projet.setNom(obj.get("nom").toString());

                listProjets.add(projet);

            }

        } catch (IOException ex) {
        }


        return listProjets;
    }

    ArrayList<Projet> listClubs = new ArrayList<>();

    public ArrayList<Projet> getProjets(){
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost:8000/apiMob/projects");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                    listClubs = parseProjectJson(new String(con.getResponseData()));
                } catch (ParseException ex) {
                    System.out.println(ex);
                }
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listClubs;
    }





}
