function callbackJSON( theXhr, myCallback ) 
{
	if ( theXhr.readyState === 4 ) {
		if ( theXhr.status === 200 ) {
			console.log(theXhr.response);
			var json = JSON.parse(theXhr.response);
			
			myCallback(json);
		}
		else if ( theXhr.status == 404)
			alert("Ricevuto 404, controlla url: "+ theXhr.responseURL);
		else 
			alert("Impossibile effettuare l'operazione richiesta.\n");
	}
}

function inviaDati(destUrl, callback, data) {
	var xhr = myGetXmlHttpRequest();
	
	if ( xhr ) {
		/* Binding funzione di callback */
		xhr.onreadystatechange = function() { callbackJSON(xhr, callback); };

		try {
			/* Apertura connessione verso destinatario */
			xhr.open("post", "../" + destUrl, true);
		}
		catch(e) { alert(e); }
		
		/* Settaggio header adeguati per la corretta trasmissione e ricezione dei dati */
		xhr.setRequestHeader("connection", "close");
		xhr.setRequestHeader("content-type", "application/x-www-form-urlencoded");
		
		/* Invio della richiesta */
		console.log(JSON.stringify(data));
		xhr.send(JSON.stringify(data));
	}
	else 
		alert("Il tuo browser non supporta AJAX");
}