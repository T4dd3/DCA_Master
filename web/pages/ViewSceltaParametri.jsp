<%@ page import="dcamaster.model.*" %>

	<head>
		<script type="text/javascript">
			function calcolaPrevisione()
			{
				if(document.getElementById('intervalloAttuale') !== null && document.getElementById('intervalloAttuale') !== '' && document.getElementById('intervalloAttuale') !== '0') {
					var intervallo = document.getElementById('intervalloAttuale');
				}
				if(document.getElementById('budgetAttuale') !== null && document.getElementById('budgetAttuale') !== ''){
					var budget = document.getElementById('budgetAttuale').value;
				}
				if(document.getElementById('intervallo') !== null && document.getElementById('intervallo') !== '' && document.getElementById('intervallo') !== '0') {
					var intervallo = document.getElementById('intervallo');
				}
				if(document.getElementById('budget') !== null && document.getElementById('budget') !== ''){
					var budget = document.getElementById('budget').value;
				}
				
				if (intervallo !== null && intervallo !== '' && budget !== null && budget !== '' && intervallo !== '0')
				{
					var previsione = 365 / intervallo * budget;
					document.getElementById('previsione').value = (Math.round(previsione * 100) / 100).toFixed(2);
				}
			}
		</script>
				
	<% Utente utente = (Utente) session.getAttribute("utente");
		if (utente == null)
			response.sendRedirect("ViewAutenticazione.jsp");%>
		
	</head>	
	
	<h1> Scelta parametri:</h1>
	<div class="main">
		<form id="parametri" method="post" action="../request" onchange="calcolaPrevisione(); return false;"><table>
			<tr><td>Intervallo attuale: </td><td id="intervalloAttuale"><%=(utente.getDca().getIntervalloInvestimento() != null) ? utente.getDca().getIntervalloInvestimento() : "Non impostato" %></td></tr>
			<tr><td>Budget attuale: </td><td id="budgetAttuale"><%=(utente.getDca().getBudget() != null) ? utente.getDca().getBudget() : "Non impostato" %></td></tr>
			<tr><td colspan="2"></td></tr>
			<tr><td>Intervallo di Investimento (in giorni): </td><td><input type="number" min="0" id="intervallo" name="intervallo" size="20" autocomplete="off"></td></tr>
			<tr><td>Budget:  </td><td><input type="number" step="0.01" min="0" id="budget" name="budget" size="20" autocomplete="off"></td></tr>
			<tr><td colspan="2"><input type="submit" style="width:100%" name="sceltaParametri" value="Cambia i parametri"/></td></tr>
		</table></form>
		<table>
		<tr><td>Previsione spesa (12 mesi): </td><td><input type="text" style="width:100%" id="previsione" disabled="disabled"/></tr>
		</table>
	</div>