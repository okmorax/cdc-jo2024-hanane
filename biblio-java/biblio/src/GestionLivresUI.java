import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionLivresUI {
    private List<LIVRE> livres; // Liste des livres en mémoire
    private List<AUTEUR> auteurs; // Liste des auteurs en mémoire
    private JFrame frame; // Fenêtre principale
    private DefaultTableModel tableModel; // Modèle de table pour afficher les livres
    private Connection conn; // Connexion à la base de données
    private JComboBox<String> comboBoxAuteurs; // JComboBox pour sélectionner les auteurs

    public JFrame getFrame() {
        return frame;
    }

    public GestionLivresUI() {
        livres = new ArrayList<>();
        auteurs = new ArrayList<>();
        initialize(); // Initialisation de l'interface utilisateur
        chargerLivresDepuisBaseDeDonnees(); // Chargement des livres depuis la base de données
        chargerAuteursDepuisBaseDeDonnees(); // Chargement des auteurs depuis la base de données
    }

    private void initialize() {
        // Création de la fenêtre principale
        frame = new JFrame("Gestion des Livres");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création du modèle de table pour afficher les livres
        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ISBN", "Titre", "Prix", "Auteur" });

        // Création de la JTable avec le modèle de table
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Création d'un panneau pour les boutons et le JComboBox
        JPanel inputPanel = new JPanel();
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

        // Création des boutons Ajouter, Modifier, Supprimer
        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterLivre();
            }
        });

        JButton btnModifier = new JButton("Modifier");
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierLivre(table);
            }
        });

        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerLivre(table);
            }
        });

        // Ajout des boutons au panneau
        inputPanel.add(btnAjouter);
        inputPanel.add(btnModifier);
        inputPanel.add(btnSupprimer);

        // Création du JComboBox pour sélectionner les auteurs
        comboBoxAuteurs = new JComboBox<>();
        inputPanel.add(comboBoxAuteurs);

        // Informations de connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/librairie_2?useSSL=false";
        String user = "root";
        String password = "root";

        try {
            // Chargement du pilote JDBC et établissement de la connexion à la base de données
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

        // Affichage des livres dans la table
        afficherLivres();
    }

    // Méthode pour charger les livres depuis la base de données
    private void chargerLivresDepuisBaseDeDonnees() {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String selectQuery = "SELECT l.ISBN, l.titre, l.prix, a.nom, a.prenom FROM livre l INNER JOIN auteur a ON l.autnum = a.autnum";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String ISBN = rs.getString("ISBN");
                String titre = rs.getString("titre");
                int prix = rs.getInt("prix");
                String nomAuteur = rs.getString("nom");
                String prenomAuteur = rs.getString("prenom");

                AUTEUR auteur = new AUTEUR("", nomAuteur, prenomAuteur, "", "");
                LIVRE livre = new LIVRE(ISBN, titre, prix, auteur);
                livres.add(livre);
            }

            afficherLivres();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des livres depuis la base de données: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour charger les auteurs depuis la base de données et les ajouter au JComboBox
    private void chargerAuteursDepuisBaseDeDonnees() {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String selectQuery = "SELECT autnum, nom, prenom FROM auteur";
            stmt = conn.prepareStatement(selectQuery);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String autnum = rs.getString("autnum");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String nomComplet = prenom + " " + nom;
                auteurs.add(new AUTEUR(autnum, nom, prenom, "", ""));
                comboBoxAuteurs.addItem(nomComplet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors du chargement des auteurs depuis la base de données: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour afficher les livres dans la table
    private void afficherLivres() {
        tableModel.setRowCount(0); // Effacer le contenu de la table
        for (LIVRE livre : livres) {
            Object[] row = { livre.getISBN(), livre.getTitre(), livre.getPrix(), livre.getAuteur().getNomComplet() };
            tableModel.addRow(row); // Ajouter une ligne à la table
        }
    }

    // Méthode pour ajouter un livre
    private void ajouterLivre() {
        // Création des champs de saisie pour le nouvel livre
        JTextField txtISBN = new JTextField();
        JTextField txtTitre = new JTextField();
        JTextField txtPrix = new JTextField();

        // Message à afficher dans la boîte de dialogue
        Object[] message = {
                "ISBN:", txtISBN,
                "Titre:", txtTitre,
                "Prix:", txtPrix,
                "Auteur:", comboBoxAuteurs
        };

        // Affichage de la boîte de dialogue pour saisir les informations du nouvel livre
        int option = JOptionPane.showConfirmDialog(frame, message, "Ajouter un livre", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String ISBN = txtISBN.getText();
            String titre = txtTitre.getText();
            String prixStr = txtPrix.getText();
            String selectedAuteur = (String) comboBoxAuteurs.getSelectedItem();

            try {
                int prix = Integer.parseInt(prixStr);

                if (!livreExiste(ISBN)) {
                    AUTEUR auteur = null;
                    for (AUTEUR aut : auteurs) {
                        if ((aut.getPrenom() + " " + aut.getNom()).equals(selectedAuteur)) {
                            auteur = aut;
                            break;
                        }
                    }
                    LIVRE nouveauLivre = new LIVRE(ISBN, titre, prix, auteur);
                    livres.add(nouveauLivre);
                    ajouterLivreDansBaseDeDonnees(nouveauLivre);
                    afficherLivres();
                } else {
                    JOptionPane.showMessageDialog(frame, "Un livre avec le même ISBN existe déjà.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Veuillez saisir un prix valide.");
            }
        }
    }

    // Méthode pour vérifier si un livre avec le même ISBN existe déjà
    private boolean livreExiste(String ISBN) {
        for (LIVRE livre : livres) {
            if (livre.getISBN().equalsIgnoreCase(ISBN)) {
                return true;
            }
        }
        return false;
    }

    // Méthode pour ajouter un livre dans la base de données
    private void ajouterLivreDansBaseDeDonnees(LIVRE livre) {
        PreparedStatement stmt = null;

        try {
            String insertQuery = "INSERT INTO livre (ISBN, titre, prix, autnum) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, livre.getISBN());
            stmt.setString(2, livre.getTitre());
            stmt.setInt(3, livre.getPrix());
            stmt.setString(4, livre.getAuteur().getAutnum());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Nouveau livre ajouté avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'ajout du livre dans la base de données: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour modifier un livre
    private void modifierLivre(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            LIVRE livreSelectionne = livres.get(selectedRow);

            // Création des champs de saisie pour modifier le livre
            JTextField txtTitre = new JTextField();
            JTextField txtPrix = new JTextField();

            txtTitre.setText(livreSelectionne.getTitre());
            txtPrix.setText(String.valueOf(livreSelectionne.getPrix()));

            // Message à afficher dans la boîte de dialogue
            Object[] message = {
                    "Titre:", txtTitre,
                    "Prix:", txtPrix,
                    "Auteur:", comboBoxAuteurs
            };

            // Affichage de la boîte de dialogue pour modifier les informations du livre
            int option = JOptionPane.showConfirmDialog(frame, message, "Modifier un livre", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String titre = txtTitre.getText();
                String prixStr = txtPrix.getText();
                String selectedAuteur = (String) comboBoxAuteurs.getSelectedItem();

                try {
                    int prix = Integer.parseInt(prixStr);

                    AUTEUR auteur = null;
                    for (AUTEUR aut : auteurs) {
                        if ((aut.getPrenom() + " " + aut.getNom()).equals(selectedAuteur)) {
                            auteur = aut;
                            break;
                        }
                    }
                    livreSelectionne.setTitre(titre);
                    livreSelectionne.setPrix(prix);
                    livreSelectionne.setAuteur(auteur);

                    modifierLivreDansBaseDeDonnees(livreSelectionne);
                    afficherLivres();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(frame, "Veuillez saisir un prix valide.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un livre à modifier.");
        }
    }

    // Méthode pour supprimer un livre
    private void supprimerLivre(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int option = JOptionPane.showConfirmDialog(frame, "Êtes-vous sûr de vouloir supprimer ce livre ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                LIVRE livreASupprimer = livres.get(selectedRow);
                livres.remove(livreASupprimer);
                supprimerLivreDansBaseDeDonnees(livreASupprimer);
                afficherLivres();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un livre à supprimer.");
        }
    }

    // Méthode pour modifier les informations du livre dans la base de données
    private void modifierLivreDansBaseDeDonnees(LIVRE livre) {
        PreparedStatement stmt = null;

        try {
            String updateQuery = "UPDATE livre SET titre = ?, prix = ?, autnum = ? WHERE ISBN = ?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, livre.getTitre());
            stmt.setInt(2, livre.getPrix());
            stmt.setString(3, livre.getAuteur().getAutnum());
            stmt.setString(4, livre.getISBN());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Livre modifié avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la modification du livre dans la base de données: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour supprimer un livre de la base de données
    private void supprimerLivreDansBaseDeDonnees(LIVRE livre) {
        PreparedStatement stmt = null;

        try {
            String deleteQuery = "DELETE FROM livre WHERE ISBN = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, livre.getISBN());

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Livre supprimé avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression du livre dans la base de données: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode principale pour exécuter l'interface utilisateur
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GestionLivresUI window = new GestionLivresUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
