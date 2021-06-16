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
	
	private static final String get_date_intervalli = "SELECT " + DATAORA + " FROM " + TABLE_INTERVALLI 
			+ " WHERE " + SIGLACRIPTOVALUTA + " = ? "; 
	
	private static final String get_intervalli_for_data = "SELECT * FROM " + TABLE_INTERVALLI 
			+ " WHERE " + SIGLACRIPTOVALUTA + " = ? AND " + DATAORA + " = ?";
	
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
	
	public Map<LocalDateTime, Map<ValutaFiat, Float>> getIntervalliAggiornamento(Criptovaluta criptovaluta) throws PersistenceException{
		Map<LocalDateTime, Map<ValutaFiat, Float>> result = new HashMap<>();
		Connection connection = null;
		PreparedStatement statement = null;
		if(criptovaluta == null || criptovaluta.getSigla().isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			ValutaFiatRepository repo = new ValutaFiatRepository(controller);
			statement = connection.prepareStatement(get_date_intervalli);
			statement.setString(1, criptovaluta.getSigla());
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				// Ottengo la chiave per la prima mappa
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime data = LocalDateTime.parse(rs.getString(DATAORA), formatter);
				// Istanzio la seconda mappa associata alla chiave
				Map<ValutaFiat, Float> innerMap = new HashMap<>();
				// Ottengo i valori con cui popolerà la seconda mappa
				statement = connection.prepareStatement(get_intervalli_for_data);
				statement.setString(1, criptovaluta.getSigla());
				statement.setString(2, rs.getString(DATAORA));
				ResultSet rs2 = statement.executeQuery();
				while(rs2.next()) {
					ValutaFiat entry = repo.readBySigla(rs.getString(SIGLAFIAT));
					Float valoreConversione = rs.getFloat(VALORECONVERSIONE);
					innerMap.put(entry, valoreConversione);
				}
				// Inserisco la seconda mappa nella prima
				result.put(data, innerMap);
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
