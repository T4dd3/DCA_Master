package dcamaster.gestionedca;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import dcamaster.model.Criptovaluta;

public class ConfigurazionePortafoglioController extends HttpServlet implements IConfigurazionePortafoglio 
{
	private static final long serialVersionUID = 1L;

	public ConfigurazionePortafoglioController() 
	{
		
	}
	
	@Override
	public void configuraPortafoglio(Map<Criptovaluta, Float> distribuzione) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Criptovaluta> getCriptovaluteAcquistabili() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
