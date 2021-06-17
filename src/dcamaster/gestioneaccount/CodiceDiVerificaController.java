package dcamaster.gestioneaccount;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CodiceDiVerificaController implements ICodiceDiVerifica
{
	// Controller associato per la registrazione
	private IRegistrazione registrazioneController;
	public void setRegistrazioneController(IRegistrazione registrazioneController) {
		this.registrazioneController = registrazioneController;
	}
	
	private final String sourceMail;
	private final String sourceMailPassword;
	private final String oggetto;
	private final String corpo;
	private String codiceSalvato;
	
	public CodiceDiVerificaController() 
	{
		this.sourceMail = "dcamastergruppo1@gmail.com";
		this.sourceMailPassword = "DCAMaster12!";
		this.oggetto = "Codice di Verifica per la registrazione a DCA Master";
		this.corpo = "Benvenuto su DCA Master! Inserisci il seguente codice per terminare la registrazione: ";
		this.codiceSalvato = "";
	}
	
	private String generaCodice() 
	{
		// Genero un numero da 0 a 999999 (inclusi)
		Random rnd = new Random();
		int codice = rnd.nextInt(1000000);
		
		System.out.println(codice);
		
		// Converto il numero generato in una stringa da 6 cifre
		return String.format("%06d", codice);
	}

	@Override
	public void inviaCodice(String email) 
	{ 	
		// Creazione e salvataggio del codice inviato alla mail utente
		String codice = generaCodice();
		this.codiceSalvato = codice;
		
		// Parametri per connessione smtp
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", sourceMail);
        props.put("mail.smtp.password", sourceMailPassword);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        
        try {
        	// Aggiungo mittente
            message.setFrom(new InternetAddress(sourceMail));
            
            // Aggiungo destinatario
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            
            // Aggiungo oggetto e corpo
            message.setSubject(oggetto);
            message.setText(corpo + codice);
            
            // Connessione SMTP
            Transport transport = session.getTransport("smtp");
            transport.connect(host, sourceMail, sourceMailPassword);
            
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
	}

	@Override
	public String verificaCodice(String codice) 
	{
		String ritorno;
		
		if (this.codiceSalvato.equals(codice)) 
		{
			ritorno = this.registrazioneController.registraUtente();
		}
		else
			ritorno = "ERRORE: Codice inserito non valido!";
		
		return ritorno;
	}

}
