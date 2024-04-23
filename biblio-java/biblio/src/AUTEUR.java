public class AUTEUR {
    private String autnum;
    private String nom;
    private String prenom;
    private String date_naissance;
    private String description;
    private int id; // Nouvelle variable pour l'identifiant

    public AUTEUR(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public AUTEUR(String nom, String prenom, String date_naissance, String description) {
        this.nom = nom;
        this.prenom = prenom;
        this.date_naissance = date_naissance;
        this.description = description;
    }

    public AUTEUR(String autnum, String nom, String prenom, String date_naissance, String description) {
        this.autnum = autnum;
        this.nom = nom;
        this.prenom = prenom;
        this.date_naissance = date_naissance;
        this.description = description;
    }

    public AUTEUR() {
        autnum = "";
        nom = "";
        prenom = "";
        date_naissance = "";
        description = "";
    }

    public String getAutnum() {
        return autnum;
    }

    public void setAutnum(String autnum) {
        this.autnum = autnum;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    // Nouvelles méthodes pour l'identifiant
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
