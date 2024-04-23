import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {
    private List<LIVRE> livres;
    private List<ADHERENT> adherents;
    private List<AUTEUR> auteurs;

    public Bibliotheque() {
        livres = new ArrayList<>();
        adherents = new ArrayList<>();
        auteurs = new ArrayList<>();
    }

    // Méthodes pour gérer les livres
    public void ajouterLivre(LIVRE livre) {
        livres.add(livre);
    }

    public void emprunterLivre(LIVRE livre, ADHERENT adherent) {
        if (livre.isDisponible()) {
            livre.setDisponible(false);
            livre.setEmprunteur(adherent);
            adherent.ajouterLivre(livre);
            System.out.println("Emprunt réussi.");
        } else {
            System.out.println("Le livre n'est pas disponible pour l'emprunt.");
        }
    }

    public void retournerLivre(LIVRE livre) {
        livre.setDisponible(true);
        ADHERENT emprunteur = livre.getEmprunteur();
        if (emprunteur != null) {
            emprunteur.getListLivre().remove(livre);
            livre.setEmprunteur(null);
            System.out.println("Livre retourné avec succès.");
        } else {
            System.out.println("Le livre n'est pas emprunté.");
        }
    }

    // Méthodes pour gérer les adhérents
    public void ajouterAdherent(ADHERENT adherent) {
        adherents.add(adherent);
    }

    // Méthodes pour gérer les auteurs
    public void ajouterAuteur(AUTEUR auteur) {
        auteurs.add(auteur);
    }

    // Autres méthodes pour la gestion, la modification, la suppression, etc.

    // Méthode pour afficher les livres disponibles
    public void afficherLivresDisponibles() {
        System.out.println("Livres disponibles : ");
        for (LIVRE livre : livres) {
            if (livre.isDisponible()) {
                System.out.println(livre.getTitre());
            }
        }
    }
}
