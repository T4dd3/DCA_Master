package dcamaster.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dcamaster.gestioneaccount.AutenticazioneController;
import dcamaster.gestioneaccount.CodiceDiVerificaController;
import dcamaster.gestioneaccount.IAutenticazione;
import dcamaster.gestioneaccount.ICodiceDiVerifica;
import dcamaster.gestioneaccount.IRegistrazione;
import dcamaster.gestioneaccount.RegistrazioneController;
import dcamaster.gestionedca.ConfigurazionePortafoglioController;
import dcamaster.gestionedca.SceltaParametriController;
import dcamaster.model.Criptovaluta;
import dcamaster.model.CriptovalutaFactory;
import dcamaster.model.TipoDeposito;
import dcamaster.model.Utente;
import dcamaster.visualizzazione.IFiltro;
import dcamaster.visualizzazione.FiltroIntervallo;
import dcamaster.visualizzazione.FiltroMoneta;
import dcamaster.visualizzazione.FiltroSpesa;
import dcamaster.visualizzazione.VisualizzaRiepiloghiController;
import dcamaster.visualizzazione.VisualizzazioneAndamentoController;

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
	
	
	@SuppressWarnings("null")
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	{
		// Recupero della sessione e utente
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
				response.setContentType("application/json");
				response.getWriter().println(jsonMappa);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (request.getParameter("sceltaParametri") != null) 
		{
			//Inizializzo il controller apposito
			Utente utente = (Utente) session.getAttribute("utente");
			SceltaParametriController sceltaParametri = new SceltaParametriController(utente);
			
			// Parametri passati dall'utente
			String budget = request.getParameter("budget");
			String intervallo = request.getParameter("intervallo");
			
			// Controllo sui parametri e valorizzazione se validi
			if (budget != null && !budget.isEmpty())
				sceltaParametri.sceltaBudget(Float.parseFloat(budget));
			if (intervallo != null && !intervallo.isEmpty())
				sceltaParametri.sceltaIntervalloInvestimento(Integer.parseInt(intervallo));
			
			try {
				response.sendRedirect("./pages/HomeConfigurazione.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (request.getParameter("distribuzionePercentuale") != null)
		{
			//Creazione e inizializzazione controller
			Utente utente = (Utente) session.getAttribute("utente");
			ConfigurazionePortafoglioController configurazionePortafoglio = new ConfigurazionePortafoglioController(utente);
			
			// Recupero json della nuova distribuzionePercentuale
			String jsonDistribuzione = (String)request.getParameter("distribuzionePercentuale");
			// Conversione json come mappa (FORMATO RICHIESTO: {'BTC': 15.5, 'ETH': 85.5})
			Type type = new TypeToken<Map<String, Float>>(){}.getType();
			Map<String, Float> nuovaDistribuzioneSigle = gson.fromJson(jsonDistribuzione, type);
			
			// Risposta come json
			response.setContentType("application/json");
			
			// Controllo che la somma dei valori sia 100 e non ci siano percentuali <0
			double somma = nuovaDistribuzioneSigle.values().stream().mapToDouble(f -> f).sum();
			try 
			{
				if (somma != 100) {
					response.getWriter().println("{\"esito\":\"La percentuale totale deve essere uguale a 100!\"}");
					return;
				}
				if (nuovaDistribuzioneSigle.values().stream().filter(f -> f < 0).count() > 0) {
					response.getWriter().println("{\"esito\":\"La percentuale assegnata deve essere maggiore o uguale a 0!\"}");
					return;
				}
			
				// Creazione distribuzione percentuale
				Map<Criptovaluta, Float> nuovaDistribuzioneCriptovalute = nuovaDistribuzioneSigle.keySet().stream()
										.collect(Collectors.toMap(CriptovalutaFactory::GetCriptovaluta, sigla -> nuovaDistribuzioneSigle.get(sigla)));
				
				// Salvataggio distribuzionePercentuale e reload pagina
				configurazionePortafoglio.configuraPortafoglio(nuovaDistribuzioneCriptovalute);
				response.getWriter().println("{\"esito\":\"Distribuzione percentuale aggiornata con successo!\"}");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (request.getParameter("visualizzazioneAndamento") != null)
		{
			// Creazione controller
			Utente utente = (Utente) session.getAttribute("utente");
			VisualizzaRiepiloghiController riepiloghiController = new VisualizzaRiepiloghiController(utente);
			VisualizzazioneAndamentoController andamentoController = new VisualizzazioneAndamentoController(riepiloghiController, utente);
			PrintWriter writerResponse = null;
			
			try 
			{
				// Risposta come json e inizializzazione writer
				writerResponse = response.getWriter();
				response.setContentType("application/json");
				
				// Parametro per diversificare se filtrare o no
				String isFilter = request.getParameter("filtri");
				
				// Separazione caso con e senza filtri passati
				if (isFilter != null && isFilter.equals("1"))
				{
					// Filtri passati dall'utente da convertire
					String criptovaluta = request.getParameter("criptovaluta");
					String startDate = request.getParameter("startDate");
					String endDate = request.getParameter("endDate");
					String spesa = request.getParameter("spesa");
					// Lista di IFiltro per applicare pattern Strategy
					List<IFiltro> filtri = new ArrayList<>();
					
					// Creazione dei filtri e aggiunta alla lista
					if (criptovaluta != null && !criptovaluta.isBlank())
						filtri.add(new FiltroMoneta(CriptovalutaFactory.GetCriptovaluta(criptovaluta)));
					if (startDate != null && !startDate.isBlank() && endDate != null && !endDate.isBlank())
						filtri.add(new FiltroIntervallo(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate)));
					if (spesa != null && !spesa.isBlank())
						filtri.add(new FiltroSpesa(Float.parseFloat(spesa)));
					
					// Invio risposta all'utente
					writerResponse.println(andamentoController.drawAndList(filtri));
				}
				else
					writerResponse.println(andamentoController.drawAndList());
			} catch (IOException e) {
				writerResponse.println("{\"esito\":\"Errore nella creazione dei filtri!\"}");
				e.printStackTrace();
			}
		}
	}
}
