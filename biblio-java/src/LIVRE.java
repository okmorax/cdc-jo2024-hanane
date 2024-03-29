import java.util.ArrayList;

public class LIVRE {
    private String ISBN;
    private String titre;
    private int prix;
    private ADHERENT Emprunteur;
    private AUTEUR Auteur;
    private boolean disponible = true;

    public LIVRE(String iSBN, String titre, int prix) {
        super();
        ISBN = iSBN;
        this.titre = titre;
        this.prix = prix;
    }

    public LIVRE(String iSBN, String titre, int prix, AUTEUR auteur) {
        super();
        ISBN = iSBN;
        this.titre = titre;
        this.prix = prix;
        Auteur = auteur;
    }

    public LIVRE(String iSBN, String titre, int prix, ADHERENT emprunteur, AUTEUR auteur) {
        super();
        ISBN = iSBN;
        this.titre = titre;
        this.prix = prix;
        Emprunteur = emprunteur;
        Auteur = auteur;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public ADHERENT getEmprunteur() {
        return Emprunteur;
    }

    public void setEmprunteur(ADHERENT emprunteur) {
        Emprunteur = emprunteur;
    }

    public AUTEUR getAuteur() {
        return Auteur;
    }

    public void setAuteur(AUTEUR auteur) {
        Auteur = auteur;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void AFFICHER() {
        System.out.println("Voici les info du livre n�" + ISBN);
        System.out.println("titre : " + titre);
        System.out.println("prix : " + prix);
        if (Auteur == null)
            System.out.println("Auteur inconnu");
        else
            System.out.println("nom Auteur :" + Auteur.getNom());
        if (Emprunteur == null)
            System.out.println("Livre dispo");
        else {
            System.out.println("Livre emprunt� par : " + Emprunteur.getNom());
        }
    }

    public String Ligne() {
        String str;
        str = ISBN + ":'" + titre + "'";
        if (Auteur == null)
            str = str + " de " + "Auteur inconnu";
        else
            str = str + " de " + Auteur.getNom();
        if (Emprunteur == null)
            str = str + " - " + "dispo";
        else {
            str = str + " - " + "Non dispo";
        }
        return str;
    }
}
