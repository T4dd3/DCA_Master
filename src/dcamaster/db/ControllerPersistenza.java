package dcamaster.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dcamaster.model.EntryOperazione;

// Singleton
public class ControllerPersistenza {
	// Variabili per la connessione al db
	private String dbName = "dbname";
    private String userName = "dbuser";
    private String password = "dbpasswd";
   
    private static ControllerPersistenza controllerPersistenza = null;

	private ControllerPersistenza() {
	}
	
	public static ControllerPersistenza getInstance() {
		if(controllerPersistenza == null)
			controllerPersistenza = new ControllerPersistenza();
		return controllerPersistenza;
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
	
	public void inserisciEntry(EntryOperazione entry) {
		//toDo
	}
}
