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
		
	<% String errorMessage = (String)request.getAttribute("errorMessage"); 
		if (session.getAttribute("utente") == null) { %>
		<meta http-equiv="Refresh" content="0; URL=ViewAutenticazione.jsp"/>
	<%} else {%>
		
	</head>
	<body>	
		<center>
			<%@ include file="./ViewConfigurazionePortafoglio.jsp" %>
			<br>
			<%@ include file="./ViewSceltaParametri.jsp" %>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
	<%} %>
</html>