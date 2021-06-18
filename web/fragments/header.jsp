<!-- accesso alla sessione -->
<%@ page session="true"%>

<!-- import di classi Java -->
<%@ page import="dcamaster.model.Utente" %>

<div class="navbar" style="margin-right: 2%">
	<% if (request.getParameter("logout") != null) {
		session.setAttribute("utente", null);
		response.sendRedirect("../pages/ViewAutenticazione.jsp");
		return;
	}%>
	<% Utente ut = (Utente)session.getAttribute("utente"); %>
	<div class="" style="float:left"><a href="./ViewVisualizzazioneAndamento.jsp">Visualizza Andamento</a><a href="./HomeConfigurazione.jsp">Configurazione</a></div>
	<div style="float:right; margin-right: 10px">
		<div style="display: inline-block">
			<img src="../images/user.png" width="40" height="40" style="vertical-align: middle;"/>
			<h4 id="header" style="display: inline">Hi <%=ut.getUsername() %>!</h4>
		</div>
		<form style="display: inline-block">
			<input type="submit" value="Log-Out" width="50%" name="logout">
		</form>
	</div>
	
</div>
