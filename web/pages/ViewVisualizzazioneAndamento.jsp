<!-- Per non far scaturire errori in console relativi a codifica caratteri -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- pagina per la gestione di errori -->
<%@ page errorPage="../errors/failure.jsp"%>

<!-- accesso alla sessione -->
<%@ page session="true"%>

<!-- import di classi Java -->
<%@ page import="java.util.*"%>
<%@ page import="dcamaster.model.*" %>

<html>
	<head>
		<meta name="Author" content="Giovanni Taddei">
		<title>Login</title>
		<meta charset="UTF-8">
		<meta http-equiv="Pragma" content="no-cache"/>
		<meta http-equiv="Expires" content="-1"/>
		<link type="text/css" href="../styles/myCss.css" rel="stylesheet"></link>
		<script type="text/javascript" src="../scripts/utils.js"></script>
		<script type="text/javascript" src="../scripts/myUtils.js"></script>
		<script type="text/javascript">
			function callbackVisualizzazione(json) 
			{
				// Recupero tabella in cui stampare riepiloghi e settaggio header
				var listaRiepiloghi = myGetElementById("riepiloghiList");
				listaRiepiloghi.innerHTML = "<tr><th>H1</th><th>H2</th><th>H3</th></tr>";
				console.log(json);
				
				for (var riepilogo of json)
					listaRiepiloghi.innerHTML += "<tr><td>" +  + "</td><td>" +  + "</td><td>" +  + "</td></tr>";
			}
		
			// Chiamate remote al server, con e senza filtri
			function drawAndList(filtri)
			{
				if (!filtri)
					inviaDati(callbackVisualizzazione, "visualizzazioneAndamento=1&filtri=0");
				else
					inviaDati(callbackVisualizzazione, "visualizzazioneAndamento=1&filtri=1&"+filtri);
			}
			
			// Caricamento dati onload
			window.onload = function() { drawAndList() };
		</script>
		
	<% Utente utente = (Utente)session.getAttribute("utente");
		if (utente == null) {
			response.sendRedirect("ViewAutenticazione.jsp");
		} else {%>
		
	</head>
	<body>	
		<center>
		<h1>Visualizzazione Andamento:</h1>
		<jsp:include page="./ViewFiltroVisualizzazione.jsp"></jsp:include>
		
		<div class="main">
			<table id="riepiloghiList">
				
			</table>
		</div>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
	<%} %>
</html>