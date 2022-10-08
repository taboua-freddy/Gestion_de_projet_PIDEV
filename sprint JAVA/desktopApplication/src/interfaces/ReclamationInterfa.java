/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Date;
import javafx.collections.ObservableList;
import entity.reclamation.Reclamation;

/**
 *
 * @author Legion
 */
public interface ReclamationInterfa {
    public void addReclation(Reclamation rec);
    public void deleteReclamation(int id_rec);
    public void updateReclamation(Reclamation Rec, int id );
    public ObservableList<Reclamation>displayAll();
    public int nbReclamByIdUsr(int id);
    public ObservableList<Reclamation> findReclamById(int id);
    public int nbReclamTotal();
    public ObservableList<Reclamation> rechercherRec(Date dt);
}
