import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionAuteursUI {
    private List<AUTEUR> auteurs;
    private JFrame frame;
    private DefaultTableModel tableModel;
    private Connection conn;

    // Méthode permettant d'obtenir la fenêtre principale
    public JFrame getFrame() {
        return frame;
    }

    // Constructeur de la classe
    public GestionAuteursUI() {
        auteurs = new ArrayList<>();
        initialize(); // Initialisation de l'interface utilisateur
        chargerAuteursDepuisBaseDeDonnees(); // Chargement des auteurs depuis la base de données
    }

    // Méthode pour initialiser l'interface utilisateur
    private void initialize() {
        frame = new JFrame("Gestion des Auteurs");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "Nom", "Prénom", "Date de naissance" });

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterAuteur();
            }
        });

        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierAuteur(table);
            }
        });

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerAuteur(table);
            }
        });

        inputPanel.add(btnAjouter);
        inputPanel.add(btnModifier);
        inputPanel.add(btnSupprimer);

        // Établir la connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/librairie_2?useSSL=false";
        String user = "root";
        String password = "root";

        try {
            // Chargement explicite du pilote JDBC
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion à la base de données réussie.");
        } catch (ClassNotFoundException e) {
            System.out.println("Pilote JDBC introuvable. Assurez-vous d'avoir ajouté le fichier JAR approprié.");
            e.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la connexion à la base de données: " + ex.getMessage());
        }
    }

    // Méthode pour charger les auteurs depuis la base de données
    private void chargerAuteursDepuisBaseDeDonnees() {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String selectQuery = "SELECT nom, prenom, date_naissance FROM auteur";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String dateNaissance = rs.getString("date_naissance");

                AUTEUR auteur = new AUTEUR("", nom, prenom, dateNaissance, "");
                auteurs.add(auteur);
            }

            afficherAuteurs(); // Afficher les auteurs dans l'interface utilisateur

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des auteurs depuis la base de données: " + e.getMessage());
        } finally {
            // Fermer les ressources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour afficher les auteurs dans l'interface utilisateur
    private void afficherAuteurs() {
        tableModel.setRowCount(0); // Réinitialiser le modèle de tableau
        for (AUTEUR auteur : auteurs) {
            Object[] row = { auteur.getNom(), auteur.getPrenom(), auteur.getDate_naissance() };
            tableModel.addRow(row); // Ajouter une ligne au modèle de tableau
        }
    }

    // Méthode pour ajouter un auteur
    private void ajouterAuteur() {
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtDateNaissance = new JTextField();

        Object[] message = {
                "Nom:", txtNom,
                "Prénom:", txtPrenom,
                "Date de naissance:", txtDateNaissance
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Ajouter un auteur", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String dateNaissance = txtDateNaissance.getText();

            AUTEUR nouvelAuteur = new AUTEUR("", nom, prenom, dateNaissance, "");
            auteurs.add(nouvelAuteur); // Ajouter l'auteur à la liste en mémoire
            ajouterAuteurDansBaseDeDonnees(nouvelAuteur); // Ajouter l'auteur à la base de données
            afficherAuteurs(); // Rafraîchir l'affichage des auteurs
        }
    }

    // Méthode pour ajouter un auteur dans la base de données
    private void ajouterAuteurDansBaseDeDonnees(AUTEUR auteur) {
        PreparedStatement stmt = null;

        try {
            String insertQuery = "INSERT INTO auteur (nom, prenom, date_naissance, description) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, auteur.getNom());
            stmt.setString(2, auteur.getPrenom());
            stmt.setString(3, auteur.getDate_naissance());
            stmt.setString(4, auteur.getDescription());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Nouvel auteur ajouté avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout de l'auteur dans la base de données: " + e.getMessage());
        } finally {
            // Fermer les ressources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour modifier un auteur
    private void modifierAuteur(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un auteur à modifier.");
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        String prenom = (String) table.getValueAt(selectedRow, 1);
        String dateNaissance = (String) table.getValueAt(selectedRow, 2);

        JTextField txtNom = new JTextField(nom);
        JTextField txtPrenom = new JTextField(prenom);
        JTextField txtDateNaissance = new JTextField(dateNaissance);

        Object[] message = {
                "Nom:", txtNom,
                "Prénom:", txtPrenom,
                "Date de naissance:", txtDateNaissance
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Modifier un auteur", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nouveauNom = txtNom.getText();
            String nouveauPrenom = txtPrenom.getText();
            String nouvelleDateNaissance = txtDateNaissance.getText();

            AUTEUR auteurModifie = new AUTEUR(nouveauNom, nouveauPrenom, nouvelleDateNaissance, "");
            auteurs.set(selectedRow, auteurModifie); // Modifier l'auteur dans la liste en mémoire
            modifierAuteurDansBaseDeDonnees(auteurModifie, nom, prenom, dateNaissance); // Modifier l'auteur dans la base de données
            afficherAuteurs(); // Rafraîchir l'affichage des auteurs
        }
    }

    // Méthode pour modifier un auteur dans la base de données
    private void modifierAuteurDansBaseDeDonnees(AUTEUR auteurModifie, String ancienNom, String ancienPrenom, String ancienneDateNaissance) {
        PreparedStatement stmt = null;

        try {
            String updateQuery = "UPDATE auteur SET nom = ?, prenom = ?, date_naissance = ? WHERE nom = ? AND prenom = ? AND date_naissance = ?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, auteurModifie.getNom());
            stmt.setString(2, auteurModifie.getPrenom());
            stmt.setString(3, auteurModifie.getDate_naissance());
            stmt.setString(4, ancienNom);
            stmt.setString(5, ancienPrenom);
            stmt.setString(6, ancienneDateNaissance);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Auteur modifié avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la modification de l'auteur dans la base de données: " + e.getMessage());
        } finally {
            // Fermer les ressources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour supprimer un auteur
    private void supprimerAuteur(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un auteur à supprimer.");
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        String prenom = (String) table.getValueAt(selectedRow, 1);
        String dateNaissance = (String) table.getValueAt(selectedRow, 2);

        int option = JOptionPane.showConfirmDialog(frame, "Êtes-vous sûr de vouloir supprimer cet auteur ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            auteurs.remove(selectedRow); // Supprimer l'auteur de la liste en mémoire
            supprimerAuteurDeBaseDeDonnees(nom, prenom, dateNaissance); // Supprimer l'auteur de la base de données
            afficherAuteurs(); // Rafraîchir l'affichage des auteurs
        }
    }

    // Méthode pour supprimer un auteur de la base de données
    private void supprimerAuteurDeBaseDeDonnees(String nom, String prenom, String dateNaissance) {
        PreparedStatement stmt = null;

        try {
            String deleteQuery = "DELETE FROM auteur WHERE nom = ? AND prenom = ? AND date_naissance = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, dateNaissance);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Auteur supprimé avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression de l'auteur dans la base de données: " + e.getMessage());
        } finally {
            // Fermer les ressources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode principale pour exécuter l'application
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GestionAuteursUI window = new GestionAuteursUI();
                    window.frame.setVisible(true); // Rendre la fenêtre visible
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
