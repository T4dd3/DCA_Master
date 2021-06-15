package dcamaster.model;

import java.util.List;
import java.util.Map;

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
	
	/*public float getValorePortafoglio() {
		float result;
		
		return result;
	}
	
	public static float getValorePortafoglio(List<RiepilogoOrdine> ordini, LocalDateTime date, ValutaFiat valuta) {
		float result;
		
		return result;
	}*/
}
