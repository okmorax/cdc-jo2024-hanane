import java.util.ArrayList;

// Définition de la classe LIVRE
public class LIVRE {
    private String ISBN; // Numéro ISBN du livre
    private String titre; // Titre du livre
    private int prix; // Prix du livre
    private ADHERENT emprunteur; // Adhérent qui a emprunté le livre
    private AUTEUR auteur; // Auteur du livre
    private boolean disponible = true; // Indique si le livre est disponible

    // Constructeur prenant ISBN, titre et prix
    public LIVRE(String ISBN, String titre, int prix) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.prix = prix;
    }

    // Constructeur prenant ISBN, titre, prix et auteur
    public LIVRE(String ISBN, String titre, int prix, AUTEUR auteur) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.prix = prix;
        this.auteur = auteur;
    }

    // Constructeur prenant ISBN, titre, prix, emprunteur et auteur
    public LIVRE(String ISBN, String titre, int prix, ADHERENT emprunteur, AUTEUR auteur) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.prix = prix;
        this.emprunteur = emprunteur;
        this.auteur = auteur;
    }

    // Getter pour ISBN
    public String getISBN() {
        return ISBN;
    }

    // Setter pour ISBN
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    // Getter pour titre
    public String getTitre() {
        return titre;
    }

    // Setter pour titre
    public void setTitre(String titre) {
        this.titre = titre;
    }

    // Getter pour prix
    public int getPrix() {
        return prix;
    }

    // Setter pour prix
    public void setPrix(int prix) {
        this.prix = prix;
    }

    // Getter pour emprunteur
    public ADHERENT getEmprunteur() {
        return emprunteur;
    }

    // Setter pour emprunteur
    public void setEmprunteur(ADHERENT emprunteur) {
        this.emprunteur = emprunteur;
    }

    // Getter pour auteur
    public AUTEUR getAuteur() {
        return auteur;
    }

    // Setter pour auteur
    public void setAuteur(AUTEUR auteur) {
        this.auteur = auteur;
    }

    // Getter pour disponible
    public boolean isDisponible() {
        return disponible;
    }

    // Setter pour disponible
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Méthode pour afficher les informations du livre
    public void afficher() {
        System.out.println("Voici les informations du livre numéro " + ISBN);
        System.out.println("Titre : " + titre);
        System.out.println("Prix : " + prix);
        if (auteur == null)
            System.out.println("Auteur inconnu");
        else
            System.out.println("Nom de l'auteur : " + auteur.getNom());
        if (emprunteur == null)
            System.out.println("Livre disponible");
        else {
            System.out.println("Livre emprunté par : " + emprunteur.getNom());
        }
    }

    // Méthode pour obtenir une ligne de description du livre
    public String ligne() {
        String str;
        str = ISBN + " : '" + titre + "'";
        if (auteur == null)
            str = str + " de Auteur inconnu";
        else
            str = str + " de " + auteur.getNom();
        if (emprunteur == null)
            str = str + " - disponible";
        else {
            str = str + " - Non disponible";
        }
        return str;
    }
}
