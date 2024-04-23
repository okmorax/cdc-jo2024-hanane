import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionAdherentsUI {
    private List<ADHERENT> adherents; // Liste des adhérents
    private JFrame frame; // Fenêtre principale
    private DefaultTableModel tableModel; // Modèle de tableau pour afficher les adhérents
    private Connection conn; // Objet de connexion à la base de données

    public JFrame getFrame() {
        return frame;
    }

    public GestionAdherentsUI() {
        adherents = new ArrayList<>(); // Initialisation de la liste des adhérents
        initialize(); // Initialisation de l'interface utilisateur
        chargerAdherentsDepuisBaseDeDonnees(); // Chargement des adhérents depuis la base de données au démarrage
    }

    private void initialize() {
        // Configuration de la fenêtre principale
        frame = new JFrame("Gestion des Adhérents");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création du modèle de tableau pour afficher les adhérents
        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "Nom", "Prénom", "Email" });

        // Création de la table avec le modèle de tableau
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Création du panneau pour les boutons d'interaction
        JPanel inputPanel = new JPanel();
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

        // Création des boutons Ajouter, Modifier et Supprimer
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterAdherent(); // Appel de la méthode pour ajouter un adhérent
            }
        });

        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierAdherent(table); // Appel de la méthode pour modifier un adhérent
            }
        });

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerAdherent(table); // Appel de la méthode pour supprimer un adhérent
            }
        });

        // Ajout des boutons au panneau d'entrée
        inputPanel.add(btnAjouter);
        inputPanel.add(btnModifier);
        inputPanel.add(btnSupprimer);

        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/librairie_2?useSSL=false";
        String user = "root";
        String password = "root";

        try {
            Class.forName("com.mysql.jdbc.Driver"); // Chargement explicite du pilote JDBC
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

    // Méthode pour charger les adhérents depuis la base de données
    private void chargerAdherentsDepuisBaseDeDonnees() {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String selectQuery = "SELECT nom, prenom, email FROM adherent";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");

                ADHERENT adherent = new ADHERENT(nom, prenom, email);
                adherents.add(adherent);
            }

            afficherAdherents(); // Affichage des adhérents dans le tableau

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des adhérents depuis la base de données: " + e.getMessage());
        } finally {
            // Fermeture des ressources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour afficher les adhérents dans le tableau
    private void afficherAdherents() {
        tableModel.setRowCount(0); // Réinitialisation du modèle de tableau
        for (ADHERENT adherent : adherents) {
            Object[] row = { adherent.getNom(), adherent.getPrenom(), adherent.getEmail() };
            tableModel.addRow(row); // Ajout de chaque adhérent au modèle de tableau
        }
    }

    // Méthode pour ajouter un adhérent
    private void ajouterAdherent() {
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JTextField txtEmail = new JTextField();

        Object[] message = {
                "Nom:", txtNom,
                "Prénom:", txtPrenom,
                "Email:", txtEmail
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Ajouter un adhérent", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nom = txtNom.getText();
            String prenom = txtPrenom.getText();
            String email = txtEmail.getText();

            ADHERENT nouvelAdherent = new ADHERENT(nom, prenom, email);
            adherents.add(nouvelAdherent); // Ajout de l'adhérent à la liste en mémoire
            ajouterAdherentDansBaseDeDonnees(nouvelAdherent); // Ajout de l'adhérent à la base de données
            afficherAdherents(); // Rafraîchissement de l'affichage des adhérents
        }
    }

    // Méthode pour ajouter un adhérent dans la base de données
    private void ajouterAdherentDansBaseDeDonnees(ADHERENT adherent) {
        PreparedStatement stmt = null;

        try {
            String insertQuery = "INSERT INTO adherent (nom, prenom, email) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, adherent.getNom());
            stmt.setString(2, adherent.getPrenom());
            stmt.setString(3, adherent.getEmail());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Nouvel adhérent ajouté avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout de l'adhérent dans la base de données: " + e.getMessage());
        } finally {
            // Fermeture des ressources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour modifier un adhérent
    private void modifierAdherent(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un adhérent à modifier.");
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        String prenom = (String) table.getValueAt(selectedRow, 1);
        String email = (String) table.getValueAt(selectedRow, 2);

        JTextField txtNom = new JTextField(nom);
        JTextField txtPrenom = new JTextField(prenom);
        JTextField txtEmail = new JTextField(email);

        Object[] message = {
                "Nom:", txtNom,
                "Prénom:", txtPrenom,
                "Email:", txtEmail
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Modifier un adhérent", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String nouveauNom = txtNom.getText();
            String nouveauPrenom = txtPrenom.getText();
            String nouvelEmail = txtEmail.getText();

            ADHERENT adherentModifie = new ADHERENT(nouveauNom, nouveauPrenom, nouvelEmail);
            adherents.set(selectedRow, adherentModifie); // Mise à jour de l'adhérent dans la liste en mémoire
            modifierAdherentDansBaseDeDonnees(adherentModifie, nom, prenom, email); // Mise à jour de l'adhérent dans la base de données
            afficherAdherents(); // Rafraîchissement de l'affichage des adhérents
        }
    }

    // Méthode pour modifier un adhérent dans la base de données
    private void modifierAdherentDansBaseDeDonnees(ADHERENT adherentModifie, String ancienNom, String ancienPrenom, String ancienEmail) {
        PreparedStatement stmt = null;

        try {
            String updateQuery = "UPDATE adherent SET nom = ?, prenom = ?, email = ? WHERE nom = ? AND prenom = ? AND email = ?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, adherentModifie.getNom());
            stmt.setString(2, adherentModifie.getPrenom());
            stmt.setString(3, adherentModifie.getEmail());
            stmt.setString(4, ancienNom);
            stmt.setString(5, ancienPrenom);
            stmt.setString(6, ancienEmail);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Adhérent modifié avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la modification de l'adhérent dans la base de données: " + e.getMessage());
        } finally {
            // Fermeture des ressources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour supprimer un adhérent
    private void supprimerAdherent(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un adhérent à supprimer.");
            return;
        }

        String nom = (String) table.getValueAt(selectedRow, 0);
        String prenom = (String) table.getValueAt(selectedRow, 1);
        String email = (String) table.getValueAt(selectedRow, 2);

        int option = JOptionPane.showConfirmDialog(frame, "Êtes-vous sûr de vouloir supprimer cet adhérent ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            adherents.remove(selectedRow); // Suppression de l'adhérent de la liste en mémoire
            supprimerAdherentDeBaseDeDonnees(nom, prenom, email); // Suppression de l'adhérent de la base de données
            afficherAdherents(); // Rafraîchissement de l'affichage des adhérents
        }
    }

    // Méthode pour supprimer un adhérent de la base de données
    private void supprimerAdherentDeBaseDeDonnees(String nom, String prenom, String email) {
        PreparedStatement stmt = null;

        try {
            String deleteQuery = "DELETE FROM adherent WHERE nom = ? AND prenom = ? AND email = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Adhérent supprimé avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression de l'adhérent dans la base de données: " + e.getMessage());
        } finally {
            // Fermeture des ressources
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
                    GestionAdherentsUI window = new GestionAdherentsUI();
                    window.frame.setVisible(true); // Rendre la fenêtre visible
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
