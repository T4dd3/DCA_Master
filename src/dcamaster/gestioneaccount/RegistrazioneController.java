package dcamaster.gestioneaccount;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;
import dcamaster.db.ValutaFiatRepository;
import dcamaster.model.EntryOperazione;
import dcamaster.model.TipoDeposito;
import dcamaster.model.ValutaFiat;

@SuppressWarnings("serial")
public class RegistrazioneController extends HttpServlet implements IRegistrazione
{
	private ICodiceDiVerifica codiceController;
	private ControllerPersistenza controllerPersistenza;
	private HttpSession session;
	private ValutaFiatRepository fiatRepo;
	
	@Override
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		
		// Istanza del singleton di Controller Persistenza
		this.controllerPersistenza = ControllerPersistenza.getInstance();

		// Creazione Controller per gestione codice verifica
		this.codiceController = new CodiceDiVerificaController(this);
		
		this.fiatRepo = new ValutaFiatRepository(this.controllerPersistenza);
	}
	
	public ICodiceDiVerifica getCodiceController() 
	{
		return codiceController;
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	{
		// 
		session = request.getSession();
		
		// Recupero di tutti i valori per la registrazione passati dall'utente
		try {
			String username = (String) request.getParameter("username");
			String password = (String) request.getParameter("password");
			String email = (String) request.getParameter("email");
			String fiatSigla = (String) request.getParameter("siglaFiat");
			ValutaFiat valutaRiferimento = fiatRepo.readBySigla(fiatSigla);
			String apiKey = (String) request.getParameter("apiKey");
			String apiSecret = (String) request.getParameter("apiSecret");
			String nomeTipoDeposito = (String) request.getParameter("tipoDeposito");
			TipoDeposito tipoDeposito = TipoDeposito.valueOf(nomeTipoDeposito);
			
			this.verificaDatiInseriti(username, password, email, valutaRiferimento, apiKey, apiSecret, tipoDeposito);
		} catch (Exception e) {
			//doSomething
		}
	}
	
	// Verifico che gli input siano corretti, se lo sono li salvo in sessione e invio il codice
	@Override
	public void verificaDatiInseriti(String username, String password, String email, ValutaFiat valutaRiferimento,
			String apiKey, String apiSecret, TipoDeposito tipoDeposito) 
	{
		byte[] array = new byte[32];  
		new Random().nextBytes(array);
		String saltPassword = new String(array, Charset.forName("UTF-8"));
		String data = password + saltPassword;
		MessageDigest mda;
		String hashPassword = "";
		
		try {
			// Generate digest as byte array
			mda = MessageDigest.getInstance("SHA-512", "SUN");
			byte[] byteDigest = mda.digest(data.getBytes());
			
			// Convert byte array digest to String
			StringBuilder sb = new StringBuilder();
		    for(int i=0; i < byteDigest.length;i++)
		        sb.append(Integer.toString((byteDigest[i] & 0xff) + 0x100, 16).substring(1));
		    hashPassword = sb.toString();
		    
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		
		/* TODO: VERIFICA DEI DATI INSERITI */
		boolean esitoVerifica = true;
		
		if (esitoVerifica)
		{
			// Salvataggio valori della registrazione in Sessione
			session.setAttribute("username", username);
			session.setAttribute("password", hashPassword);
			session.setAttribute("email", email);
			session.setAttribute("apiSecret", apiSecret);
			session.setAttribute("apiKey", apiKey);
			session.setAttribute("valutaFiat", valutaRiferimento);
			session.setAttribute("tipoDeposito", tipoDeposito);
			session.setAttribute("saltPassword", saltPassword);
			
			// Invio del codice per mail all'utente e salvataggio in sessione
			String codice = this.codiceController.inviaCodice(email);
			session.setAttribute("codice", codice);
		}
	}

	@Override
	public void registraUtente() 
	{
		UserRepository userRepo = new UserRepository(this.controllerPersistenza);
		String username = (String) session.getAttribute("username");
		String hashPassword = (String) session.getAttribute("password");
		String email = (String) session.getAttribute("email");
		ValutaFiat valutaRiferimento = (ValutaFiat) session.getAttribute("valutaFiat");
		String apiKey = (String) session.getAttribute("apiKey");
		String apiSecret = (String) session.getAttribute("apiSecret");
		TipoDeposito tipoDeposito = (TipoDeposito) session.getAttribute("tipoDeposito");
		String saltPassword = (String) session.getAttribute("saltPassword");
		
		try {
			userRepo.create(username, hashPassword, saltPassword, email, valutaRiferimento, apiKey, apiSecret, tipoDeposito);
		} catch (PersistenceException e) {
			//do something
		}
		// Inserire entry nei log
	}

	
	public void inserisciEntry(EntryOperazione entry) 
	{
		this.controllerPersistenza.inserisciEntry(entry);
	}

}
