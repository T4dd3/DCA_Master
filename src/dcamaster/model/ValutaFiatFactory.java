package dcamaster.model;

import java.util.HashMap;
import java.util.Map;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.PersistenceException;
import dcamaster.db.ValutaFiatRepository;

public abstract class ValutaFiatFactory {
	// Contiene tutti gli oggetti Criptovaluta identificati dalla sigla
		private static Map<String, ValutaFiat> valuteFiat = new HashMap<>();
		private static ValutaFiatRepository fiatRepo = new ValutaFiatRepository(ControllerPersistenza.getInstance());
		
		public static ValutaFiat GetValutaFiat(String sigla)
		{
			ValutaFiat valutaFiat = valuteFiat.get(sigla);
			
			if (valutaFiat == null)
			{
				try {
					valutaFiat = fiatRepo.readBySigla(sigla);
					valuteFiat.put(sigla, valutaFiat);
				} catch (PersistenceException e) {
					e.printStackTrace();
				}
			}
			
			return valutaFiat;
		}
}
