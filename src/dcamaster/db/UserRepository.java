package dcamaster.db;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dcamaster.model.StrategiaDCA;
import dcamaster.model.TipoDeposito;
import dcamaster.model.Utente;
import dcamaster.model.ValutaFiat;

public class UserRepository {

	private ControllerPersistenza controller;
	
	//---- TABLE UTENTI ----------------------------------------------------------------------------------
	
	private static final String TABLE_UTENTI = "utenti";
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
			+ "U." + INTERVALLOINVESTIMENTO 
			+ "U." + BUDGET 
			+ "U." + HASHPASSWORD
			+ " U." + SALTPASSWORD
			+ " U." + TIPODEPOSITO
			+ " V.* "
			+" FROM " + TABLE_UTENTI + " AS U INNER JOIN " + TABLE_VALUTEFIAT + "AS V"
			+ "ON U." + VALUTAFIATRIFERIMENTO + " = V.sigla"
			+ " WHERE U." + USERNAME + " = ? ";
	
	//update table
	private static final String update = "";
	
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
	
	public Utente read(String username, String password) throws PersistenceException {
		Connection connection = null;
		PreparedStatement statement = null;
		Utente result = null;
		if (username == null || username.isEmpty()) {
			System.out.println("read(): cannot read an entry with an invalid name");
			return result;
		}
		connection = controller.getConnection();
		try {
			statement = connection.prepareStatement(read_by_username);
			statement.setString(1, username);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				String saltPassword = rs.getString("saltPassword");
				String hashPassword = rs.getString("hashPassword");
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
					ValutaFiat fiatRiferimento = new ValutaFiat();
					StrategiaDCA strategiaDCA = new StrategiaDCA();
					utente.setUsername(rs.getString("username"));
					utente.setTipoDeposito(TipoDeposito.valueOf(rs.getString("tipoDeposito")));
					fiatRiferimento.setSigla(rs.getString("valutaFiatRiferimento"));
					fiatRiferimento.setNome(rs.getString("nome"));
					strategiaDCA.setBudget(rs.getFloat("budget"));
					strategiaDCA.setIntervalloInvestimento(rs.getInt("intervalloInvestimento"));
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
	
	public void update(StrategiaDCA strategiaDCA) {
		
	}
	
}
