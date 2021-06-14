package dcamaster.visualizzazione;

import java.util.List;
import java.util.Map;

import dcamaster.model.Criptovaluta;

public class VisualizzazioneAndamentoController implements IVisualizzazioneAndamento{

	private IVisualizzaRiepiloghi riepiloghiController;
	
	public VisualizzazioneAndamentoController(VisualizzaRiepiloghiController riepiloghiController) {
		this.riepiloghiController = riepiloghiController;
	}
	
	@Override
	public void drawAndList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawAndList(List<Filtro> filtri) {
		// TODO Auto-generated method stub
		
	}

}
