import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GestionRetourUI {
    private JFrame frame;
    private Connection conn;

    public JFrame getFrame() {
        return frame;
    }

    public GestionRetourUI() {
        initialize(); // Initialise la fenêtre et les composants
    }

    private void initialize() {
        frame = new JFrame("Gestion des Retours"); // Crée la fenêtre JFrame avec le titre
        frame.setBounds(100, 100, 400, 200); // Définit la taille et la position de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Définit l'action par défaut lors de la fermeture de la fenêtre
        frame.getContentPane().setLayout(new BorderLayout()); // Utilise BorderLayout pour organiser les composants

        JPanel panel = new JPanel(); // Crée un JPanel pour contenir les composants
        frame.getContentPane().add(panel, BorderLayout.CENTER); // Ajoute le JPanel au centre de la fenêtre
        panel.setLayout(new GridLayout(2, 2, 10, 10)); // Utilise GridLayout pour organiser les composants en grille

        JLabel lblLivres = new JLabel("Livres empruntés:"); // Crée un JLabel pour afficher le texte "Livres empruntés"
        panel.add(lblLivres); // Ajoute le JLabel au JPanel

        JComboBox<String> comboBoxLivres = new JComboBox<>(); // Crée une JComboBox pour afficher les livres empruntés
        panel.add(comboBoxLivres); // Ajoute la JComboBox au JPanel

        JButton btnValider = new JButton("Valider"); // Crée un JButton avec le texte "Valider"
        btnValider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String livreSelectionne = (String) comboBoxLivres.getSelectedItem();
                if (livreSelectionne == null) {
                    JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un livre."); // Affiche un message si aucun livre n'est sélectionné
                } else {
                    validerRetour(livreSelectionne); // Valide le retour du livre sélectionné
                }
            }
        });
        panel.add(btnValider); // Ajoute le JButton au JPanel

        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/librairie_2?useSSL=false";
        String user = "root";
        String password = "root";

        try {
            // Chargement explicite du pilote JDBC
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion à la base de données réussie.");
            chargerLivresEmpruntesDansComboBox(comboBoxLivres); // Charge les livres empruntés dans la JComboBox
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la connexion à la base de données: " + ex.getMessage());
        }

        frame.setVisible(true); // Rend la fenêtre visible
    }

    // Charge les livres empruntés depuis la base de données dans la JComboBox
    private void chargerLivresEmpruntesDansComboBox(JComboBox<String> comboBoxLivres) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String selectQuery = "SELECT titre FROM emprunt, livre WHERE emprunt.isbn = livre.isbn";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String titre = rs.getString("titre");
                comboBoxLivres.addItem(titre); // Ajoute chaque titre de livre à la JComboBox
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des livres empruntés: " + e.getMessage());
        } finally {
            // Ferme les ressources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Valide le retour du livre sélectionné
    private void validerRetour(String titreLivre) {
        PreparedStatement stmt = null;

        try {
            // Requête SQL pour supprimer l'emprunt du livre retourné de la table 'emprunt'
            String deleteQuery = "DELETE FROM emprunt WHERE isbn = (SELECT isbn FROM livre WHERE titre = ?)";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, titreLivre); // Définit le titre du livre à supprimer

            int rowsDeleted = stmt.executeUpdate(); // Exécute la requête de suppression
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(frame, "Retour du livre '" + titreLivre + "' validé."); // Affiche un message de confirmation
            } else {
                JOptionPane.showMessageDialog(frame, "Erreur lors de la validation du retour du livre.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la validation du retour du livre: " + e.getMessage());
        } finally {
            // Ferme les ressources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Point d'entrée principal du programme
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GestionRetourUI window = new GestionRetourUI(); // Crée une instance de la classe GestionRetourUI
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
