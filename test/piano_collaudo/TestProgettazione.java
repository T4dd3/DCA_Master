package piano_collaudo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dcamaster.model.Criptovaluta;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.ValutaFiat;

class TestProgettazione {

	private StrategiaDCA dca;
	private Criptovaluta criptovaluta;
	private ValutaFiat fiat;
	
	@BeforeEach
	void setup() {
		Map<LocalDateTime, Float> valoriConversione = new HashMap<>();
		List<Criptovaluta> criptovalute = new ArrayList<>();
		Map<ValutaFiat, Map<LocalDateTime, Float>> intervalliAggiornamento = new HashMap<>();
		Map<Criptovaluta, Float> distribuzionePercentuale = new HashMap<>();
		dca = new StrategiaDCA();
		criptovaluta = new Criptovaluta();
		fiat = new ValutaFiat();
		fiat.setNome("euro");
		fiat.setSigla("EUR");
		criptovaluta.setNome("ethereum");
		criptovaluta.setSigla("ETH");
		criptovalute.add(criptovaluta);
		fiat.setCriptovaluteAssociate(criptovalute);
		valoriConversione.put(LocalDateTime.of(2021, 05, 28, 17, 47, 13), 2018.23f);
		intervalliAggiornamento.put(fiat, valoriConversione);
		criptovaluta.setIntervalliAggiornamento(intervalliAggiornamento);
		distribuzionePercentuale.put(criptovaluta, 100f);
		dca.setDistribuzionePercentuale(distribuzionePercentuale);
		dca.setBudget(100f);
		dca.setIntervalloInvestimento(30);
	}
	@Test
	void testDCA() {
		assertTrue(dca instanceof StrategiaDCA);
		assertEquals(dca.getBudget(), 100f);
		assertEquals(dca.getIntervalloInvestimento(), 30);
		assertEquals(dca.getDistribuzionePercentuale().get(criptovaluta), 100f);
	}
	@Test
	void testCriptovaluta() {
		assertTrue(criptovaluta instanceof Criptovaluta);
		assertEquals(criptovaluta.getNome(), "ethereum");
		assertEquals(criptovaluta.getSigla(), "ETH");
		assertEquals(criptovaluta.getIntervalliAggiornamento().get(fiat)
				.get(LocalDateTime.of(2021, 05, 28, 17, 47, 13)), 2018.23f);
		assertEquals(criptovaluta.getValore(fiat, LocalDateTime.of(2021, 05, 28, 17, 47, 13)), 2018.23f);
	}
	@Test
	void testFiat() {
		assertTrue(fiat instanceof ValutaFiat);
		assertEquals(fiat.getNome(), "euro");
		assertEquals(fiat.getSigla(), "EUR");
		assertEquals(fiat.getCriptovaluteAssociate().get(0).getNome(), "ethereum");
	}
}
