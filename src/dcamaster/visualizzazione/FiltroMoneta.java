package dcamaster.visualizzazione;

import dcamaster.model.Criptovaluta;
import dcamaster.model.RiepilogoOrdine;

public class FiltroMoneta implements IFiltro{

	private Criptovaluta criptovaluta;
	
	public FiltroMoneta(Criptovaluta criptovaluta) {
		this.criptovaluta = criptovaluta;
	}
	@Override
	public boolean filtra(RiepilogoOrdine riepilogoOrdine) {
		return this.criptovaluta.equals(riepilogoOrdine.getCriptovaluta());
	}
}
