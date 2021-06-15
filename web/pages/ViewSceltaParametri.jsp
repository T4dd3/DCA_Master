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
		
	<% String errorMessage = (String)request.getAttribute("errorMessage"); %>
	
	<%!
			public float calcolaPrevisione(String intervallo, String budget)
			{
				if (intervallo != null && budget != null)
					return Float.parseFloat(budget) * Integer.parseInt(intervallo);
				
				return 0;
			}
	
	%>
		
	</head>
	<body>	
		<center>
		<h1>Scelta parametri:</h1>
		<div class="main">
			<form id="parametri" method="post" action="../request"><table>
				<tr><td>Intervallo di Investimento: </td><td><input type="number" id="intervallo" name="intervallo" size="20" autocomplete="off"></td></tr>
				<tr><td>Budget:  </td><td><input type="number" id="budget" name="budget" size="20" autocomplete="off"></td></tr>
				
				<tr><td colspan="2"><input type="submit" style="width:100%" name="sceltaParametri" value="Cambia i parametri"/></td></tr>
			</table></form>
			<p style="color: red"><b><%=(errorMessage != null) ? errorMessage : "" %></b></p>
		</div>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
</html>