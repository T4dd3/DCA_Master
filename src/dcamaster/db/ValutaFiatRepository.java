package dcamaster.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dcamaster.model.Criptovaluta;
import dcamaster.model.CriptovalutaFactory;
import dcamaster.model.ValutaFiat;

public class ValutaFiatRepository {

	private ControllerPersistenza controller;
	
	//TABLE VALUTEFIAT-------------------------------------------------------------------------------------
	
	private static final String TABLE_VALUTEFIAT = "ValuteFiat";
	
	private static final String SIGLAFIAT = "siglaFiat";
	private static final String SIGLACRIPTO = "siglaCriptovaluta";
	private static final String SIGLA = "sigla";
	private static final String NOME = "nome";
	
	//===STATEMENT SQL=====================================================================================
	
	private static final String read_by_sigla = "SELECT * FROM " + TABLE_VALUTEFIAT + " WHERE " 
			+ SIGLA + " = ? ";
	
	private static final String get_criptovalute = "SELECT DISTINCT " + SIGLACRIPTO
			+ " FROM IntervalloAggiornamento "
			+ " WHERE " + SIGLAFIAT + " = ?";
	
	//====================================================================================================
	
	public ValutaFiatRepository(ControllerPersistenza controller) {
		this.controller = controller;
	}
	
	public ValutaFiat readBySigla(String sigla) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;
		ValutaFiat result = null;
		if (sigla == null || sigla.isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(read_by_sigla);
			statement.setString(1, sigla);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				result = new ValutaFiat();
				result.setSigla(sigla);
				result.setNome(rs.getString(NOME));
			}
			rs.close();
		} catch (Exception e) {
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
	
	public List<Criptovaluta> getCriptovaluteAssociate(String sigla){
		List<Criptovaluta> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		if (sigla == null || sigla.isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		try {
			connection = controller.getConnection();
			statement = connection.prepareStatement(get_criptovalute);
			statement.setString(1, sigla);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Criptovaluta entry = CriptovalutaFactory.GetCriptovaluta(rs.getString(SIGLACRIPTO));
				result.add(entry);
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
				//doSomething
			}
		}
		return result;
	}
}
