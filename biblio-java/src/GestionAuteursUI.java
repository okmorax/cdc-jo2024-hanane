import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GestionAuteursUI {
    private List<AUTEUR> auteurs;
    private JFrame frame;
    private DefaultTableModel tableModel;

    public GestionAuteursUI() {
        auteurs = new ArrayList<>();
        initialize();
    }

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

        afficherAuteurs();
    }

    private void afficherAuteurs() {
        tableModel.setRowCount(0);
        for (AUTEUR auteur : auteurs) {
            Object[] row = { auteur.getNom(), auteur.getPrenom(), auteur.getDate_naissance() };
            tableModel.addRow(row);
        }
    }

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
            auteurs.add(nouvelAuteur);
            afficherAuteurs();
        }
    }

    private void modifierAuteur(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String nom = (String) tableModel.getValueAt(selectedRow, 0);
            AUTEUR auteurAModifier = trouverAuteurParNom(nom);

            if (auteurAModifier != null) {
                String nouveauNom = JOptionPane.showInputDialog("Nouveau nom de l'auteur:");
                String nouveauPrenom = JOptionPane.showInputDialog("Nouveau prénom de l'auteur:");
                String nouvelleDateNaissance = JOptionPane.showInputDialog("Nouvelle date de naissance de l'auteur:");

                auteurAModifier.setNom(nouveauNom);
                auteurAModifier.setPrenom(nouveauPrenom);
                auteurAModifier.setDate_naissance(nouvelleDateNaissance);
                afficherAuteurs();
            } else {
                JOptionPane.showMessageDialog(frame, "Auteur non trouvé.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un auteur à modifier.");
        }
    }

    private void supprimerAuteur(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String nom = (String) tableModel.getValueAt(selectedRow, 0);
            AUTEUR auteurASupprimer = trouverAuteurParNom(nom);

            if (auteurASupprimer != null) {
                auteurs.remove(auteurASupprimer);
                afficherAuteurs();
            } else {
                JOptionPane.showMessageDialog(frame, "Auteur non trouvé.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un auteur à supprimer.");
        }
    }

    private AUTEUR trouverAuteurParNom(String nom) {
        for (AUTEUR auteur : auteurs) {
            if (auteur.getNom().equalsIgnoreCase(nom)) {
                return auteur;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GestionAuteursUI window = new GestionAuteursUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
