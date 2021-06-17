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
		/*if (intervalliLoaded)
			return super.getIntervalliAggiornamento();*/
		
			CriptovalutaRepository repo = new CriptovalutaRepository(ControllerPersistenza.getInstance());
			try {
				setIntervalliAggiornamento(repo.getIntervalliAggiornamento(this));
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
			//intervalliLoaded(true);
			return super.getIntervalliAggiornamento();
	}
}
