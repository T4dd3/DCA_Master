package dcamaster.visualizzazione;

import java.time.LocalDateTime;

import dcamaster.model.RiepilogoOrdine;

public class FiltroIntervallo implements IFiltro{

	private LocalDateTime dataInizio;
	private LocalDateTime dataFine;
	
	
	public FiltroIntervallo(LocalDateTime dataInizio, LocalDateTime dataFine) {
		super();
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
	}


	@Override
	public boolean filtra(RiepilogoOrdine riepilogoOrdine) {
		return (this.dataInizio.isBefore(riepilogoOrdine.getData()) && this.dataFine.isAfter(riepilogoOrdine.getData()));
	}

}
