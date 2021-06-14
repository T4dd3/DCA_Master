package dcamaster.gestionedca;

import java.util.List;
import java.util.Map;

import dcamaster.model.Criptovaluta;

public interface IConfigurazionePortafoglio {

public void configuraPortafoglio(Map<Criptovaluta, Float> distribuzione);
	
	public List<Criptovaluta> getCriptovaluteAcquistabili();
}
