import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
  public static void main(String[] args) {
    // Chargement explicite du pilote JDBC
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Pilote JDBC introuvable. Assurez-vous d'avoir ajouté le fichier JAR approprié.");
      e.printStackTrace();
      return;
    }

    // Remplacer 'root' par votre nom d'utilisateur et votre mot de passe MySQL
    String url = "jdbc:mysql://localhost:3306/librairie_2?useSSL=false";
    String user = "root";
    String password = "root";

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      // Connexion réussie
      System.out.println("Connexion à la base de données réussie.");
      // Vous pouvez ajouter d'autres opérations avec la base de données ici
    } catch (SQLException e) {
      // Gestion des erreurs de connexion
      System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
    }
  }
}
