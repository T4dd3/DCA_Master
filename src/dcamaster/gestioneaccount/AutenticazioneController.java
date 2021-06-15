package dcamaster.gestioneaccount;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.UserRepository;
import dcamaster.db.ValutaFiatRepository;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.Utente;

public class AutenticazioneController extends HttpServlet implements IAutenticazione
{

	private static final long serialVersionUID = 1L;
	
	private ControllerPersistenza controllerPersistenza;
	
	@Override
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		
		// Istanza del singleton di Controller Persistenza
		this.controllerPersistenza = ControllerPersistenza.getInstance();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");
		
		// RequestDispatcher usato per redirect alla pagina corretta (default=pagina di login)
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/ViewAutenticazione.jsp");;
		
		if (username != null && password != null) 
		{
			Utente utente = this.autentica(username, password);
			//
			if (utente != null) 
			{
				// Recupero strategiaDCA e forward alla pagina corretta
				StrategiaDCA strategiaDCA = utente.getDca();
				
				// Salvataggio Utente in Sessione
				request.getSession().setAttribute("utente", utente);
				
				// Forward a HomeConfigurazione
				if(strategiaDCA.getBudget() == null || 
					strategiaDCA.getIntervalloInvestimento() == null || 
					strategiaDCA.getDistribuzionePercentuale() == null)
				{	
					requestDispatcher = request.getRequestDispatcher("/HomeConfigurazione.jsp");
					requestDispatcher.forward(request, response);
				}
				// Forward a VisualizzaAndamento
				else 
				{
					requestDispatcher = request.getRequestDispatcher("/VisualizzaAndamento.jsp");
					requestDispatcher.forward(request, response);
				}
			}
			else
			{
				// Pagina login con errore
				request.setAttribute("errorMessage", "Le credenziali inserite non sono valide");
				requestDispatcher.forward(request, response);
			}
		}
		else
		{
			// Pagina login con errore
			request.setAttribute("errorMessage", "Inserisci sia lo username che la password");
			requestDispatcher.forward(request, response);
		}
			
	}
	
	@Override
	public Utente autentica(String username, String password) 
	{
		UserRepository repo = new UserRepository(controllerPersistenza);
				
		try {
			Utente utenteAttivo = repo.read(username, password);
			return utenteAttivo;
		} catch (Exception e) {
			return null;
		}
		
	}

}
