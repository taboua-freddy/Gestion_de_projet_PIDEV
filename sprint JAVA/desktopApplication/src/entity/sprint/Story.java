package entity;

import javafx.scene.control.Button;

public class Story {
    private int idStory;
    private int idProjet;

    public Story(
                 int idProjet,
                 String nomStory,
                 int BV,
                 int cap,
                 int priorite,
                 float complexite,
                 String description,
                 float ROI) {
        this.idProjet = idProjet;
        this.nomStory = nomStory;
        this.BV = BV;
        this.cap = cap;
        this.priorite = priorite;
        this.complexite = complexite;
        this.description = description;
        this.ROI = ROI;
    }


    public Story(int idProjet,String nomStory, int BV, int cap, int priorite, float complexite, String description, float ROI, Button update) {
        this.nomStory = nomStory;
        this.idProjet = idProjet;
        this.BV = BV;
        this.cap = cap;
        this.priorite = priorite;
        this.complexite = complexite;
        this.description = description;
        this.ROI = ROI;
        this.update = update;
    }

    private String nomStory;
    private int BV;
    private int cap;
    private int priorite;
    private float complexite;
    private String description;
    private float ROI;
    Button update;







    public Button getUpdate() {
        return update;
    }

    public void setUpdate(Button update) {
        this.update = update;
    }
    public int getIdStory() {
        return idStory;
    }

    public void setIdStory(int idStory) {
        this.idStory = idStory;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public String getNomStory() {
        return nomStory;
    }

    public void setNomStory(String nomStory) {
        this.nomStory = nomStory;
    }

    public int getBV() {
        return BV;
    }

    public void setBV(int BV) {
        this.BV = BV;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public float getComplexite() {
        return complexite;
    }

    public void setComplexite(float complexite) {
        this.complexite = complexite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getROI() {
        return ROI;
    }

    public void setROI(float ROI) {
        this.ROI = ROI;
    }



    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    @Override
    public String toString() {
        return "Story{" +
                "idStory=" + idStory +
                ", idProjet=" + idProjet +
                ", nomStory='" + nomStory + '\'' +
                ", BV=" + BV +
                ", cap=" + cap +
                ", complexite=" + complexite +
                ", description='" + description + '\'' +
                ", ROI=" + ROI +
                '}';
    }
}
