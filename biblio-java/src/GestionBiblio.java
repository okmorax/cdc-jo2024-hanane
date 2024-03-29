import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionBiblio {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frame = new JFrame("Gestion de la Bibliothèque");
                    frame.setBounds(100, 100, 400, 300);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    JPanel panel = new JPanel();
                    frame.getContentPane().add(panel, BorderLayout.CENTER);
                    panel.setLayout(new GridLayout(3, 1));

                    JButton btnGestionAuteur = new JButton("Gestion des Auteurs");
                    btnGestionAuteur.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            GestionAuteursUI gestionAuteursUI = new GestionAuteursUI();
                            gestionAuteursUI.getFrame().setVisible(true);
                        }
                    });
                    panel.add(btnGestionAuteur);

                    JButton btnGestionLivre = new JButton("Gestion des Livres");
                    btnGestionLivre.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            GestionLivresUI gestionLivresUI = new GestionLivresUI();
                            gestionLivresUI.getFrame().setVisible(true);
                        }
                    });
                    panel.add(btnGestionLivre);

                    JButton btnGestionAdherent = new JButton("Gestion des Adhérents");
                    btnGestionAdherent.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Ajoutez ici le code pour la gestion des adhérents
                            // Exemple : GestionAdherentsUI gestionAdherentsUI = new GestionAdherentsUI();
                            // gestionAdherentsUI.getFrame().setVisible(true);
                        }
                    });
                    panel.add(btnGestionAdherent);

                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
