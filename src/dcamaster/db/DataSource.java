package dcamaster.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

	private String dbName = "dbname";
    private String userName = "dbuser";
    private String password = "dbpasswd";
    
    public DataSource() {
    	
    }
    
    public Connection getConnection() throws PersistenceException {
    	String driver = "com.mysql.jdbc.Driver";
        String dbUri = "jdbc:mysql://localhost:3306/"+dbName;
        Connection connection = null;
        try {
            System.out.println("DataSource.getConnection() driver = "+driver);
            Class.forName(driver);
            System.out.println("DataSource.getConnection() dbUri = "+dbUri);
            connection = DriverManager.getConnection(dbUri, userName, password);
        }
        catch (ClassNotFoundException e) {
            throw new PersistenceException(e.getMessage());
        }
        catch(SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
        return connection;
    }
}
