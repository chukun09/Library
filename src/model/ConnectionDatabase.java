package model;

import java.sql.*;

public class ConnectionDatabase {
	public static Connection ConnectionData(String name) throws SQLException {
		Connection connection = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionURL = "jdbc:sqlserver://DESKTOP-8MSKLND\\SQLEXPRESS:1433;databaseName=" + name
					+ ";integratedSecurity=true;";
			connection = DriverManager.getConnection(connectionURL, "sa", "longngoc14121902");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

}
