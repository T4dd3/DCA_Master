package dcamaster.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dcamaster.gestioneaccount.AutenticazioneController;
import dcamaster.gestioneaccount.CodiceDiVerificaController;
import dcamaster.gestioneaccount.IAutenticazione;
import dcamaster.gestioneaccount.ICodiceDiVerifica;
import dcamaster.gestioneaccount.IRegistrazione;
import dcamaster.gestioneaccount.RegistrazioneController;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.TipoDeposito;
import dcamaster.model.Utente;

@SuppressWarnings("serial")
public class RequestManager extends HttpServlet 
{
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	{
		// Recupero della richiesta
		HttpSession session = request.getSession();
		
		// Differenzio il tipo di richiesta
		if (request.getParameter("richiediCodice") != null)
		{
			// Recupero di tutti i valori per la registrazione passati dall'utente
			try {
				String username = (String) request.getParameter("username");
				String password = (String) request.getParameter("password");
				String email = (String) request.getParameter("email");
				String fiatSigla = (String) request.getParameter("siglaFiat");
				String apiKey = (String) request.getParameter("apiKey");
				String apiSecret = (String) request.getParameter("apiSecret");
				String nomeTipoDeposito = (String) request.getParameter("tipoDeposito");
				TipoDeposito tipoDeposito = TipoDeposito.valueOf(nomeTipoDeposito);
				
				// Creazione dei controller
				ICodiceDiVerifica codiceController = new CodiceDiVerificaController();
				IRegistrazione registrazioneController = new RegistrazioneController(codiceController);
				codiceController.setRegistrazioneController(registrazioneController);
				
				// Salvataggio in sessione
				session.setAttribute("codiceController", codiceController);
				
				// Verifica dei dati e invio mail
				registrazioneController.verificaDatiInseriti(username, password, email, fiatSigla, apiKey, apiSecret, tipoDeposito);
			} catch (Exception e) {
				//doSomething
			}
		}
		else if (request.getParameter("signup") != null)
		{
			String codice = (String)request.getParameter("codice");
			ICodiceDiVerifica codiceController = (ICodiceDiVerifica)session.getAttribute("codiceController");
			
			// Verifico la correttezza del codice e registro l'utente
			codiceController.verificaCodice(codice);
		}
		else if (request.getParameter("login") != null)
		{
			// Ricavo variabili passate dall'utente e creo il controller per l'autenticazione
			String username = (String)request.getParameter("username");
			String password = (String)request.getParameter("password");
			IAutenticazione autenticazioneController = new AutenticazioneController();
			
			// Cerco di effettuare l'autenticazione dell'utente
			String result = autenticazioneController.autentica(username, password);
			Utente utente = autenticazioneController.getUtente();
			
			// Controllo se autenticazione andata a buon fine
			if (utente != null && !result.toUpperCase().contains("ERRORE")) 
			{
				// Salvataggio Utente in Sessione
				request.getSession().setAttribute("utente", utente);
				
				// Redirect alle view deciso dal Controller
				try {
					response.sendRedirect(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else
			{	
				// Errore nell'autenticazione
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/pages/ViewAutenticazione.jsp");
				request.setAttribute("errorMessage", result);
				
				try {
					requestDispatcher.forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
