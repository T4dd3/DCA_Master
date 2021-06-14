import java.time.LocalDateTime;

public class Entry {

	private LocalDateTime dataOra;
	private String messaggio;
	public Entry(LocalDateTime dataOra, String messaggio) {
		super();
		this.dataOra = dataOra;
		this.messaggio = messaggio;
	}
	public LocalDateTime getDataOra() {
		return dataOra;
	}
	public void setDataOra(LocalDateTime dataOra) {
		this.dataOra = dataOra;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	
}
