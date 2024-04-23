public class Emprunt {
    private LIVRE livreEmprunte;
    private ADHERENT emprunteur;

    public Emprunt(LIVRE livreEmprunte, ADHERENT emprunteur) {
        this.livreEmprunte = livreEmprunte;
        this.emprunteur = emprunteur;
    }

    public void emprunterLivre() {
        if (livreEmprunte.isDisponible()) {
            livreEmprunte.setDisponible(false);
            livreEmprunte.setEmprunteur(emprunteur);
            emprunteur.ajouterLivreEmprunte(livreEmprunte);
            System.out.println("Emprunt réussi.");
        } else {
            System.out.println("Le livre n'est pas disponible pour l'emprunt.");
        }
    }

    public void retournerLivre() {
        if (livreEmprunte.isDisponible()) {
            System.out.println("Le livre n'est pas emprunté.");
        } else {
            livreEmprunte.setDisponible(true);
            emprunteur.retirerLivreEmprunte(livreEmprunte);
            livreEmprunte.setEmprunteur(null);
            System.out.println("Livre retourné avec succès.");
        }
    }
}
