package dcamaster.gestioneaccount;

public interface ICodiceDiVerifica {

	public void inviaCodice(String email);
	
	public void verificaCodice(String codice);
	
	public void setRegistrazioneController(IRegistrazione registrazioneController);
}
