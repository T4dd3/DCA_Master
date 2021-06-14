package dcamaster.gestioneaccount;

public interface ICodiceDiVerifica {

	public String inviaCodice(String email);	//nuovo valore restituito
	
	public void verificaCodice(String codice);
	
	public void setRegistrazioneController(IRegistrazione registrazioneController); //nuovo metodo
}
