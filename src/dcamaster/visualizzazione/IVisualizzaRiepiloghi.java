package dcamaster.visualizzazione;

import java.util.List;

import dcamaster.model.RiepilogoOrdine;

public interface IVisualizzaRiepiloghi {

	public List<RiepilogoOrdine> visualizza();
	
	public List<RiepilogoOrdine> visualizza(List<IFiltro> filtri);
}
