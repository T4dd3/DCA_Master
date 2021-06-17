package dcamaster.model;
import java.time.LocalDateTime;
import java.util.Map;

public class Criptovaluta 
{
	private String sigla;
	private String nome;
	private Map<LocalDateTime, Map<ValutaFiat, Float>> intervalliAggiornamento;
	
	protected boolean intervalliLoaded;
	
	public Criptovaluta() {
		this.intervalliLoaded = false;
	}
	
	protected void intervalliLoaded(Boolean bool) {
		this.intervalliLoaded = bool;
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
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof Criptovaluta) 
			return this.sigla.equals(((Criptovaluta) obj).getSigla());
		return false;
	}
}
