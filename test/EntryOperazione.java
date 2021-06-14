import java.time.LocalDateTime;

public class EntryOperazione extends Entry{

	private String tipoOperazione;
	
	public EntryOperazione(LocalDateTime dataOra, String messaggio, String tipoOperazione) {
		super(dataOra, messaggio);
		this.tipoOperazione = tipoOperazione;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	
}
