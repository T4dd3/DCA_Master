<!-- Per non far scaturire errori in console relativi a codifica caratteri -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- pagina per la gestione di errori -->
<%@ page errorPage="../errors/failure.jsp"%>

<!-- accesso alla sessione -->
<%@ page session="true"%>

<!-- import di classi Java -->
<%@ page import="java.util.*"%>

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
			
			function getCriptovaluteAcquistabili() 
			{
				var callback = function(jsonResponse) 
				{
					var table = myGetElementById("distribuzionePercentuale");
					// jsonResponse = [{criptovaluta: "BTC", percentuale: 0}, {criptovaluta: "ETH", perc: 100}]
					console.log(jsonResponse, table);
					for (var criptovaluta of jsonResponse) {
						
					}
				}
				inviaDati("configurazionePortafoglio", callback, "getCriptovaluteAcquistabili=1")
			}
		</script>
		
	<% String errorMessage = (String)request.getAttribute("errorMessage"); %>
		
	</head>
	<body>	
		<center>
		<form>
			<input type="submit" name="criptovaluteAcquistabili" onclick="getCriptovaluteAcquistabili(); return false;" />
		</form>
		<form action="sceltaDistribuzionePercentuale">
			<table id="distribuzionePercentuale">
				<tr><th>Criptovaluta</th><th>Percentuale Assegnata</th></tr>
			</table>
		</form>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
</html>