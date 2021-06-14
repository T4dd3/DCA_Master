package dcamaster.db;

import dcamaster.model.ValutaFiat;

public class ValutaFiatRepository {

	private ControllerPersistenza controller;
	
	//TABLE VALUTEFIAT-------------------------------------------------------------------------------------
	
	private static final String TABLE_VALUTEFIAT = "valuteFiat";
	
	private static final String SIGLAVALUTAFIAT = "sigla";
	private static final String NOMEVALUTAFIAT = "nome";
	
	//====================================================================================================
	
	public ValutaFiatRepository(ControllerPersistenza controller) {
		this.controller = controller;
	}
	
	public ValutaFiat readBySigla(String sigla) {
		ValutaFiat result = new ValutaFiat();
		//toDo
		return result;
	}
}
