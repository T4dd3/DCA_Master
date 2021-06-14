package dcamaster.gestioneaccount;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;
import dcamaster.db.ValutaFiatRepository;
import dcamaster.model.EntryOperazione;
import dcamaster.model.TipoDeposito;
import dcamaster.model.ValutaFiat;

public class RegistrazioneController extends HttpServlet implements IRegistrazione{

	private static final long serialVersionUID = 1L;
	private ICodiceDiVerifica codiceController;
	private ControllerPersistenza controllerPersistenza;
	private HttpSession session;
	private ValutaFiatRepository fiatRepo;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		//controller persistenza come singleton
		//this.controllerPersistenza = ControllerPersistenza.getInstance();
		this.codiceController = new CodiceDiVerificaController(this);
		//codiceController.setRegistrazioneController();
		this.fiatRepo = new ValutaFiatRepository(this.controllerPersistenza);
	}
	
	public ICodiceDiVerifica getCodiceController() {
		return codiceController;
	}
	
	@Override
	public void verificaDatiInseriti(String username, String password, String email, ValutaFiat valutaRiferimento,
			String apiKey, String apiSecret, TipoDeposito tipoDeposito) {
		//verifico che gli input siano corretti, se lo sono li salvo in sessione e invio il codice
		byte[] array = new byte[10];  
		new Random().nextBytes(array);
		String saltPassword = new String(array, Charset.forName("UTF-8"));
		String data = password + saltPassword;
		MessageDigest mda;
		String hashPassword = "";
		
		try {
			mda = MessageDigest.getInstance("SHA-512", "BC");
			hashPassword = new String(mda.digest(data.getBytes()), StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		
		session.setAttribute("username", username);
		session.setAttribute("password", hashPassword);
		session.setAttribute("email", email);
		session.setAttribute("apiSecret", apiSecret);
		session.setAttribute("apiKey", apiKey);
		session.setAttribute("valutaFiat", valutaRiferimento);
		session.setAttribute("tipoDeposito", tipoDeposito);
		session.setAttribute("saltPassword", saltPassword);
		this.codiceController.inviaCodice(email);
	}

	@Override
	public void registraUtente() {
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
		} catch (PersistenceException e){
			//do something
		}
		//Inserire entry nei log
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		session = request.getSession();
		String username = (String) request.getAttribute("username");
		String password = (String) request.getAttribute("password");
		String email = (String) request.getAttribute("email");
		String fiatSigla = (String) request.getAttribute("siglaFiat");
		ValutaFiat valutaRiferimento = fiatRepo.readBySigla(fiatSigla);
		String apiKey = (String) request.getAttribute("apiKey");
		String apiSecret = (String) request.getAttribute("apiSecret");
		String nomeTipoDeposito = (String) request.getAttribute("tipoDeposito");
		TipoDeposito tipoDeposito = TipoDeposito.valueOf("");
		this.verificaDatiInseriti(username, password, email, 
					valutaRiferimento, apiKey, apiSecret, tipoDeposito);
		
	}
	
	public void inserisciEntry(EntryOperazione entry) {
		this.controllerPersistenza.inserisciEntry(entry);
	}

}
