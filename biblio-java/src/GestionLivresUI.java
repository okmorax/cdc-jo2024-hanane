import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GestionLivresUI {
    private List<LIVRE> livres;
    private JFrame frame;
    private DefaultTableModel tableModel;

    public GestionLivresUI() {
        livres = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Gestion des Livres");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ISBN", "Titre", "Prix" });

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

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

        inputPanel.add(btnAjouter);
        inputPanel.add(btnModifier);
        inputPanel.add(btnSupprimer);

        afficherLivres();
    }

    private void afficherLivres() {
        tableModel.setRowCount(0);
        for (LIVRE livre : livres) {
            Object[] row = { livre.getISBN(), livre.getTitre(), livre.getPrix() };
            tableModel.addRow(row);
        }
    }

    private void ajouterLivre() {
        JTextField txtISBN = new JTextField();
        JTextField txtTitre = new JTextField();
        JTextField txtPrix = new JTextField();

        Object[] message = {
                "ISBN:", txtISBN,
                "Titre:", txtTitre,
                "Prix:", txtPrix
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Ajouter un livre", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String ISBN = txtISBN.getText();
            String titre = txtTitre.getText();
            String prixStr = txtPrix.getText();

            try {
                int prix = Integer.parseInt(prixStr);

                if (!livreExiste(ISBN)) {
                    LIVRE nouveauLivre = new LIVRE(ISBN, titre, prix);
                    livres.add(nouveauLivre);
                    afficherLivres();
                } else {
                    JOptionPane.showMessageDialog(frame, "Un livre avec le même ISBN existe déjà.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Veuillez saisir un prix valide.");
            }
        }
    }

    private boolean livreExiste(String ISBN) {
        for (LIVRE livre : livres) {
            if (livre.getISBN().equalsIgnoreCase(ISBN)) {
                return true;
            }
        }
        return false;
    }

    private void modifierLivre(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String ISBN = (String) tableModel.getValueAt(selectedRow, 0);
            String nouveauTitre = JOptionPane.showInputDialog("Nouveau titre:");
            String nouveauPrixStr = JOptionPane.showInputDialog("Nouveau prix:");

            try {
                int nouveauPrix = Integer.parseInt(nouveauPrixStr);
                LIVRE livreAModifier = trouverLivreParISBN(ISBN);

                if (livreAModifier != null) {
                    livreAModifier.setTitre(nouveauTitre);
                    livreAModifier.setPrix(nouveauPrix);
                    afficherLivres();
                } else {
                    JOptionPane.showMessageDialog(frame, "Livre non trouvé.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Veuillez saisir un prix valide.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un livre à modifier.");
        }
    }

    private void supprimerLivre(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String ISBN = (String) tableModel.getValueAt(selectedRow, 0);
            LIVRE livreASupprimer = trouverLivreParISBN(ISBN);

            if (livreASupprimer != null) {
                livres.remove(livreASupprimer);
                afficherLivres();
            } else {
                JOptionPane.showMessageDialog(frame, "Livre non trouvé.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un livre à supprimer.");
        }
    }

    private LIVRE trouverLivreParISBN(String ISBN) {
        for (LIVRE livre : livres) {
            if (livre.getISBN().equalsIgnoreCase(ISBN)) {
                return livre;
            }
        }
        return null;
    }

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
