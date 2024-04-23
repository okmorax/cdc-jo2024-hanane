import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GestionBiblio {
    private JFrame frame;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Création de l'instance de l'application principale
                    GestionBiblio window = new GestionBiblio();
                    // Initialisation de l'application
                    window.initialize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Méthode pour initialiser l'interface utilisateur
    public void initialize() {
        // Création de la fenêtre principale
        frame = new JFrame("Gestion de la Bibliothèque");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création d'un panneau pour contenir les boutons
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(5, 1));

        // Création des boutons pour chaque fonctionnalité
        JButton btnGestionAuteur = new JButton("Gestion des Auteurs");
        btnGestionAuteur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lorsque le bouton est cliqué, ouvrir la fenêtre de gestion des auteurs
                GestionAuteursUI gestionAuteursUI = new GestionAuteursUI();
                gestionAuteursUI.getFrame().setVisible(true);
                // Ajouter un WindowListener pour gérer la visibilité de la fenêtre principale
                addWindowListenerToChildFrame(gestionAuteursUI.getFrame());
            }
        });
        panel.add(btnGestionAuteur);

        JButton btnGestionLivre = new JButton("Gestion des Livres");
        btnGestionLivre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lorsque le bouton est cliqué, ouvrir la fenêtre de gestion des livres
                GestionLivresUI gestionLivresUI = new GestionLivresUI();
                gestionLivresUI.getFrame().setVisible(true);
                // Ajouter un WindowListener pour gérer la visibilité de la fenêtre principale
                addWindowListenerToChildFrame(gestionLivresUI.getFrame());
            }
        });
        panel.add(btnGestionLivre);

        JButton btnGestionAdherent = new JButton("Gestion des Adhérents");
        btnGestionAdherent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lorsque le bouton est cliqué, ouvrir la fenêtre de gestion des adhérents
                GestionAdherentsUI gestionAdherentsUI = new GestionAdherentsUI();
                gestionAdherentsUI.getFrame().setVisible(true);
                // Ajouter un WindowListener pour gérer la visibilité de la fenêtre principale
                addWindowListenerToChildFrame(gestionAdherentsUI.getFrame());
            }
        });
        panel.add(btnGestionAdherent);

        JButton btnGestionEmprunt = new JButton("Gestion des Emprunts");
        btnGestionEmprunt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lorsque le bouton est cliqué, ouvrir la fenêtre de gestion des emprunts
                GestionEmpruntUI gestionEmpruntUI = new GestionEmpruntUI();
                gestionEmpruntUI.getFrame().setVisible(true);
                // Ajouter un WindowListener pour gérer la visibilité de la fenêtre principale
                addWindowListenerToChildFrame(gestionEmpruntUI.getFrame());
            }
        });
        panel.add(btnGestionEmprunt);

        JButton btnGestionRetour = new JButton("Gestion des Retours");
        btnGestionRetour.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lorsque le bouton est cliqué, ouvrir la fenêtre de gestion des retours
                GestionRetourUI gestionRetourUI = new GestionRetourUI();
                gestionRetourUI.getFrame().setVisible(true);
                // Ajouter un WindowListener pour gérer la visibilité de la fenêtre principale
                addWindowListenerToChildFrame(gestionRetourUI.getFrame());
            }
        });
        panel.add(btnGestionRetour);

        // Rendre la fenêtre principale visible
        frame.setVisible(true);
    }

    // Méthode pour ajouter un WindowListener à une fenêtre enfant
    private void addWindowListenerToChildFrame(JFrame childFrame) {
        childFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // Rendre la fenêtre principale visible lorsque la fenêtre enfant se ferme
                frame.setVisible(true);
            }
        });
    }
}
