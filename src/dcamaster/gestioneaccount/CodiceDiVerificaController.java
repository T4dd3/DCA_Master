package dcamaster.gestioneaccount;

public class CodiceDiVerificaController implements ICodiceDiVerifica{

	private IRegistrazione registrazioneController;
	
	public CodiceDiVerificaController(IRegistrazione registrazioneController) {
		this.registrazioneController = registrazioneController;
		
	}

	@Override
	public void setRegistrazioneController(IRegistrazione registrazioneController) {
		this.registrazioneController = registrazioneController;
	}

	@Override
	public String inviaCodice(String email) { 	//nuovo valore restituito
		String result = "";
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public void verificaCodice(String codice) {
		// TODO Auto-generated method stub
	}

}
