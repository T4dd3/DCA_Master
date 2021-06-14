package dcamaster.gestioneaccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.UserRepository;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.Utente;

public class AutenticazioneController extends HttpServlet implements IAutenticazione{

	private static final long serialVersionUID = 1L;
	
	private HttpSession session;
	private ControllerPersistenza controllerPersistenza;
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
	
	@Override
	public void autentica(String username, String password) 
	{
		UserRepository repo = new UserRepository(controllerPersistenza);
		try {
			Utente utenteAttivo = repo.read(username, password);
			if(utenteAttivo == null) {
				//credenziali errate
			} else {
				session.setAttribute("utente", utenteAttivo);
				StrategiaDCA strategiaDCA = utenteAttivo.getDca();
				//redirect alla prossima servlet in base ai parametri di DCA
			}
		} catch (Exception e) {
			//login fallito
		}
	}

}
