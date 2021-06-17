package dcamaster.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import dcamaster.model.Criptovaluta;
import dcamaster.model.CriptovalutaFactory;
import dcamaster.model.StrategiaDCA;
import dcamaster.model.TipoDeposito;
import dcamaster.model.Utente;
import dcamaster.model.ValutaFiat;

public class UserRepository {

	private ControllerPersistenza controller;
	
	//---- TABLE ----------------------------------------------------------------------------------
	
	private static final String TABLE_UTENTI = "Utenti";
	private static final String TABLE_VALUTEFIAT = "valuteFiat";
	
	private static final String USERNAME = "username";
	private static final String HASHPASSWORD = "hashPassword";
	private static final String SALTPASSWORD = "saltPassword";
	private static final String EMAIL = "email";
	private static final String INTERVALLOINVESTIMENTO = "intervalloInvestimento";
	private static final String BUDGET = "budget";
	private static final String ISADMIN = "isAdmin";
	private static final String APIKEY = "apiSecret";
	private static final String APISECRET = "apiKey";
	private static final String VALUTAFIATRIFERIMENTO = "valutaFiatRiferimento";
	private static final String TIPODEPOSITO = "tipoDeposito";
	
	private static final String TABLE_DISTRIBUZIONE = "DistribuzionePercentuale";
	
	private static final String NOME = "nome";
	private static final String SIGLA = "siglaCriptovaluta";
	private static final String PERCENTUALE = "percentualeAssegnata";
	//=== STATEMENT SQL =======================================================================================
	
	//create table
	private static final String create = "CREATE TABLE " + TABLE_UTENTI + " ("
			+ USERNAME + " TEXT NOT NULL, "
			+ HASHPASSWORD + " TEXT NOT NULL, "
			+ SALTPASSWORD + " TEXT NOT NULL, "
			+ EMAIL + " TEXT NOT NULL UNIQUE, "
			+ INTERVALLOINVESTIMENTO + " INTEGER, "
			+ BUDGET + " NUMERIC, "
			+ ISADMIN + " INTEGER NOT NULL CHECK(" + ISADMIN +" = 0 OR " + ISADMIN + " = 1), "
			+ APIKEY + " TEXT NOT NULL, "
			+ APISECRET + " TEXT NOT NULL, "
			+ VALUTAFIATRIFERIMENTO + " TEXT NOT NULL, "
			+ TIPODEPOSITO + " TEXT NOT NULL, "
			+ " PRIMARY KEY( " + USERNAME + "), "
			+ " FOREIGN KEY (" + VALUTAFIATRIFERIMENTO + ") REFERENCES valuteFiat(sigla)) ";
	
	//drop table
	private static final String drop = "DROP TABLE " + TABLE_UTENTI + " ";
	
	//insert into table
	private static final String insert = "INSERT INTO " + TABLE_UTENTI + " (" 
			+ USERNAME + ", "
			+ HASHPASSWORD + ", "
			+ SALTPASSWORD + ", "
			+ EMAIL + ", "
			+ ISADMIN + ", "
			+ APIKEY + ", "
			+ APISECRET + ", "
			+ VALUTAFIATRIFERIMENTO + ", "
			+ TIPODEPOSITO + ") "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	//select from table
	private static final String read_by_username = "SELECT U." + USERNAME 
			+ ", U." + INTERVALLOINVESTIMENTO 
			+ ", U." + BUDGET 
			+ ", U." + HASHPASSWORD
			+ ", U." + SALTPASSWORD
			+ ", U." + TIPODEPOSITO
			+ ", V.* "
			+" FROM " + TABLE_UTENTI + " AS U INNER JOIN " + TABLE_VALUTEFIAT + " AS V "
			+ "ON U." + VALUTAFIATRIFERIMENTO + " = V.sigla"
			+ " WHERE U." + USERNAME + " = ? ";
	
	//update table
	private static final String update_budget = "UPDATE " + TABLE_UTENTI 
			+ " SET " + BUDGET + " = ? "
			+ " WHERE " + USERNAME + " = ? ";
	
	private static final String update_intervallo = "UPDATE " + TABLE_UTENTI 
			+ " SET " + INTERVALLOINVESTIMENTO + " = ? "
			+ " WHERE " + USERNAME + " = ? ";
	
	private static final String update_distribuzione = "INSERT INTO " + TABLE_DISTRIBUZIONE + " ("
			+  SIGLA + ", "
			+ PERCENTUALE + ", "
			+ USERNAME + ") "
			+ "VALUES (?, ?, ?)" ;
	
	private static final String get_distribuzione = "SELECT * FROM " + TABLE_DISTRIBUZIONE
			+ " WHERE " + USERNAME + " = ? ";
	
	private static final String delete_distribuzione_utente = "DELETE FROM " + TABLE_DISTRIBUZIONE 
			+ " WHERE " + USERNAME + " = ?";
	
	private static final String get_valore_portafoglio = "SELECT sum(TotaleValoreCriptoInValutaFiat)"
			+ " FROM (SELECT sum(quantitivoAcquistato) * ( "
				+ " SELECT valoreConversione "
				+ " FROM IntervalloAggiornamento "
				+ " WHERE IntervalloAggiornamento.siglaCriptovaluta = R.siglaCriptovaluta AND "
				+ " IntervalloAggiornamento.siglaFiat = V.sigla "
				+ " ORDER BY abs(strftime('%s', IntervalloAggiornamento.dataOra) - strftime('%s', datetime())) ASC "
				+ " LIMIT 1 "
			+ " )) as 'TotaleValoreCriptoInValutaFiat' "
			+ " FROM RiepilogoOrdini as R "
			+ " INNER JOIN Utenti as U ON U.username = R.username "
			+ " INNER JOIN ValuteFiat as V ON V.sigla = U.valutaFiatRiferimento "
			+ " WHERE U.username = ? AND date(dataOra) <= ? "
			+ " GROUP BY R.siglaCriptovaluta) ";
	
