package dcamaster.gestioneaccount;

import dcamaster.model.TipoDeposito;
import dcamaster.model.ValutaFiat;

public interface IRegistrazione {

	public void verificaDatiInseriti(String username, String password, String email, 
			ValutaFiat valutaRiferimento, String apiKey, String apiSecret, TipoDeposito tipoDeposito);
	
	public void registraUtente();
	
	public ICodiceDiVerifica getCodiceController(); //nuovo metodo
}
