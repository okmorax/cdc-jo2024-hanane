public class Main {

    public static void main(String[] args) {
        // Créer une instance de la bibliothèque
        Bibliotheque bibliotheque = new Bibliotheque();

        // Ajouter des livres, des adhérents, des auteurs, etc.
        bibliotheque.ajouterLivre(new Livre("ISBN123", "Titre Livre 1", 29.99));
        bibliotheque.ajouterAdherent(new Adherent("123", "John Doe"));

        // Effectuer des opérations de test
        bibliotheque.emprunterLivre("ISBN123", "123");
        bibliotheque.afficherLivresEmpruntes("123");

        // Vous pouvez ajouter plus d'opérations de test selon votre logique
    }
}
