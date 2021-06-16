	<head>
		<script type="text/javascript">
			function getCriptovaluteAcquistabili() 
			{
				var callback = function(jsonResponse) 
				{
					var tableDistribuzione = myGetElementById("distribuzionePercentuale");
					tableDistribuzione.innerHTML = "<tr><th>Criptovaluta</th><th>Percentuale Assegnata</th></tr>";
					
					// Show configurazioneBTN and tableDistribuzione; hide visualizzaCriptoBTN
					myGetElementById("criptovaluteAcquistabili").style.display = "none";
					myGetElementById("configuraDistribuzione").style.display = "";
					tableDistribuzione.style.display = "";
					
					// jsonResponse = { BTC: 0, ETH: 74.2, ADA: 25.8 }
					for (var criptovaluta in jsonResponse) {
						tableDistribuzione.innerHTML += 
							"<tr><td>" + criptovaluta + "</td><td><input type='text' name='" +
							criptovaluta + "' value='" + jsonResponse[criptovaluta] + "' /></td></tr>"
					}
				}
				inviaDati(callback, "criptovaluteAcquistabili=1");
			}
			
			function sceltaDistribuzionePercentuale(form)
			{
				var distribuzionePercentuale = {};
				var callback = function(result) {
					alert(result.esito);
				}
				
				for (var element of form.elements) {
					if (element.type == "text") {
						distribuzionePercentuale[element.name] = parseFloat(element.value) || 0;
					}
				}
				
				inviaDati(callback, "distribuzionePercentuale="+JSON.stringify(distribuzionePercentuale));
			}
		</script>
	</head>
		<h1>Scelta distribuzione percentuale:</h1>
		<div class="main">
			<form onsubmit="getCriptovaluteAcquistabili(); return false;">
				<input type="submit" name="criptovaluteAcquistabili" id="criptovaluteAcquistabili" value="Visualizza distribuzione"/>
			</form>
			<form id="distribuzionePercentualeForm" onsubmit="sceltaDistribuzionePercentuale(this); return false;">
				<table class="formdata" id="distribuzionePercentuale" style="display: none;">
				</table>
				<input type="submit" name="configuraDistribuzione" id="configuraDistribuzione" style="display: none;" value="Configura distribuzione"/>
			</form>
		</div>