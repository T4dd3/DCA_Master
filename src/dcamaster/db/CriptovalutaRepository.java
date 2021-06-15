package dcamaster.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Map;

import dcamaster.model.Criptovaluta;
import dcamaster.model.ValutaFiat;

public class CriptovalutaRepository {
	
	private ControllerPersistenza controller;
	
	//TABLE CRIPTOVALUTE-------------------------------------------------------------------------------
	
	private static final String TABLE = "Criptovalute";
	
	private static final String SIGLA = "sigla";
	private static final String NOME = "nome";
	
	//=== QUERIES ========================================================================================
	
	private static final String create_table = "CREATE TABLE " + TABLE + "( "
			+ SIGLA + " TEXT NOT NULL, "
			+ NOME + " TEXT NOT NULL, "
			+ "PRIMARY KEY(" + SIGLA + "))";
	
	private static final String drop_table = "DROP TABLE " + TABLE + "";
	
	private static final String read_by_sigla = "SELECT * FROM " + TABLE + " WHERE " + SIGLA + " = ? ";
	
	private static final String get_intervalli = "";
	
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
	
	public Map<LocalDateTime, Map<ValutaFiat, Float>> getIntervalliAggiornamento(Criptovaluta criptovaluta){
		Map<LocalDateTime, Map<ValutaFiat, Float>> result = null;
		
		return result;
	}
}
