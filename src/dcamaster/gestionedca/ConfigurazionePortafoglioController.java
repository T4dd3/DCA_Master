package dcamaster.gestionedca;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.CriptovalutaRepository;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;
import dcamaster.model.Criptovaluta;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.Utente;
import dcamaster.model.ValutaFiat;


@SuppressWarnings("serial")
public class ConfigurazionePortafoglioController implements IConfigurazionePortafoglio 
{
	Gson gson;
	HttpSession session;
	CriptovalutaRepository criptoRepo;
	UserRepository userRepo;
	
	public ConfigurazionePortafoglioController() 
	{
		gson = new Gson();
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);

		this.criptoRepo = new CriptovalutaRepository(ControllerPersistenza.getInstance());
		this.userRepo = new UserRepository(ControllerPersistenza.getInstance());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		synchronized (this) 
		{
			this.session = req.getSession();
			
			// Differenzio il tipo di richiesta
			if (req.getParameter("getCriptovaluteAcquistabili") != null) 
			{	
				// Preparazione della mappa da restituire di tipo <String, Float> cioè <siglaCripto, percentuale>
				Map<Criptovaluta, Float> criptoAcquistabiliPiuDistribuzione = this.getCriptovaluteAcquistabili();
				Map<String, Float> criptoAcquistabiliPiuDistribuzioneStringa = criptoAcquistabiliPiuDistribuzione.keySet().stream().
						collect(Collectors.toMap(Criptovaluta::getSigla, cripto -> criptoAcquistabiliPiuDistribuzione.get(cripto)));
				
				// Preparazione del json da inviare all'utente
				String jsonMappa = gson.toJson(criptoAcquistabiliPiuDistribuzione);
				
				// Restituzione risposta all'utente
				resp.getWriter().write(jsonMappa);
			}
			else if (req.getParameter("configuraPortafoglio") != null)
			{
				// Recupero json della nuova distribuzionePercentuale
				String jsonMappa = (String)req.getParameter("distribuzionePercentuale");
				if (jsonMappa == null) return;
				
				// Conversione json come mappa (FORMATO RICHIESTO: {'BTC': 15.5, 'ETH': 85.5})
				Type type = new TypeToken<Map<String, Float>>(){}.getType();
				Map<String, Float> nuovaDistribuzioneSigle = gson.fromJson(jsonMappa, type);
				Set<String> keys = nuovaDistribuzioneSigle.keySet();
				Map<Criptovaluta, Float> distribuzione = new HashMap<>();
				for(String sigla: keys) {
					try {
						Criptovaluta entry = criptoRepo.read(sigla);
						distribuzione.put(entry, nuovaDistribuzioneSigle.get(sigla));
					} catch (PersistenceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.configuraPortafoglio(distribuzione);
				}
				
			}
		}
	}
	
	@Override
	public void configuraPortafoglio(Map<Criptovaluta, Float> distribuzione) 
	{
		Utente utente = (Utente)this.session.getAttribute("utente");
		utente.getDca().setDistribuzionePercentuale(distribuzione);
		try {
			userRepo.updateDistribuzione(utente.getDca());
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Map<Criptovaluta, Float> getCriptovaluteAcquistabili() 
	{
		Map<Criptovaluta, Float> distribuzioneRestituita;
		
		// Recupero tutti i valori e controlli che siano valorizzati
		Utente utente = (Utente)this.session.getAttribute("utente");
		if (utente == null) return null;
		
		StrategiaDCA strategiaDCA = utente.getDca();
		if (strategiaDCA == null) return null;
		
		ValutaFiat valutaFiatUtente = utente.getFiatScelta();
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
