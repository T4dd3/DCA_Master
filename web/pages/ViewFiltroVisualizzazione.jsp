<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Date"%>
<%@ page import="dcamaster.model.*" %>

<head>
	<script type="text/javascript">
		function scegliFiltro(form)
		{
			var filtri = {
					criptovaluta: form.elements.criptovalute.value,
					startDate: form.elements.start.value,
					endDate: form.elements.end.value
					spesa: form.elements.end.value
			};
			
			drawAndList(JSON.stringify(filtri));
		}
	</script>
		
	<% Utente ut = (Utente)session.getAttribute("utente");
		if (ut == null)
			response.sendRedirect("ViewAutenticazione.jsp"); %>
		
	</head>
	<center>
	<h1>Scelta parametri:</h1>
	<div class="main">
		<form id="filtri" onsubmit="scegliFiltro(this); return false;"><table>
		
			<tr><td>Criptovaluta: </td>
				<td colspan="2"><select name="criptovalute" size="20" autocomplete="off">
					<% for (Criptovaluta cripto : ut.getFiatScelta().getCriptovaluteAssociate()) {
							%><option name="<%=cripto.getSigla() %>"><%=cripto.getSigla() %></option>
				<% }%></select></td>
			</tr>
			
			<tr><td>Intervallo di Date: </td>
				<td><input type="date" id="start" name="start" value="<%= LocalDate.now().minusDays(30)%>" min="1999-06-01" max="2100-01-01"></td>
				<td><input type="date" id="end" name="end" value="<%= LocalDate.now()%>" min="1999-06-01" max="2100-01-01"></td>
			</tr>
			
			<tr><td>Spesa: </td>
				<td><input type="number" step="0.01" min="0" id="spesa" name="spesa" size="20" autocomplete="off"></td>
			</tr>
			
			<tr><td colspan="3"><input type="submit" style="width:100%" name="scegliFiltri" value="Applica Filtri"/></td></tr>
		
		</table></form>
	</div>
	</center>
	