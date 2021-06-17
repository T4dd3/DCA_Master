package dcamaster.gestioneaccount;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.UserRepository;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.Utente;

public class AutenticazioneController implements IAutenticazione
{
	// Utente se autenticato
	private Utente utente;
	public Utente getUtente() {
		return this.utente;
	}
	
	private ControllerPersistenza controllerPersistenza;
	
	public AutenticazioneController()
	{
		// Istanza del singleton di Controller Persistenza
		this.controllerPersistenza = ControllerPersistenza.getInstance();
	}
	
	@Override
	public String autentica(String username, String password) 
	{
		UserRepository repo = new UserRepository(controllerPersistenza);
		String ritorno = "ERRORE";
		
		try {
			this.utente = repo.read(username, password);
			
			if (this.utente != null) 
			{
				// Recupero strategiaDCA e forward alla pagina corretta
				StrategiaDCA strategiaDCA = utente.getDca();
				
				// Decido a quale pagina dovrò fare redirect
				if(strategiaDCA.getBudget() == null || 
					strategiaDCA.getIntervalloInvestimento() == null || 
					strategiaDCA.getDistribuzionePercentuale() == null)
						ritorno = "./pages/HomeConfigurazione.jsp";
				else
					ritorno = "./pages/ViewVisualizzazioneAndamento.jsp";
			}
			else
				ritorno = "ERRORE: Le credenziali inserite non sono valide";
		} 
		catch (Exception e) {
			ritorno = "ERRORE: Server-Side error";
		}
		
		return ritorno;
	}

}
