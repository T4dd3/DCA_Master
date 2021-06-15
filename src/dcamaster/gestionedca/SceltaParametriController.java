package dcamaster.gestionedca;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dcamaster.db.ControllerPersistenza;
import dcamaster.db.UserRepository;
import dcamaster.model.Utente;

@SuppressWarnings("serial")
public class SceltaParametriController extends HttpServlet implements ISceltaParametri {

	HttpSession session;
	UserRepository repo;

	@Override
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);

		this.repo = new UserRepository(ControllerPersistenza.getInstance());
	}


	@Override
	public void sceltaBudget(float budget) {
		
		Utente user = (Utente) this.session.getAttribute("utente");
		
		user.getDca().setBudget(budget);
		
		repo.update(user.getDca());
	}

	@Override
	public void sceltaIntervalloInvestimento(int intervalloInvestimento) {
		
		Utente user = (Utente) this.session.getAttribute("utente");
		
		user.getDca().setIntervalloInvestimento(intervalloInvestimento);
		
		repo.update(user.getDca()); 
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
	}
}
