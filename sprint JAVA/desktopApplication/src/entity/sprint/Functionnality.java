package entity;

public class Functionnality {
    private int idFn;
    private int idSprint;
    private String nomFn;
    private int priorite;

    public Functionnality(int idFn, int idSprint, String nomFn, int priorite) {
        this.idFn = idFn;
        this.idSprint = idSprint;
        this.nomFn = nomFn;
        this.priorite = priorite;
    }

    public Functionnality(int idFn,String nomFn, int priorite) {
        this.idFn = idFn;
        this.nomFn = nomFn;
        this.priorite = priorite;
    }

    public Functionnality() {
    }

    public Functionnality(int anInt, int anInt1, String string, String toString, String toString1) {
    }

    public int getIdFn() {
        return idFn;
    }

    public void setIdFn(int idFn) {
        this.idFn = idFn;
    }

    public int getIdSprint() {
        return idSprint;
    }

    public void setIdSprint(int idSprint) {
        this.idSprint = idSprint;
    }

    public String getNomFn() {
        return nomFn;
    }

    public void setNomFn(String nomFn) {
        this.nomFn = nomFn;
    }

    public int getPriorite() {
        return priorite;
    }

    public void setPriorite(int priorite) {
        this.priorite = priorite;
    }

    @Override
    public String toString() {
        return "Fonctionnality{" +
                "idFn=" + idFn +
                ", idSprint=" + idSprint +
                ", nomFn='" + nomFn + '\'' +
                ", priorite=" + priorite +
                '}';
    }
}
