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
		<script type="text/javascript" src="../scripts/chart.js"></script>
		<script type="text/javascript">
			function visualizzaRiepiloghi(riepiloghi)
			{
				// Recupero tabella in cui stampare riepiloghi e settaggio header
				var listaRiepiloghi = myGetElementById("riepiloghiList");
				listaRiepiloghi.innerHTML = "<tr><th>Data</th><th>Criptovaluta</th><th>FiatSpesa</th><th>Quantitativo</th><th>Valore</th></tr>";
				
				for (var riepilogo of riepiloghi)
				{
					var data = riepilogo.data.date.day + "-" + riepilogo.data.date.month + "-" + riepilogo.data.date.year + " " + riepilogo.data.time.hour + ":" + riepilogo.data.time.minute;
					var criptovaluta = riepilogo.criptovaluta.sigla;
					var fiatSpesa = riepilogo.fiatSpesa;
					var quantitativoAcquistato = riepilogo.quantitativoAcquistato;
					var valoreAllAcquisto = riepilogo.valore;
					
					listaRiepiloghi.innerHTML += "<tr><td>" + data + "</td><td>" + criptovaluta + "</td><td>" + fiatSpesa + "</td><td>" + quantitativoAcquistato + criptovaluta + "</td><td>" + valoreAllAcquisto + "</td></tr>";
				}
			}
			
			function drawGraph(valoriGrafico)
			{
				// X-Axis => Tutte le date normalizzate
				const labels = Object.keys(valoriGrafico).map(x => x.replace("T", " ").replace(/:[^d]{2}$/g, ""));
				// Y-Axis data => Valore portafoglio
				const data = {
				  labels: labels,
				  datasets: [{
				    label: 'Valore Portafoglio',
				    backgroundColor: 'rgb(255, 99, 132)',
				    borderColor: 'rgb(255, 99, 132)',
				    data: Object.values(valoriGrafico),
				  }]
				};
				// Config richieste da chart.js
				const config = {
					  type: 'line',
					  data,
					  options: {}
					};
				
				// Update the div and the canvas
				var divContainer = myGetElementById("chartDiv");
				divContainer.removeChild(myGetElementById("myChart"));
				var canvas = document.createElement("canvas");
				canvas.id = "myChart";
				divContainer.appendChild(canvas);
				
				// Render the chart
				var myChart = new Chart(
			    	canvas,
			    	config
			  	);
			}
		
			function callbackVisualizzazione(json) 
			{
				visualizzaRiepiloghi(json.riepiloghi);
				drawGraph(json.valoriGrafico);
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
	<%@ include file="../fragments/header.jsp" %>
		<center>
		<h1>Visualizzazione Andamento:</h1>
		<jsp:include page="./ViewFiltroVisualizzazione.jsp"></jsp:include>
		<br>
		<div class="main" id="chartDiv">
		  <canvas id="myChart"></canvas>
		</div>
		<br>
		<div class="main">
			<table id="riepiloghiList" class="formdata">
				
			</table>
		</div>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
	<%} %>
</html>