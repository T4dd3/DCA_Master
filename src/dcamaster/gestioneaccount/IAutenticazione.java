package dcamaster.gestioneaccount;

import dcamaster.model.Utente;

public interface IAutenticazione 
{
	public String autentica(String username, String password);
	
	public Utente getUtente();
}
