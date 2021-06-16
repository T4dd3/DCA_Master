<%@ page import="dcamaster.model.*" %>

<head>
	<script type="text/javascript">
		function calcolaPrevisione()
		{
			var intervallo = document.getElementById('intervallo').value;
			var budget = document.getElementById('budget').value;
			
			if (intervallo !== null && intervallo !== '' && budget !== null && budget !== '' && intervallo !== '0')
			{
				var previsione = 365 / intervallo * budget;
				document.getElementById('previsione').value = (Math.round(previsione * 100) / 100).toFixed(2);
			}
		}
	</script>
		
	<% Utente ut = (Utente)session.getAttribute("utente");
		if (ut == null)
			response.sendRedirect("ViewAutenticazione.jsp");%>
		
	</head>
	<center>
	<h1>Scelta parametri:</h1>
	<div class="main">
		<form id="filtri" method="post" action="../request" onchange="scegliFiltro(this); return false;"><table>
			<tr><td>Criptovaluta: </td><td><select type="number" min="0" id="intervallo" name="intervallo" size="20" autocomplete="off"></td></tr>
			<tr><td>Intervallo di Date:  </td><td><input type="number" step="0.01" min="0" id="budget" name="budget" size="20" autocomplete="off"></td></tr>
			<tr><td>Spesa:  </td><td><input type="number" step="0.01" min="0" id="budget" name="budget" size="20" autocomplete="off"></td></tr>
			
			<tr><td colspan="2"><input type="submit" style="width:100%" name="sceltaParametri" value="Cambia i parametri"/></td></tr>
		</table></form>
		<table>
		<tr><td>Previsione spesa (12 mesi): </td><td><input type="text" style="width:100%" id="previsione" disabled="disabled"/></tr>
		</table>
	</div>
	</center>
	