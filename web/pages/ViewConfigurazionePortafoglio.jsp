	<head>
		<script type="text/javascript">
			function getCriptovaluteAcquistabili() 
			{
				var callback = function(jsonResponse) 
				{
					var table = myGetElementById("distribuzionePercentuale");
					// jsonResponse = [{criptovaluta: "BTC", percentuale: 0}, {criptovaluta: "ETH", perc: 100}]
					console.log(jsonResponse, table);
					for (var criptovaluta of jsonResponse) {
						
					}
				}
				inviaDati(callback, "criptovaluteAcquistabili=1")
			}
		</script>
	</head>
			
		<form onsubmit="getCriptovaluteAcquistabili(); return false;">
			<h3>Configurazione della percentuale assegnata alle criptovalute nella tua strategia DCA</h3>
			<input type="submit" name="criptovaluteAcquistabili" value="Visualizza"/>
		</form>
		<br>
		<form action="sceltaDistribuzionePercentuale">
			<table id="distribuzionePercentuale">
				<tr><th>Criptovaluta</th><th>Percentuale Assegnata</th></tr>
			</table>
		</form>