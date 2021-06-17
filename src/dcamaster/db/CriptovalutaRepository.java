package dcamaster.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import dcamaster.model.Criptovaluta;
import dcamaster.model.ValutaFiat;
import dcamaster.model.ValutaFiatFactory;

public class CriptovalutaRepository {
	
	private ControllerPersistenza controller;
	
	//TABLE CRIPTOVALUTE-------------------------------------------------------------------------------
	
	private static final String TABLE = "Criptovalute";
	
	private static final String SIGLA = "sigla";
	private static final String NOME = "nome";
	
	//TABLE INTERVALLOAGGIORNAMENTO
	
	private static final String TABLE_INTERVALLI = "IntervalloAggiornamento";
	
	private static final String SIGLAFIAT = "siglaFiat";
	private static final String SIGLACRIPTOVALUTA = "siglaCriptovaluta";
	private static final String DATAORA = "dataOra";
	private static final String VALORECONVERSIONE = "valoreConversione";
	
	//=== QUERIES ========================================================================================
	
	private static final String read_by_sigla = "SELECT * FROM " + TABLE + " WHERE " + SIGLA + " = ? ";
	
	private static final String get_valute_intervalli = "SELECT " + SIGLAFIAT + " FROM " + TABLE_INTERVALLI 
			+ " WHERE " + SIGLACRIPTOVALUTA + " = ? "; 
	
	private static final String get_intervalli_for_valuta = "SELECT * FROM " + TABLE_INTERVALLI 
			+ " WHERE " + SIGLACRIPTOVALUTA + " = ? AND " + SIGLAFIAT + " = ?";
	
	//======================================================================================================
	
	public CriptovalutaRepository(ControllerPersistenza controller) {
		this.controller = controller;
	}
	
	public Criptovaluta read(String sigla) throws PersistenceException {
		Criptovaluta result = null;
		Connection connection = null;
		PreparedStatement statement = null;
		if(sigla == null || sigla.isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(read_by_sigla);
			statement.setString(1, sigla);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				result = new CriptovalutaProxy();
				result.setNome(rs.getString(NOME));
				result.setSigla(rs.getString(SIGLA));
				rs.close();
			}
		} catch (Exception e){
			System.out.println("read(): failed to read entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return result;
	}
	
	public Map<ValutaFiat, Map<LocalDateTime, Float>> getIntervalliAggiornamento(Criptovaluta criptovaluta) throws PersistenceException{
		Map<ValutaFiat, Map<LocalDateTime, Float>> result = new HashMap<>();
		Connection connection = null;
		PreparedStatement statement = null;
		if(criptovaluta == null || criptovaluta.getSigla().isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(get_valute_intervalli);
			statement.setString(1, criptovaluta.getSigla());
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				// Ottengo la chiave per la prima mappa
				ValutaFiat key = ValutaFiatFactory.GetValutaFiat(rs.getString(SIGLAFIAT));
				// Istanzio la seconda mappa associata alla chiave
				Map<LocalDateTime, Float> innerMap = new HashMap<>();
				// Ottengo i valori con cui popolare  la seconda mappa
				statement = connection.prepareStatement(get_intervalli_for_valuta);
				statement.setString(1, criptovaluta.getSigla());
				statement.setString(2, rs.getString(SIGLAFIAT));
				ResultSet rs2 = statement.executeQuery();
				while(rs2.next()) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					LocalDateTime data = LocalDateTime.parse(rs2.getString(DATAORA), formatter);
					Float valoreConversione = rs2.getFloat(VALORECONVERSIONE);
					innerMap.put(data, valoreConversione);
				}
				// Inserisco la seconda mappa nella prima
				result.put(key, innerMap);
			}
		} catch (Exception e){
			System.out.println("read(): failed to read entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return result;
	}
}
