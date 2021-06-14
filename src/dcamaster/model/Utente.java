package dcamaster.model;

public class Utente {

	private String username;
	private StrategiaDCA dca;
	private ValutaFiat fiatScelta;
	private TipoDeposito tipoDeposito;

	public Utente() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public StrategiaDCA getDca() {
		return dca;
	}

	public void setDca(StrategiaDCA dca) {
		this.dca = dca;
	}

	public ValutaFiat getFiatScelta() {
		return fiatScelta;
	}

	public void setFiatScelta(ValutaFiat fiatScelta) {
		this.fiatScelta = fiatScelta;
	}

	public TipoDeposito getTipoDeposito() {
		return tipoDeposito;
	}

	public void setTipoDeposito(TipoDeposito tipoDeposito) {
		this.tipoDeposito = tipoDeposito;
	}
	
	
}
