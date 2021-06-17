<%@page import="java.util.stream.Collectors"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.util.Map"%>
<%@ page import="dcamaster.model.*" %>

<head>
	<script type="text/javascript">
		function scegliFiltro(form)
		{
			var filtri = "criptovaluta=" + form.elements.criptovalute.value + "&"
						+ "startDate=" + form.elements.start.value + "&"
						+ "endDate=" + form.elements.end.value + "&"
						+ "spesa=" + form.elements.spesa.value;
			
			drawAndList(filtri);
		}
	</script>
		
	<% Utente ut = (Utente)session.getAttribute("utente");
		if (ut == null) {
			response.sendRedirect("ViewAutenticazione.jsp");
		}%>
		
	</head>
	<center>
	<div class="main">
		<form id="filtri" onsubmit="scegliFiltro(this); return false;"><table class="formdata">
		
			<tr><td>Criptovaluta: </td>
				<td colspan="2"><select name="criptovalute">
					<% 	Map<Criptovaluta, Float> distrPerc = ut.getDca().getDistribuzionePercentuale();
						for (Criptovaluta cripto : distrPerc.keySet().stream().filter(cripto -> distrPerc.get(cripto) != 0).collect(Collectors.toList())) {
							%><option name="<%=cripto.getSigla() %>"><%=cripto.getSigla() %></option>
				<% }%></select></td>
			</tr>
			
			<tr><td>Intervallo di Date: </td>
				<td><input type="datetime-local" id="start" name="start" value="<%= LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"))%>" min="1999-06-01" max="2100-01-01"></td>
				<td><input type="datetime-local" id="end" name="end" value="<%= LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"))%>" min="1999-06-01" max="2100-01-01"></td>
			</tr>
			
			<tr><td>Spesa: </td>
				<td colspan="2"><input type="number" step="0.01" min="0" id="spesa" name="spesa" size="20" autocomplete="off"></td>
			</tr>
			
			<tr><td colspan="3"><input type="submit" style="width:100%" name="scegliFiltri" value="Applica Filtri"/></td></tr>
		
		</table></form>
	</div>
	</center>
	