package dcamaster.gestionedca;

import javax.servlet.http.HttpServlet;


import dcamaster.db.ControllerPersistenza;
import dcamaster.db.PersistenceException;
import dcamaster.db.UserRepository;
import dcamaster.model.Utente;

@SuppressWarnings("serial")
public class SceltaParametriController extends HttpServlet implements ISceltaParametri {

	//HttpSession session;
	
	private UserRepository repo;
	
	private ControllerPersistenza controllerPersistenza;
	
	private Utente utente;

	/*@Override
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);

		this.repo = new UserRepository(ControllerPersistenza.getInstance());
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		synchronized (this) 
		{
			this.session = request.getSession();
			
			if (request.getParameter("budget") != null && !(request.getParameter("budget").trim().equals(""))) 
			{
				this.sceltaBudget(Float.parseFloat(request.getParameter("budget")));
			}

			if (request.getParameter("intervallo") != null && !(request.getParameter("intervallo").trim().equals(""))) 
			{
				this.sceltaIntervalloInvestimento(Integer.parseInt(request.getParameter("intervallo")));
			}
		}
	}*/
	
	public SceltaParametriController(Utente utente) {
		this.utente = utente;
		this.controllerPersistenza = ControllerPersistenza.getInstance();
		this.repo = new UserRepository(controllerPersistenza);
	}


	@Override
	public void sceltaBudget(float budget) {
		
		this.utente.getDca().setBudget(budget);
		
		try {
			repo.updateParametri(utente.getDca());
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sceltaIntervalloInvestimento(int intervalloInvestimento) {
		
		//Utente user = (Utente) this.session.getAttribute("utente");
		
		this.utente.getDca().setIntervalloInvestimento(intervalloInvestimento);
		
		try {
			repo.updateParametri(this.utente.getDca());
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	
}
