package dcamaster.db;

import java.util.List;

import dcamaster.model.Criptovaluta;
import dcamaster.model.ValutaFiat;

public class ValutaFiatProxy extends ValutaFiat{

	public ValutaFiatProxy() {
		super();
	}
	
	@Override
	public List<Criptovaluta> getCriptovaluteAssociate(){
		if(isLoaded) {
			return super.getCriptovaluteAssociate();
		} else {
			ValutaFiatRepository repo = new ValutaFiatRepository(ControllerPersistenza.getInstance());
			setCriptovaluteAssociate(repo.getCriptovaluteAssociate(this.getSigla()));
			isLoaded(true);
			return getCriptovaluteAssociate();
		}
	}
}
