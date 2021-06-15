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
		
	<%!public boolean allParametersInitialized(HttpServletRequest request, List<String> paramsName) {
			for (String par : paramsName) {
				if (request.getParameter(par) == null || request.getParameter(par).isBlank())
					return false;
			}
			return true;
		}%>
		
	<% String errorMessage = (String)request.getAttribute("errorMessage"); %>
		
	</head>
	<body>	
		<center>
		<h1>Login:</h1>
		<div class="main">
			<form id="login" action="../autenticazione" method="post"><table>
				<tr><td>Username: </td><td><input type="text" id="username" name="username" size="20" autocomplete="off"></td></tr>
				<tr><td>Password:  </td><td><input type="password" id="password" name="password" size="20" autocomplete="off"></td></tr>
				
				<tr><td colspan="2"><input type="submit" style="width:100%" name="login" value="Log-in"/></td></tr>
			</table></form>
			<span style="font-size: 0.8em">Non ancora registrato? <a href="./ViewRegistrazione.jsp">Crea un account</a></span><br />
			<p style="color: red"><b><%=(errorMessage != null) ? errorMessage : "" %></b></p>
		</div>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
</html>