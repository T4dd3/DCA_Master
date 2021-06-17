package dcamaster.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;

public class StrategiaDCA 
{
	private Float budget;
	private Integer intervalloInvestimento;
	private Utente utente;
	private Map<Criptovaluta, Float> distribuzionePercentuale;
	private List<RiepilogoOrdine> riepiloghiOrdine;
	
	protected boolean distribuzioneLoaded;
	protected boolean riepiloghiLoaded;
	
	public StrategiaDCA() {
		this.distribuzioneLoaded = false;
		this.riepiloghiLoaded = false;
	}

	public Float getBudget() {
		return budget;
	}

	public void setBudget(Float budget) {
		this.budget = budget;
	}

	public Integer getIntervalloInvestimento() {
		return intervalloInvestimento;
	}

	public void setIntervalloInvestimento(Integer intervalloInvestimento) {
		this.intervalloInvestimento = intervalloInvestimento;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Map<Criptovaluta, Float> getDistribuzionePercentuale() {
		return distribuzionePercentuale;
	}

	public void setDistribuzionePercentuale(Map<Criptovaluta, Float> distribuzionePercentuale) {
		this.distribuzionePercentuale = distribuzionePercentuale;
	}

	public List<RiepilogoOrdine> getReipiloghiOrdine() {
		return riepiloghiOrdine;
	}

	public void setReipiloghiOrdine(List<RiepilogoOrdine> reipiloghiOrdine) {
		this.riepiloghiOrdine = reipiloghiOrdine;
	}
	
	protected void distribuzioneLoaded(Boolean bool) {
		this.distribuzioneLoaded = bool;
	}
	
	protected void riepiloghiLoaded(Boolean bool) {
		this.riepiloghiLoaded = bool;
	}
	
	public float getValorePortafoglio(LocalDateTime dateTime) throws PersistenceException {
		UserRepository repo = new UserRepository(ControllerPersistenza.getInstance());
		return repo.getValorePortafoglio(this.utente.getUsername(), dateTime);
	}
	
	public static float getValorePortafoglio(List<RiepilogoOrdine> riepiloghi, LocalDateTime date, ValutaFiat valutaFiat) 
	{
		
		float risultato = 0;
		
		for (RiepilogoOrdine riepilogo : riepiloghi) 
		{
			//Valore della criptovaluta al momento specificato
			//float valore = riepilogo.getCriptovaluta().getIntervalliAggiornamento().get(date).get(valutaFiat);
	
			/*Set<LocalDateTime> intervalliValutaFiat = riepilogo.getCriptovaluta().getIntervalliAggiornamento().get(valutaFiat);
			
			for (LocalDateTime dataSet : keySet) 
			{
				if (dataSet.isBefore(date)) 
				{
					//float valore = riepilogo.getCriptovaluta().getIntervalliAggiornamento().get(dataSet).get(valutaFiat);
					Criptovaluta cripto = riepilogo.getCriptovaluta();
					Map<LocalDateTime, Map<ValutaFiat, Float>> intervalli = cripto.getIntervalliAggiornamento();
					Map<ValutaFiat, Float> valuteFiat = intervalli.get(dataSet);
					float valore = valuteFiat.get(valutaFiat);
					
					risultato += valore * riepilogo.getQuantitativoAcquistato();
				}
			}*/
			
			//Tutti gli intervalli presenti per quella specifica valuta fiat
			Set<LocalDateTime> dateAggiornamento = new TreeSet<>(riepilogo.getCriptovaluta().getIntervalliAggiornamento().get(valutaFiat).keySet());
			
			LocalDateTime nearestDateRiepilogo = dateAggiornamento.stream().min(Comparator.comparingLong(x -> ChronoUnit.SECONDS.between(x , date))).orElse(null);
			
			float valore = riepilogo.getQuantitativoAcquistato() * riepilogo.getCriptovaluta().getIntervalliAggiornamento().get(valutaFiat).get(nearestDateRiepilogo);
			
			risultato += valore;
		}
		
		return risultato;
	}
}
