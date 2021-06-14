<!-- accesso alla sessione -->
<%@ page session="true"%>

<!-- import di classi Java -->
<%@ page import="it.unibo.tw.web.beans.*" %>

<div style="display: inline-block; white-space: nowrap; float:right; margin-right: 2%">
	<% if (request.getParameter("logout") != null) {
		session.setAttribute("utente", null);
		response.sendRedirect("login.jsp");
		return;
	}%>
	<% Utente ut = (Utente)session.getAttribute("utente"); %>
	<img src="../images/user.png" width="40" height="40" style="vertical-align: middle;"/>
	<h4 id="header" style="display: inline">Hi <%=ut.getUsername() %>!</h4>
	<form style="display: inline-block">
		<input type="submit" value="Log-Out" width="50%" name="logout">
	</form>
</div>
