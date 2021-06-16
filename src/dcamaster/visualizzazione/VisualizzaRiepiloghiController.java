package dcamaster.visualizzazione;

import java.util.ArrayList;
import java.util.List;

import dcamaster.model.RiepilogoOrdine;
import dcamaster.model.Utente;

public class VisualizzaRiepiloghiController implements IVisualizzaRiepiloghi{
	
	private Utente utente;

	public VisualizzaRiepiloghiController(Utente utente) {
		this.utente = utente;
	}
	
	@Override
	public List<RiepilogoOrdine> visualizza() 
	{
		return utente.getDca().getReipiloghiOrdine();
	}

	@Override
	public List<RiepilogoOrdine> visualizza(List<Filtro> filtri) 
	{
		
		//Preparo la lista da tornare e la lista dei riepiloghi dell'utente
		List<RiepilogoOrdine> ritorno = new ArrayList<>();
		List<RiepilogoOrdine> riepiloghiUtente = utente.getDca().getReipiloghiOrdine();
		
		//Variabile per indicare se i filtri vengono rispettati
		boolean valid = true;
		
		//Itero su tutti i riepiloghi dell'utente
		for (RiepilogoOrdine ro : riepiloghiUtente) 
		{
			//Itero su ogni filtro che viene passato in input
			for (Filtro filtro : filtri) 
			{
				//Se il filtro mi restituisce false, esco da questo ciclo
				if (!(filtro.filtra(ro))) 
				{
					valid = false;
					break;
				}
			}
			
			//Se all'uscita del ciclo dei filtri valid è true, il Riepilogo è ok
			if (valid)
				ritorno.add(ro);
			
			//Re-inizializzo la variabile di controllo a true
			valid = true;
		}
		
		return ritorno;
	}

	
}
