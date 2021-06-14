package dcamaster.gestioneaccount;

import dcamaster.model.Utente;

public interface IAutenticazione {

	public Utente autentica(String username, String password);
}
