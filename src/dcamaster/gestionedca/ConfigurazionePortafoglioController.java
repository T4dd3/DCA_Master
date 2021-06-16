package dcamaster.gestionedca;

import java.util.HashMap;
import java.util.Map;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.CriptovalutaRepository;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;
import dcamaster.model.Criptovaluta;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.Utente;
import dcamaster.model.ValutaFiat;

public class ConfigurazionePortafoglioController implements IConfigurazionePortafoglio 
{
	// Utente associato su cui effettuare configurazioni
	Utente utente;
	// Repositories
	CriptovalutaRepository criptoRepo;
	UserRepository userRepo;
	
	public ConfigurazionePortafoglioController(Utente utente) 
	{
		this.utente = utente;
		
		this.criptoRepo = new CriptovalutaRepository(ControllerPersistenza.getInstance());
		this.userRepo = new UserRepository(ControllerPersistenza.getInstance());
	}
	
	@Override
	public void configuraPortafoglio(Map<Criptovaluta, Float> distribuzione) 
	{
		this.utente.getDca().setDistribuzionePercentuale(distribuzione);
		
		try {
			userRepo.updateDistribuzione(utente.getDca());
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<Criptovaluta, Float> getCriptovaluteAcquistabili() 
	{
		Map<Criptovaluta, Float> distribuzioneRestituita;
		
		// Recupero tutti i valori e controllo che siano valorizzati
		if (this.utente == null) return null;
		
		StrategiaDCA strategiaDCA = this.utente.getDca();
		if (strategiaDCA == null) return null;
		
		ValutaFiat valutaFiatUtente = this.utente.getFiatScelta();
		if (valutaFiatUtente == null) return null;
		
		Map<Criptovaluta, Float> distribuzionePercentuale = strategiaDCA.getDistribuzionePercentuale();
		
		// Inizializzo la distribuzioneRestituita con quella già esistente o altrimenti vuota
		if (distribuzionePercentuale == null) 
			distribuzioneRestituita = new HashMap<>();
		else 
			distribuzioneRestituita = new HashMap<>(distribuzionePercentuale);
		
		// Per ogni Criptovaluta non nella distribuzionePercentuale dell'Utente la restituisco con percentuale a 0
		for (Criptovaluta criptovaluta : valutaFiatUtente.getCriptovaluteAssociate())
			if (distribuzioneRestituita.get(criptovaluta) == null)
				distribuzioneRestituita.put(criptovaluta, 0.00f);
		
		return distribuzioneRestituita;
	}

}
