package dcamaster.model;

import java.util.List;
import java.util.Map;

public class StrategiaDCA {

	private float budget;
	private int intervalloInvestimento;
	private Utente utente;
	private Map<Criptovaluta, Float> distribuzionePercentuale;
	private List<RiepilogoOrdine> reipiloghiOrdine;
	
	public StrategiaDCA() {
		
	}

	public float getBudget() {
		return budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public int getIntervalloInvestimento() {
		return intervalloInvestimento;
	}

	public void setIntervalloInvestimento(int intervalloInvestimento) {
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
		return reipiloghiOrdine;
	}

	public void setReipiloghiOrdine(List<RiepilogoOrdine> reipiloghiOrdine) {
		this.reipiloghiOrdine = reipiloghiOrdine;
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
