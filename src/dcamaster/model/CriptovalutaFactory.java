package dcamaster.model;

import java.util.HashMap;
import java.util.Map;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.CriptovalutaRepository;
import dcamaster.db.PersistenceException;

public abstract class CriptovalutaFactory 
{
	// Contiene tutti gli oggetti Criptovaluta identificati dalla sigla
	private static Map<String, Criptovaluta> criptovalute = new HashMap<>();
	private static CriptovalutaRepository criptovalutaRepo = new CriptovalutaRepository(ControllerPersistenza.getInstance());
	
	public static Criptovaluta GetCriptovaluta(String sigla)
	{
		Criptovaluta criptovaluta = criptovalute.get(sigla);
		
		if (criptovaluta == null)
		{
			try {
				criptovaluta = criptovalutaRepo.read(sigla);
				criptovalute.put(sigla, criptovaluta);
			} catch (PersistenceException e) {
				e.printStackTrace();
			}
		}
		
		return criptovaluta;
	}
}
