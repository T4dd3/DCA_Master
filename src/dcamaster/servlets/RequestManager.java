package dcamaster.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import dcamaster.gestioneaccount.AutenticazioneController;
import dcamaster.gestioneaccount.CodiceDiVerificaController;
import dcamaster.gestioneaccount.IAutenticazione;
import dcamaster.gestioneaccount.ICodiceDiVerifica;
import dcamaster.gestioneaccount.IRegistrazione;
import dcamaster.gestioneaccount.RegistrazioneController;
import dcamaster.gestionedca.ConfigurazionePortafoglioController;
import dcamaster.model.Criptovaluta;
import dcamaster.model.TipoDeposito;
import dcamaster.model.Utente;

@SuppressWarnings("serial")
public class RequestManager extends HttpServlet 
{
	Gson gson;
	
	@Override
	public void init() throws ServletException 
	{
		super.init();
		gson = new Gson();
	}
	
	
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
				
				// Torna alla ViewRegistrazione per inserire il codice
				//response.sendRedirect("./pages/ViewRegistrazione.jsp");
				
				
				response.getWriter().println("OK");
				
			} catch (Exception e) {
				//doSomething
			}
		}
		else if (request.getParameter("signup") != null)
		{
			String codice = (String)request.getParameter("codice");
			ICodiceDiVerifica codiceController = (ICodiceDiVerifica)session.getAttribute("codiceController");
			
			// Verifico la correttezza del codice e registro l'utente
			String esito = codiceController.verificaCodice(codice);
			
			// Codice esatto
			if (!esito.contains("ERRORE")) 
			{
				try {
					response.sendRedirect(esito);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// Codice errato o fallimento DB
			else 
			{
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/pages/ViewRegistrazione.jsp");
				request.setAttribute("errorMessage", esito);
				
				try {
					requestDispatcher.forward(request, response);
				} catch (ServletException | IOException e) {
					e.printStackTrace();
				}
			}
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
		else if (request.getParameter("criptovaluteAcquistabili") != null)
		{
			//Creazione e inizializzazione controller
			Utente utente = (Utente) session.getAttribute("utente");
			ConfigurazionePortafoglioController configurazionePortafoglio = new ConfigurazionePortafoglioController(utente);
			
			// Preparazione della mappa da restituire di tipo <String, Float> cioè <siglaCripto, percentuale>
			Map<Criptovaluta, Float> criptoAcquistabiliPiuDistribuzione = configurazionePortafoglio.getCriptovaluteAcquistabili();
			Map<String, Float> criptoAcquistabiliPiuDistribuzioneSigla = criptoAcquistabiliPiuDistribuzione.keySet().stream().
					collect(Collectors.toMap(Criptovaluta::getSigla, cripto -> criptoAcquistabiliPiuDistribuzione.get(cripto)));
			
			// Preparazione del json da inviare all'utente
			String jsonMappa = gson.toJson(criptoAcquistabiliPiuDistribuzioneSigla);
			
			// Restituzione risposta all'utente
			try {
				response.getWriter().println(jsonMappa);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
