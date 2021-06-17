package dcamaster.visualizzazione;

import java.util.List;

public interface IVisualizzazioneAndamento {

	public String drawAndList();
	
	public String drawAndList(List<IFiltro> filtri);
}
