package dcamaster.model;

import java.time.LocalDateTime;

public class Entry {

	private String messaggio;
	private LocalDateTime dataOra;
	
	public Entry() {
		
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public LocalDateTime getDataOra() {
		return dataOra;
	}

	public void setDataOra(LocalDateTime dataOra) {
		this.dataOra = dataOra;
	}
	
	
}
