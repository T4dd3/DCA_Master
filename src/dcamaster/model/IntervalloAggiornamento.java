package dcamaster.model;
import java.time.LocalDateTime;

public class IntervalloAggiornamento {

	private LocalDateTime dataOra;
	private float valoreConversione;
	private String siglaValutaFiat;
	private String siglaCriptovaluta;
	
	
	public IntervalloAggiornamento(LocalDateTime dataOra, float valoreConversione, String siglaValutaFiat,
		String siglaCriptovaluta) {
		this.dataOra = dataOra;
		this.valoreConversione = valoreConversione;
		this.siglaValutaFiat = siglaValutaFiat;
		this.siglaCriptovaluta = siglaCriptovaluta;
	}
	
	public LocalDateTime getDataOra() {
		return dataOra;
	}
	public void setDataOra(LocalDateTime dataOra) {
		this.dataOra = dataOra;
	}
	public float getValoreConversione() {
		return valoreConversione;
	}
	public void setValoreConversione(float valoreConversione) {
		this.valoreConversione = valoreConversione;
	}
	public String getSiglaValutaFiat() {
		return siglaValutaFiat;
	}
	public void setSiglaValutaFiat(String siglaValutaFiat) {
		this.siglaValutaFiat = siglaValutaFiat;
	}
	public String getSiglaCriptovaluta() {
		return siglaCriptovaluta;
	}
	public void setSiglaCriptovaluta(String siglaCriptovaluta) {
		this.siglaCriptovaluta = siglaCriptovaluta;
	}
	
	
}
