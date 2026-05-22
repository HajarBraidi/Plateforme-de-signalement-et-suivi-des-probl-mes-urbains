package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.flywaydb.core.Flyway;

public class SingletonConnection {
	
    private static Connection connection;
    
    static {
        try {   	
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // ✅ Lire depuis variables d'environnement (Railway)
            // Si la variable n'existe pas → fallback vers db.properties (local)
            String url      = System.getenv("DB_URL");
            String user     = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");
            
            // ✅ Fallback local : si on est en développement (pas sur Railway)
            if (url == null || url.isEmpty()) {
                Properties props = new Properties();
                var in = SingletonConnection.class.getClassLoader()
                            .getResourceAsStream("db.properties");
                props.load(in);
                url      = props.getProperty("db.url");
                user     = props.getProperty("db.user");
                password = props.getProperty("db.password");
            }
            
            migrateDatabase(url, user, password);
            connection = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }
    
    private static void migrateDatabase(String URL, String USER, String PASSWORD) {
        try {
            System.out.println("Migration Flyway en cours...");
            Flyway flyway = Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .cleanDisabled(true)   // ✅ true en production !
                .baselineOnMigrate(true)
                .outOfOrder(true)  
                .load();
            
            flyway.repair();
            flyway.migrate();
            
            System.out.println("Migration réussie ✅");
        } catch (Exception e) {
            throw new RuntimeException("Erreur migration Flyway", e);
        }
    }
    
    public static Connection getConnection() {
        return connection;
    }
}