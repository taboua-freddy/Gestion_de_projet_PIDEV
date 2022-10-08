
package connexiondb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataSource {
    private String username = "root";
    private String password = "";
    private String url = "jdbc:mysql://127.0.0.1/feather";
    private Connection con;
    private static DataSource instance;
    
    
    public static DataSource getInstance()
    {
        if(instance==null)
        {
            instance = new DataSource();
        }
        return instance;
    }
    
    public Connection getConnection()
    {
        return con;
    }
    
    private DataSource()
    {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.err.println("connexion etablie");
        } catch (SQLException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
 