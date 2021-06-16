package dcamaster.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

import dcamaster.model.EntryOperazione;

// Singleton
public class ControllerPersistenza {
	// Variabili per la connessione al db
	private String dbPath = "/root/git/DCA_Master/web/dbProgettoIngSoftware.db";
   
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
		File logs = new File("Log.txt");
		try {
			FileWriter writer = new FileWriter(logs);
			writer.write("Operazione: " + entry.getTipoOperazione() + ", Messaggio: " + entry.getMessaggio()
					 + ", Data: " + LocalDateTime.now());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
