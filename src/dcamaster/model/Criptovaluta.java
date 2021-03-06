package dcamaster.model;
import java.time.LocalDateTime;
import java.util.Map;

public class Criptovaluta 
{
	private String sigla;
	private String nome;
	private Map<ValutaFiat, Map<LocalDateTime, Float>> intervalliAggiornamento;
	
	//protected boolean intervalliLoaded;
	
	public Criptovaluta() {
		//this.intervalliLoaded = false;
	}
	
	/*protected void intervalliLoaded(Boolean bool) {
		this.intervalliLoaded = bool;
	}*/
	
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

	public Map<ValutaFiat, Map<LocalDateTime, Float>> getIntervalliAggiornamento() {
		return intervalliAggiornamento;
	}

	public void setIntervalliAggiornamento(Map<ValutaFiat, Map<LocalDateTime, Float>> intervalloAggiornamento) {
		this.intervalliAggiornamento = intervalloAggiornamento;
	}

	public Float getValore(ValutaFiat valuta, LocalDateTime date) {
		Map<LocalDateTime, Float> mapA = this.intervalliAggiornamento.get(valuta);
		Float result = mapA.get(date);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof Criptovaluta) 
			return this.sigla.equals(((Criptovaluta) obj).getSigla());
		return false;
	}
}
