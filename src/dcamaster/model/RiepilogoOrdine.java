package dcamaster.model;
import java.util.Date;

public class RiepilogoOrdine {
	private Date data;
	private float fiatSpesa;
	private float quantitativoAcquistato;
	private float valore;
	private Criptovaluta criptovaluta;
	
	public RiepilogoOrdine() {
		
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public float getFiatSpesa() {
		return fiatSpesa;
	}

	public void setFiatSpesa(float fiatSpesa) {
		this.fiatSpesa = fiatSpesa;
	}

	public float getQuantitativoAcquistato() {
		return quantitativoAcquistato;
	}

	public void setQuantitativoAcquistato(float quantitativoAcquistato) {
		this.quantitativoAcquistato = quantitativoAcquistato;
	}

	public float getValore() {
		return valore;
	}

	public void setValore(float valore) {
		this.valore = valore;
	}

	public Criptovaluta getCriptovaluta() {
		return criptovaluta;
	}

	public void setCriptovaluta(Criptovaluta criptovaluta) {
		this.criptovaluta = criptovaluta;
	}
	
}
