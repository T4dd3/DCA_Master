package dcamaster.gestioneaccount;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;
import dcamaster.model.EntryOperazione;
import dcamaster.model.TipoDeposito;
import dcamaster.model.ValutaFiat;
import dcamaster.model.ValutaFiatFactory;

public class RegistrazioneController implements IRegistrazione
{
	// CodiceController associato
	private ICodiceDiVerifica codiceController;

	// Per dialogare con la persistenza
	private ControllerPersistenza controllerPersistenza;
	
	// Attributi associati all'utente
	private String username;
	private String password;
	private String email;
	private String apiSecret;
	private String apiKey;
	private ValutaFiat valutaFiat;
	private String saltPassword;
	private TipoDeposito tipoDeposito;
	
	public RegistrazioneController(ICodiceDiVerifica codiceController) 
	{
		this.codiceController = codiceController;
		
		// Istanza del singleton di Controller Persistenza
		this.controllerPersistenza = ControllerPersistenza.getInstance();
	}
	
	// Verifico che gli input siano corretti, se lo sono li salvo in sessione e invio il codice
	@Override
	public void verificaDatiInseriti(String username, String password, String email, String valutaRiferimento,
			String apiKey, String apiSecret, TipoDeposito tipoDeposito) 
	{
		// Generazione hash e salt della password
		byte[] array = new byte[32];  
		new Random().nextBytes(array);
		String saltPassword = new String(array, Charset.forName("UTF-8"));
		String data = password + saltPassword;
		MessageDigest mda;
		String hashPassword = "";
		// Generate digest as byte array
		try {
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
			// Salvataggio valori della registrazione
			this.username = username;
			this.password = hashPassword;
			this.email = email;
			this.apiSecret = apiSecret;
			this.apiKey = apiKey;
			this.valutaFiat = ValutaFiatFactory.GetValutaFiat(valutaRiferimento);
			this.tipoDeposito = tipoDeposito;
			this.saltPassword = saltPassword;
			
			// Invio del codice per mail all'utente e salvataggio in sessione
			this.codiceController.inviaCodice(email);
		}
	}

	@Override
	public String registraUtente() 
	{
		String esitoRegistrazione = "ERRORE";
		UserRepository userRepo = new UserRepository(this.controllerPersistenza);
		
		try {
			userRepo.create(this.username, this.password, this.saltPassword, this.email, this.valutaFiat, this.apiKey, this.apiSecret, this.tipoDeposito);
		} catch (PersistenceException e) {
			esitoRegistrazione = "ERRORE: Inserimento nel database fallito!";
			return esitoRegistrazione;
		}
		
		esitoRegistrazione = "./pages/ViewAutenticazione.jsp";
		
		return esitoRegistrazione;
	}

	
	public void inserisciEntry(EntryOperazione entry) 
	{
		this.controllerPersistenza.inserisciEntry(entry);
	}

}
