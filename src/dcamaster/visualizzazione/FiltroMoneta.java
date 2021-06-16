package dcamaster.visualizzazione;

import dcamaster.model.Criptovaluta;
import dcamaster.model.RiepilogoOrdine;

public class FiltroMoneta implements Filtro{

	private Criptovaluta criptovaluta;
	
	public FiltroMoneta(Criptovaluta criptovaluta) {
		this.criptovaluta = criptovaluta;
	}
	@Override
	public boolean filtra(RiepilogoOrdine riepilogoOrdine) {
		return this.criptovaluta.getSigla().equals(riepilogoOrdine.getCriptovaluta().getSigla());
	}
}
