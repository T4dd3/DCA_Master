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
			//Tutti gli intervalli presenti per l'associazione tra Criptovaluta del Riepilogo e la Valuta fiat dell'utente
			Map <LocalDateTime, Float> intervalliCriptoRiepilogoEValutaFiat = riepilogo.getCriptovaluta().getIntervalliAggiornamento().get(valutaFiat);
			// Lista delle sole date ordinate
			Set<LocalDateTime> dateAggiornamento = new TreeSet<>(intervalliCriptoRiepilogoEValutaFiat.keySet());
			
			// Data più vicina alla data passata dall'utente
			LocalDateTime nearestDateRiepilogo = dateAggiornamento.stream().min(Comparator.comparingLong(x -> ChronoUnit.SECONDS.between(x , date))).orElse(null);
			
			// Calcolo del valore, alla data passata, per il riepilogo
			float valore = riepilogo.getQuantitativoAcquistato() * intervalliCriptoRiepilogoEValutaFiat.get(nearestDateRiepilogo);
			risultato += valore;
		}
		
		return risultato;
	}
}
