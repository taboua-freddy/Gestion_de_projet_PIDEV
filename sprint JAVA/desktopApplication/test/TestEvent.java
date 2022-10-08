
import entity.activite.Evenement;
import entity.pojet.Projet;
import java.time.LocalDateTime;
import service.activiteService.EventService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Freddy
 */
public class TestEvent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Projet p1 = new Projet(1);
        Projet p2 = new Projet(2);*/
        
        EventService es = new EventService();
        
        Evenement e1 = new Evenement("tunis", "balle", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "presentation des projets 2018",null,null);
        
       /* es.displayAll().forEach(System.out::println);
        
        int idEvent = es.insertIdReturn(e1);
        
        es.delete(idEvent);
        
        idEvent = es.insertIdReturn(e1);
        
        es.insertProjetToEvent(1,idEvent);
        es.insertProjetToEvent(2,idEvent);
        
        es.displayProjetsEvenement(idEvent).forEach(System.out::println);
        
        es.deleteProjetToEvent(idEvent, 1);
        
        es.displayProjetsEvenement(idEvent).forEach(System.out::println);*/
        Evenement evenement = new Evenement();
            evenement.setTitre("qsdq");
            evenement.setDateDebut(LocalDateTime.now());
            evenement.setDateFin(LocalDateTime.now().plusDays(2));
            evenement.setDateRappel(null);
            evenement.setDescription("qsdqs");
            evenement.setLieu("qdqs");
            evenement.setAffiche(null);
            es.insertIdReturn(evenement,null);
    }
    
}
