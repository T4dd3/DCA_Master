package dcamaster.gestioneaccount;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class CodiceDiVerificaController extends HttpServlet implements ICodiceDiVerifica
{
	private IRegistrazione registrazioneController;
	private final String username;
	private final String password;
	private final String oggetto;
	private final String corpo;
	private String codiceSalvato;
	
	public CodiceDiVerificaController(IRegistrazione registrazioneController) 
	{
		this.username = "progetto.ing.software.gruppo1@gmail.com";
		this.password = "ProjIng1";
		this.registrazioneController = registrazioneController;
		this.oggetto = "Codice di Verifica per la registrazione a DCA Master";
		this.corpo = "Benvenuto su DCA Master! Inserisci il seguente codice per terminare la registrazione: ";
		this.codiceSalvato = "";
	}
	
	//gestione richieste POST (quando l'utente inserisce il codice)
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		
		// Recupero codice salvato precedentemente in sessione e codice appena inserito dall'utente
		String codiceInserito = request.getParameter("codice");
		this.codiceSalvato = (String) session.getAttribute("codice");
		
		//Verifico la correttezza del codice e chiamo registra utente
		this.verificaCodice(codiceInserito);
	}

	@Override
	public void setRegistrazioneController(IRegistrazione registrazioneController) 
	{
		this.registrazioneController = registrazioneController;
	}
	
	private String generaCodice() 
	{
		//genero un numero da 0 a 999999 (inclusi)
		Random rnd = new Random();
		int codice = rnd.nextInt(1000000);
		
		//converto il numero generato in una stringa da 6 cifre
		return String.format("%06d", codice);
	}

	@Override
	public String inviaCodice(String email) 
	{ 	
		String codice = generaCodice();
		
		// Parametri per connessione smtp
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        
        try {
        	// Aggiungo mittente
            message.setFrom(new InternetAddress(username));
            
            // Aggiungo destinatario
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            
            // Aggiungo oggetto e corpo
            message.setSubject(oggetto);
            message.setText(corpo + codice);
            
            // Connessione SMTP
            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            
            // Invio messaggio e chiusura connessione SMTP
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
		
		return codice;
	}

	@Override
	public void verificaCodice(String codice) 
	{
		if (this.codiceSalvato.equals(codice))
			this.registrazioneController.registraUtente();
		else 
			; //TODO: come fare per inviare la risposta?
	}

}
