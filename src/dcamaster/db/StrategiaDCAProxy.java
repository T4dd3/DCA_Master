package dcamaster.db;

import java.util.List;
import java.util.Map;

import dcamaster.model.Criptovaluta;
import dcamaster.model.RiepilogoOrdine;
import dcamaster.model.StrategiaDCA;

public class StrategiaDCAProxy extends StrategiaDCA {

	public StrategiaDCAProxy() {
		super();
	}
	
	@Override
	public Map<Criptovaluta, Float> getDistribuzionePercentuale() {
		if(distribuzioneLoaded) {
			return super.getDistribuzionePercentuale();
		} else {
			UserRepository repo = new UserRepository(ControllerPersistenza.getInstance());
			try {
				setDistribuzionePercentuale(repo.getDistribuzionePercentuale(this));
			} catch (PersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			distribuzioneLoaded(true);
			return getDistribuzionePercentuale();
		}
	}
	
	@Override
	public List<RiepilogoOrdine> getReipiloghiOrdine() {
		if(riepiloghiLoaded) {
			return super.getReipiloghiOrdine();
		} else {
			RiepiloghiOrdineRepository repo = new RiepiloghiOrdineRepository(ControllerPersistenza.getInstance());
			try {
				setReipiloghiOrdine(repo.getRiepiloghiOrdine(this));
			} catch (PersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			riepiloghiLoaded(true);
			return getReipiloghiOrdine();
		}
	}
}
