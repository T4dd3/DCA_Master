

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestEntry {

	private EntryOperazione entryOperazione;
	private EntrySegnalazione entrySegnalazione;
	
	@BeforeEach
	void entrySetup() {
		entryOperazione = new EntryOperazione(LocalDateTime.of(2021, 05, 02, 17, 47, 13), "Password errata, Utente: user1", "Autenticazione");
		entrySegnalazione = new EntrySegnalazione(LocalDateTime.of(2021, 05, 02, 17, 47, 20), "Rilevato tentativo di accesso a Utente: user1");
	}
	@Test
	void testEntryOperazione() {
		assertTrue(entryOperazione instanceof Entry);
		assertTrue(entryOperazione instanceof EntryOperazione);
		assertEquals(entryOperazione.getDataOra(), LocalDateTime.of(2021, 05, 02, 17, 47, 13));
		assertEquals(entryOperazione.getMessaggio(), "Password errata, Utente: user1");
		assertEquals(entryOperazione.getTipoOperazione(), "Autenticazione");
	}
	@Test
	void testEntrySegnalazione() {
		assertTrue(entrySegnalazione instanceof Entry);
		assertTrue(entrySegnalazione instanceof EntrySegnalazione);
		assertEquals(entrySegnalazione.getDataOra(), LocalDateTime.of(2021, 05, 02, 17, 47, 20));
		assertEquals(entrySegnalazione.getMessaggio(), "Rilevato tentativo di accesso a Utente: user1");
	}

}
