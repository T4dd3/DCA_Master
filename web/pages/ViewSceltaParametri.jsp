<%@ page import="dcamaster.model.*" %>

	<head>
		<script type="text/javascript">
			function calcolaPrevisione()
			{
				// Provo a recuperare prima i nuovi valori se settati altrimenti uso quelli vecchi
				var intervallo = parseFloat(myGetElementById('intervallo').value) ||
								parseFloat(myGetElementById('intervalloAttuale').innerText);
				var budget = parseFloat(myGetElementById('budget').value) ||
							parseFloat(myGetElementById('budgetAttuale').innerText);
				
				// Calcolo e output previsione
				var previsione = parseFloat((365 * budget / intervallo).toFixed(2)) || "";
				myGetElementById('previsione').value = previsione;
			}
			document.onload = calcolaPrevisione;
		</script>
				
	<% Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null)
			response.sendRedirect("ViewAutenticazione.jsp");%>
		
	</head>	
	
	<h1> Scelta parametri:</h1>
	<div class="main">
		<table class="formdata">
			<tr><th colspan="2" style="text-align:center">Visualizzazione</th></tr>
			<tr><th>Parametro</th><th>Valore</th></tr>
			<tr><td>Intervallo attuale: </td><td id="intervalloAttuale"><%=(utente.getDca().getIntervalloInvestimento() != null) ? utente.getDca().getIntervalloInvestimento() : "Non impostato" %></td></tr>
			<tr><td>Budget attuale: </td><td id="budgetAttuale"><%=(utente.getDca().getBudget() != null) ? utente.getDca().getBudget() : "Non impostato" %></td></tr>
		</table>
		
		<form id="parametri" method="post" action="../request" onchange="calcolaPrevisione(); return false;">
			<table class="formdata">
				<tr><th colspan="2" style="text-align:center">Configurazione</th></tr>
				<tr><th>Parametro</th><th>Valore</th></tr>
				<tr><td>Intervallo di Investimento (in giorni): </td><td><input type="number" min="0" id="intervallo" name="intervallo" size="20" autocomplete="off"></td></tr>
				<tr><td>Budget:  </td><td><input type="number" step="0.01" min="0" id="budget" name="budget" size="20" autocomplete="off"></td></tr>
			</table>
			<table>
				<tr><td>Previsione spesa (12 mesi):</td><td><input type="text" style="width:100%" id="previsione" disabled="disabled"/></tr>
			</table>
			<input type="submit" style="width:100%" name="sceltaParametri" value="Cambia i parametri"/>
		</form>
	</div>