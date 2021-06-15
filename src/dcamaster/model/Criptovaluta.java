package dcamaster.model;
import java.time.LocalDateTime;
import java.util.Map;

public class Criptovaluta 
{
	private String sigla;
	private String nome;
	private Map<LocalDateTime, Map<ValutaFiat, Float>> intervalliAggiornamento;
	
	protected boolean isLoaded;
	
	public Criptovaluta() {
		this.isLoaded = false;
	}
	
	public String getSigla() {
		return sigla;
	}



	public void setSigla(String sigla) {
		this.sigla = sigla;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public Map<LocalDateTime, Map<ValutaFiat, Float>> getIntervalliAggiornamento() {
		return intervalliAggiornamento;
	}



	public void setIntervalliAggiornamento(Map<LocalDateTime, Map<ValutaFiat, Float>> intervalloAggiornamento) {
		this.intervalliAggiornamento = intervalloAggiornamento;
	}



	public Float getValore(ValutaFiat valuta, LocalDateTime date) {
		Map<ValutaFiat, Float> mapA = this.intervalliAggiornamento.get(date);
		Float result = mapA.get(valuta);
		return result;
	}
	
	protected void isLoaded(Boolean bool) {
		this.isLoaded = bool;
	}
}
