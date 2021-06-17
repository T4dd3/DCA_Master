package dcamaster.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import dcamaster.model.CriptovalutaFactory;
import dcamaster.model.RiepilogoOrdine;
import dcamaster.model.StrategiaDCA;

public class RiepiloghiOrdineRepository {
	
	private ControllerPersistenza controller;
	
	//TABLE RIEPILOGHIORDINE------------------------------------------------------------------------------
	
	private static final String TABLE_RIEPILOGHIORDINE = "RiepilogoOrdini";
	
	@SuppressWarnings("unused")
	private static final String IDRIEPILOGO = "idRiepilogo";
	private static final String DATAORA = "dataOra";
	private static final String FIATSPESA = "fiatSpesa";
	private static final String QUANTITATIVOACQUISTATO = "quantitativoAcquistato";
	private static final String SIGLA = "siglaCriptovaluta";
	private static final String USERNAME = "username";
	
	//==QUERY=============================================================================================
	
	private static final String get_riepiloghi = "SELECT * FROM " + TABLE_RIEPILOGHIORDINE 
			+ " WHERE " + USERNAME + " = ? ORDER BY dataOra";
	
	public RiepiloghiOrdineRepository(ControllerPersistenza controller) {
		this.controller = controller;
	}
	
	public List<RiepilogoOrdine> getRiepiloghiOrdine(StrategiaDCA strategiaDCA) throws PersistenceException {
		List<RiepilogoOrdine> result = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		if(strategiaDCA == null) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(get_riepiloghi);
			statement.setString(1, strategiaDCA.getUtente().getUsername());
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				RiepilogoOrdine entry = new RiepilogoOrdine();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime data = LocalDateTime.parse(rs.getString(DATAORA), formatter);
				entry.setData(data);
				entry.setFiatSpesa(rs.getFloat(FIATSPESA));
				entry.setQuantitativoAcquistato(rs.getFloat(QUANTITATIVOACQUISTATO));
				entry.setValore(entry.getFiatSpesa() / entry.getQuantitativoAcquistato());
				entry.setCriptovaluta(CriptovalutaFactory.GetCriptovaluta((rs.getString(SIGLA))));
				
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
				throw new PersistenceException(e.getMessage());
			}
		}
		return result;
	}
}
