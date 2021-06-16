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
		
	<% Utente utente = (Utente)session.getAttribute("utente");
		if (utente == null)
			response.sendRedirect("ViewAutenticazione.jsp"); %>
		
	</head>
	<body>	
		<center>
		<h1>Visualizzazione Andamento:</h1>
		<jsp:include page="./ViewFiltroVisualizzazione"></jsp:include>
		
		<div class="main">
			
		</div>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
</html>