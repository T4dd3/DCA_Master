package dcamaster.db;

import java.time.LocalDateTime;
import java.util.Map;

import dcamaster.model.Criptovaluta;
import dcamaster.model.ValutaFiat;

public class CriptovalutaProxy extends Criptovaluta{

	public CriptovalutaProxy() {
		super();
	}
	
	@Override
	public Map<LocalDateTime, Map<ValutaFiat, Float>> getIntervalliAggiornamento() {
		if(isLoaded) {
			return super.getIntervalliAggiornamento();
		} else {
			CriptovalutaRepository repo = new CriptovalutaRepository(ControllerPersistenza.getInstance());
			setIntervalliAggiornamento(repo.getIntervalliAggiornamento(this));
			return getIntervalliAggiornamento();
		}
	}
}
