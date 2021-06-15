package dcamaster.gestionedca;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import com.google.gson.Gson;

import dcamaster.model.Criptovaluta;


@SuppressWarnings("serial")
public class ConfigurazionePortafoglioController extends HttpServlet implements IConfigurazionePortafoglio 
{
	Gson gson;
	
	public ConfigurazionePortafoglioController() 
	{
		gson = new Gson();
	}
	
	@Override
	public void configuraPortafoglio(Map<Criptovaluta, Float> distribuzione) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Criptovaluta, Float> getCriptovaluteAcquistabili() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
