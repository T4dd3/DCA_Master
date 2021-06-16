package dcamaster.visualizzazione;

import dcamaster.model.RiepilogoOrdine;

public class FiltroSpesa implements Filtro{

	private float spesaMax;		//nella progettazione era un int
	
	public FiltroSpesa(float spesaMax) {
		super();
		this.spesaMax = spesaMax;
	}

	@Override
	public boolean filtra(RiepilogoOrdine riepilogoOrdine) {
		return this.spesaMax <= riepilogoOrdine.getFiatSpesa();
	}

}
