import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class GestionEmpruntUI {
    private JFrame frame;
    private Connection conn;

    public JFrame getFrame() {
        return frame;
    }

    public GestionEmpruntUI() {
        initialize();
    }

    private void initialize() {
        // Création de la fenêtre de gestion des emprunts
        frame = new JFrame("Gestion des Emprunts");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Création d'un panneau pour contenir les composants
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // Ajout des étiquettes et des champs de sélection
        JLabel lblLivres = new JLabel("Livres:");
        panel.add(lblLivres);

        JComboBox<String> comboBoxLivres = new JComboBox<>();
        panel.add(comboBoxLivres);

        JLabel lblAdherents = new JLabel("Adhérents:");
        panel.add(lblAdherents);

        JComboBox<String> comboBoxAdherents = new JComboBox<>();
        panel.add(comboBoxAdherents);

        JLabel lblDateRetour = new JLabel("Date de retour:");
        panel.add(lblDateRetour);

        JTextField txtDateRetour = new JTextField();
        panel.add(txtDateRetour);

        // Bouton pour effectuer l'emprunt
        JButton btnEmprunter = new JButton("Emprunter");
        btnEmprunter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String livreSelectionne = (String) comboBoxLivres.getSelectedItem();
                String adherentSelectionne = (String) comboBoxAdherents.getSelectedItem();
                String dateRetour = txtDateRetour.getText();

                // Vérification des champs obligatoires
                if (livreSelectionne == null || adherentSelectionne == null || dateRetour.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un livre, un adhérent et saisir une date de retour.");
                } else {
                    emprunterLivre(livreSelectionne, adherentSelectionne, dateRetour);
                }
            }
        });
        panel.add(btnEmprunter);

        // Établir la connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/librairie_2?useSSL=false";
        String user = "root";
        String password = "root";

        try {
            // Chargement explicite du pilote JDBC
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion à la base de données réussie.");
            // Charger les livres et les adhérents dans les JComboBox
            chargerLivresDansComboBox(comboBoxLivres);
            chargerAdherentsDansComboBox(comboBoxAdherents);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la connexion à la base de données: " + ex.getMessage());
        }

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }

    // Méthode pour charger les livres disponibles dans la base de données dans le JComboBox
    private void chargerLivresDansComboBox(JComboBox<String> comboBoxLivres) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String selectQuery = "SELECT titre FROM livre WHERE isbn NOT IN (SELECT isbn FROM emprunt)";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String titre = rs.getString("titre");
                comboBoxLivres.addItem(titre);
            }
        } finally {
            // Fermer les ressources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    // Méthode pour charger les adhérents disponibles dans la base de données dans le JComboBox
    private void chargerAdherentsDansComboBox(JComboBox<String> comboBoxAdherents) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String selectQuery = "SELECT CONCAT(nom, ' ', prenom) AS nom_prenom FROM adherent";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String nomPrenom = rs.getString("nom_prenom");
                comboBoxAdherents.addItem(nomPrenom);
            }
        } finally {
            // Fermer les ressources
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
    }

    // Méthode pour effectuer l'emprunt d'un livre
    private void emprunterLivre(String titre, String nomPrenom, String dateRetour) {
        PreparedStatement stmt = null;

        try {
            LocalDate dateEmprunt = LocalDate.now();
            String insertQuery = "INSERT INTO emprunt (isbn, adhnum, date_emprunt, date_retour) " +
                    "SELECT isbn, adhnum, ?, ? FROM livre, adherent " +
                    "WHERE titre = ? AND CONCAT(nom, ' ', prenom) = ? AND isbn NOT IN (SELECT isbn FROM emprunt)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setDate(1, Date.valueOf(dateEmprunt));
            stmt.setDate(2, Date.valueOf(LocalDate.parse(dateRetour)));
            stmt.setString(3, titre);
            stmt.setString(4, nomPrenom);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(frame, "Emprunt réussi.");
            } else {
                JOptionPane.showMessageDialog(frame, "Le livre n'est pas disponible pour l'emprunt.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'emprunt du livre: " + e.getMessage());
        } finally {
            // Fermer les ressources
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Création de l'instance de la fenêtre de gestion des emprunts
                    GestionEmpruntUI window = new GestionEmpruntUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
