package dcamaster.gestioneaccount;

import dcamaster.model.TipoDeposito;

public interface IRegistrazione 
{
	public void verificaDatiInseriti(String username, String password, String email, 
			String valutaRiferimento, String apiKey, String apiSecret, TipoDeposito tipoDeposito);
	
	public String registraUtente();
}
