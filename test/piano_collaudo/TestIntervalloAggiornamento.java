package piano_collaudo;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dcamaster.model.IntervalloAggiornamento;

class TestIntervalloAggiornamento {

	private IntervalloAggiornamento intervalloAggiornamento;
	@BeforeEach
	void intervalloAggiornamentoSetup() {
		intervalloAggiornamento = new IntervalloAggiornamento(LocalDateTime.of(2020, 05, 02, 17, 8, 12), 1.21f, "EUR", "ADA");
	}
	@Test
	void testGetterIntervalloAggiornamento() {
		assertEquals(intervalloAggiornamento.getDataOra(), LocalDateTime.of(2020, 05, 02, 17, 8, 12));
		assertEquals(intervalloAggiornamento.getValoreConversione(), 1.21f);
		assertEquals(intervalloAggiornamento.getSiglaValutaFiat(), "EUR");
		assertEquals(intervalloAggiornamento.getSiglaCriptovaluta(), "ADA");
	}
	
}
