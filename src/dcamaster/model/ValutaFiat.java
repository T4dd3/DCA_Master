package dcamaster.model;

import java.util.List;

public class ValutaFiat {

	private String nome;
	private String sigla;
	private List<Criptovaluta> criptovaluteAssociate;
	
	protected boolean isLoaded;
	
	public ValutaFiat() {
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public List<Criptovaluta> getCriptovaluteAssociate() {
		return criptovaluteAssociate;
	}

	public void setCriptovaluteAssociate(List<Criptovaluta> criptovaluteAssociate) {
		this.criptovaluteAssociate = criptovaluteAssociate;
	}
	
	public void isLoaded(boolean bool) {
		this.isLoaded = bool;
	}
	
}
