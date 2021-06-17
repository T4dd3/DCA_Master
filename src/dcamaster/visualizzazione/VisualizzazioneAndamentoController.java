package dcamaster.visualizzazione;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;

import dcamaster.db.PersistenceException;
import dcamaster.model.RiepilogoOrdine;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.Utente;

public class VisualizzazioneAndamentoController implements IVisualizzazioneAndamento{

	private IVisualizzaRiepiloghi riepiloghiController;
	
	private Utente utente;
	
	public static final int INTERVALLI = 10;
	
	public VisualizzazioneAndamentoController(VisualizzaRiepiloghiController riepiloghiController, Utente utente) {
		this.riepiloghiController = riepiloghiController;
		this.utente = utente;
	}
	
	//La string è in realtà una mappa in Json (data-valore) + lista riepiloghi
	@Override
	public String drawAndList() 
	{
		Gson gson = new Gson();
		
		Map<LocalDateTime, Float> valoriIntervalli = new TreeMap<>(); 
		List<RiepilogoOrdine> riepiloghi = riepiloghiController.visualizza();
		
		//Controllo se è stato effettuato almeno un ordine
		if (riepiloghi != null && riepiloghi.size() > 0) 
		{
			//Prendo le date del primo riepilogo e di oggi
			LocalDateTime primoRiepilogo = riepiloghi.get(0).getData();
			LocalDateTime oggi = LocalDateTime.now();
			
			long secondsDifference = primoRiepilogo.until(oggi, ChronoUnit.SECONDS );
			
			for (int i = 1; i <= INTERVALLI; i++) 
			{
				LocalDateTime untilDate = primoRiepilogo.plusSeconds(secondsDifference - 10 - (secondsDifference / INTERVALLI * i));
				
				float valueUntilDate = 0.0f;
				try {
					valueUntilDate = utente.getDca().getValorePortafoglio(untilDate);
				} catch (PersistenceException e) {
					e.printStackTrace();
				}
				
				valoriIntervalli.put(untilDate, valueUntilDate);
			}
			
			String mappaJson = gson.toJson(valoriIntervalli);
			String listaJson = gson.toJson(riepiloghi);
			
			return "{\"riepiloghi\":" + listaJson + ",\"valoriGrafico\":" + mappaJson + ",\"esito\":\"OK\"}";
		}	
		else 
			return "{\"esito\":\"Nessun ordine effettuato!\"}";
	}

	@Override
	public String drawAndList(List<IFiltro> filtri) 
	{
		Gson gson = new Gson();
		
		Map<LocalDateTime, Float> valoriIntervalli = new TreeMap<>(); 
		List<RiepilogoOrdine> riepiloghi = riepiloghiController.visualizza(filtri);
		
		//Controllo se è stato effettuato almeno un ordine
		if (riepiloghi.size() > 0) 
		{
			//Prendo le date del primo riepilogo e di oggi
			LocalDateTime primoRiepilogo = riepiloghi.get(0).getData();
			LocalDateTime oggi = LocalDateTime.now();
			
			long secondsDifference = primoRiepilogo.until(oggi, ChronoUnit.SECONDS );
			
			for (int i = 1; i <= INTERVALLI; i++) 
			{
				LocalDateTime untilDate = primoRiepilogo.plusSeconds(secondsDifference - 10 - (secondsDifference / INTERVALLI * i));
				
				float valueUntilDate = StrategiaDCA.getValorePortafoglio(riepiloghi, untilDate, utente.getFiatScelta());
				
				valoriIntervalli.put(untilDate, valueUntilDate);
			}
			
			String mappaJson = gson.toJson(valoriIntervalli);
			String listaJson = gson.toJson(riepiloghi);
			
			return "{\"riepiloghi\":" + listaJson + ",\"valoriGrafico\":" + mappaJson + ",\"esito\":\"OK\"}";
		}
		else 
			return "{\"esito\":\"Nessun ordine effettuato!\"}";
	}

}
