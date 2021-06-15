package TestDb;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;
import dcamaster.model.Utente;

class ConnectionTest {

	@Test
	void test() {
		ControllerPersistenza controller = ControllerPersistenza.getInstance();
		try {
			Connection connection = controller.getConnection();
			System.out.println("Connesso al db: " + connection.toString());
		} catch (PersistenceException e) {
			System.out.println("unlucky");
			e.printStackTrace();
			fail();
		}
		UserRepository repo = new UserRepository(controller);
		try {
			Utente utente = repo.read("giova", "boh");
			if(utente == null) {
				System.out.println("credenziali sbagliate");
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
			fail();
		}
	}

}