	//===================================================================================================
	
	public UserRepository(ControllerPersistenza controller) {
		this.controller = controller;
	}
	
	public void dropTable() throws PersistenceException {
		Connection connection = controller.getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(drop);
		} catch (SQLException e) {
			// the table does not exist
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}
	
	public void createTable() throws PersistenceException {
		Connection connection = controller.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(create);
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}
	
	public void create(String username, String hashPassword, String saltPassword, String email, ValutaFiat valutaRiferimento,
			String apiKey, String apiSecret, TipoDeposito tipoDeposito) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = this.controller.getConnection();
			statement = connection.prepareStatement(insert);
			statement.setString(1, username);
			statement.setString(2, hashPassword);
			statement.setString(3, saltPassword);
			statement.setString(4, email);
			statement.setInt(5, 0);
			statement.setString(6, apiKey);
			statement.setString(7, apiSecret);
			statement.setString(8, valutaRiferimento.getSigla());
			statement.setString(9, tipoDeposito.name());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
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
	}
	
	public Utente read(String username, String password) throws PersistenceException 
	{
		Connection connection = null;
		PreparedStatement statement = null;
		Utente result = null;
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(read_by_username);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				String saltPassword = rs.getString(SALTPASSWORD);
				String hashPassword = rs.getString(HASHPASSWORD);
				String data = password + saltPassword;
				MessageDigest mda;
				String newHashedPassword = "";
				
				try {
					// Generate digest as byte array
					mda = MessageDigest.getInstance("SHA-512", "SUN");
					byte[] byteDigest = mda.digest(data.getBytes());
					
					// Convert byte array digest to String
					StringBuilder sb = new StringBuilder();
				    for(int i=0; i < byteDigest.length;i++)
				        sb.append(Integer.toString((byteDigest[i] & 0xff) + 0x100, 16).substring(1));
				    newHashedPassword = sb.toString();
				} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
					e.printStackTrace();
				}
		
				if(newHashedPassword.equals(hashPassword)) {
					Utente utente = new Utente();
					ValutaFiat fiatRiferimento = new ValutaFiatProxy();
					StrategiaDCA strategiaDCA = new StrategiaDCAProxy();
					utente.setUsername(rs.getString(USERNAME));
					utente.setTipoDeposito(TipoDeposito.valueOf(rs.getString(TIPODEPOSITO)));
					fiatRiferimento.setSigla(rs.getString("sigla"));
					fiatRiferimento.setNome(rs.getString(NOME));
					float budget = (rs.getFloat(BUDGET));
					if(!rs.wasNull()) {
						strategiaDCA.setBudget(budget);
					}
					int intervalloInvestimento = (rs.getInt(INTERVALLOINVESTIMENTO));
					if(!rs.wasNull()) {
						strategiaDCA.setIntervalloInvestimento(intervalloInvestimento);
					}
					strategiaDCA.setUtente(utente);
					utente.setDca(strategiaDCA);
					utente.setFiatScelta(fiatRiferimento);
					result = utente;
				}
			}
			rs.close();
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

	public void updateIntervallo(StrategiaDCA dca) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;
		if(dca == null) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(update_intervallo);
			statement.setInt(1, dca.getIntervalloInvestimento());
			statement.setString(2, dca.getUtente().getUsername());
			statement.executeUpdate();
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
	}
	
	public void updateBudget(StrategiaDCA dca) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;
		if(dca == null) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(update_budget);
			statement.setFloat(1, dca.getBudget());
			statement.setString(2, dca.getUtente().getUsername());
			statement.executeUpdate();
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
	}

	public void updateDistribuzione(StrategiaDCA dca) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;
		if(dca == null) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(delete_distribuzione_utente);
			statement.setString(1, dca.getUtente().getUsername());
			statement.executeUpdate();
			Map<Criptovaluta, Float> nuovaDistribuzione = dca.getDistribuzionePercentuale();
			for(Criptovaluta cripto : nuovaDistribuzione.keySet()) {
				statement = connection.prepareStatement(update_distribuzione);
				statement.setString(1, cripto.getSigla());
				statement.setFloat(2, nuovaDistribuzione.get(cripto));
				statement.setString(3, dca.getUtente().getUsername());
				statement.executeUpdate();	
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
	}
	
	public Map<Criptovaluta, Float> getDistribuzionePercentuale(StrategiaDCA strategiaDCA) throws PersistenceException{
		Map<Criptovaluta, Float> result = new HashMap<>();
		Connection connection = null;
		PreparedStatement statement = null;
		if(strategiaDCA == null) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(get_distribuzione);
			statement.setString(1, strategiaDCA.getUtente().getUsername());
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				Criptovaluta entry = CriptovalutaFactory.GetCriptovaluta((rs.getString(SIGLA)));
				Float percentuale = rs.getFloat(PERCENTUALE);
				result.put(entry, percentuale);
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

	public float getValorePortafoglio(String username, LocalDateTime data) throws PersistenceException {
		float result = -1;
		Connection connection = null;
		PreparedStatement statement = null;
		if(username == null || username.isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(get_valore_portafoglio);
			statement.setString(1, username);
			//statement.setString(2, data);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				result = rs.getFloat("TotaleValoreCriptoInValutaFiat");
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
