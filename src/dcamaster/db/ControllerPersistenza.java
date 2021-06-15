package dcamaster.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dcamaster.model.EntryOperazione;

// Singleton
public class ControllerPersistenza {
	// Variabili per la connessione al db
	private String dbPath = "dbProgettoIngSoftware.db";
   
    private static ControllerPersistenza controllerPersistenza = null;

	private ControllerPersistenza() {
	}
	
	public static ControllerPersistenza getInstance() {
		if(controllerPersistenza == null)
			controllerPersistenza = new ControllerPersistenza();
		return controllerPersistenza;
	}
	
	public Connection getConnection() throws PersistenceException {
        String dbUri = "jdbc:sqlite:" + dbPath;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUri);
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
