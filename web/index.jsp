<!-- Per non far scaturire errori in console relativi a codifica caratteri -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- pagina per la gestione di errori -->
<%@ page errorPage="../errors/failure.jsp"%>

<!-- Creo subito una sessione per l'utente appena si collega -->
<%@ page session="true"%>

<html>
   <head>
		<meta http-equiv="Refresh" content="4; URL=pages/login.jsp"/>
      	<title>WebApplication starting...</title>
		<link type="text/css" href="styles/myCss.css" rel="stylesheet"></link>
		<script type="text/javascript">
			/* Rendo responsive l'icona di caricamento */
			window.onload = function () {
				document.getElementsByTagName("p")[0].style.marginTop = window.innerHeight * 0.1;
			};
			window.onresize = window.onload;
		</script>
   </head>
   <body style="background-color: black;">
	<center>
    	<p style="color: white">
      		<b>Your WebApp is loading... <br /><br /></b>
			<img width="auto" height="50%" alt="wait" title="wait" src="images/wait.gif"/>
      	</p>
	</center>
	<%@ include file="fragments/footer.jsp" %>
   </body>
</html>

