<!-- Per non far scaturire errori in console relativi a codifica caratteri -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- pagina per la gestione di errori -->
<%@ page errorPage="../errors/failure.jsp"%>

<!-- accesso alla sessione -->
<%@ page session="true"%>

<!-- import di classi Java -->
<%@ page import="java.util.*"%>

<% /* Eseguo la gesione del login all'interno della jsp (avrei potuto usare una servlet),
	* ma essendo la business logic abbastanza semplice e compatta o preferito tenere tutto insieme */ %>
<html>
	<head>
		<meta name="Author" content="Giovanni Taddei">
		<title>SignUp</title>
		<meta charset="UTF-8">
		<meta http-equiv="Pragma" content="no-cache"/>
		<meta http-equiv="Expires" content="-1"/>
		<link type="text/css" href="../styles/myCss.css" rel="stylesheet"></link>
		<script type="text/javascript" src="../scripts/utils.js"></script>
		<script type="text/javascript" src="../scripts/myUtils.js"></script>
	
	
	<%!	/* Metodo di ausilio per verificare che tutti i parametri specificati della richiesta siano non null e non vuoti */
		public boolean allParametersInitialized(HttpServletRequest request, List<String> paramsName) {
			for (String par : paramsName) {
				if (request.getParameter(par) == null || request.getParameter(par).isBlank())
					return false;
			}
			return true;
		}
	%>
	<% 	
		String stato = "";
		/* if (request.getParameter("signup") != null)
			stato = "Compila tutti i campi per procedere!";*/
	%>

		
	</head>
	<body>	
		<center>
		<h1>Sign-Up:</h1>
		<div class="main">
			<form id="dati" action="../registrazione" method="post"><table>
				<tr><td>Username: </td><td><input type="text" id="username" name="username" size="20" autocomplete="off"></td></tr>
				<tr><td>Email: </td><td><input type="email" id="email" name="email" size="20" autocomplete="off"></td></tr>
				<tr><td>Api Key: </td><td><input type="password" id="apiKey" name="apiKey" size="20" autocomplete="off"></td></tr>
				<tr><td>Api Secret: </td><td><input type="password" id="apiSecret" name="apiSecret" size="20" autocomplete="off"></td></tr>
				<tr><td>Valuta Fiat: </td><td>
					<select id="siglaFiat" name="siglaFiat" type="text">
      					<option value="EUR">EUR</option>
				      	<option value="USD">USD</option>
				      	<option value="CHF">CHF</option>
    				</select>
    			</td></tr>
    			<tr><td>Tipo Deposito: </td><td>
					<select id="tipoDeposito" name="tipoDeposito" type="text">
      					<option value="SEPA_BANK_ACCOUNT">Sepa Bank Account</option>
        				<option value="FIAT_ACCOUNT">Fiat Account</option>
        				<option value="BANK_WIRE">Bank Wire</option>
        				<option value="CREDIT_CARD">Credit Card</option>
    				</select>
    			</td></tr>
				<tr><td>Password:  </td><td><input type="password" id="password" name="password" size="20" autocomplete="off"></td></tr>
				<tr><td colspan="2"><input type="submit" style="width:100%" name="richiediCodice" value="Richiedi Codice di Verifica"/></td></tr>
			</table></form>
			<form id="codice" action="../codiceVerifica" method="post"><table>
				<tr><td>Codice: </td><td><input type="text" id="codice" name="codice" size="20" autocomplete="off" disabled></td></tr>
				<tr><td colspan="2"><input type="submit" style="width:100%" name="signup" value="Sign-Up" /></td></tr>
			</table></form>
			<span style="font-size: 0.8em">Hai già un account? <a href="./login.jsp">Effettua il login</a></span><br />
			<p style="color: red"><b><%=(request.getParameter("user") != null) ? stato : "" %></b></p>
		</div>
		</center>
		
		<%@ include file="../fragments/footer.jsp" %>
	</body>
</html>