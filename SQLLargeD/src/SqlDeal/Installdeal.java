package SqlDeal;

import java.lang.reflect.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.jdbc.*;

@SuppressWarnings("unused")
public class Installdeal {
	
	private final String USERNAME = "MENO";
	private final String PASSWORD = "12345";
	private final String LINKURL = "jdbc:mysql://localhost:3306/test";
	private final String DRIVE = "com.mysql.jdbc.Driver";
	
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	public Installdeal() throws ClassNotFoundException {
		Class.forName(DRIVE);
	}
	
	public Connection getConnection() {

		try {
			connection =  DriverManager.getConnection(LINKURL, USERNAME, PASSWORD);
		}catch(Exception e){
			e.getMessage();
		}
		return connection;
	}
}
