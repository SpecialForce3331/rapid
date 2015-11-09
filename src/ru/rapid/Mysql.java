package ru.rapid;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;


public class Mysql
{
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String mysqlHost = "jdbc:mysql://localhost/findrepair";

	private static String login;
	private static String password;
	
	private Statement stmt;
	private Connection conn;
	private Properties mysqlProp;
	

	private Logger log = Logger.getLogger(Mysql.class);
	
	public Mysql() throws ClassNotFoundException, SQLException, FileNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		
		mysqlProp = new Properties();
		mysqlProp.put("user", login);
		mysqlProp.put("password", password);
		mysqlProp.put("autoReconnect", "true");
		mysqlProp.put("useUnicode", "true");
		mysqlProp.put("characterEncoding", "utf-8");
	}
	
	public void addFile(String fileName, int fileSize, String whoUpload, int random) throws SQLException
	{
		conn = DriverManager.getConnection(mysqlHost, mysqlProp);
		PreparedStatement ps = conn.prepareStatement( "INSERT INTO files (random, file, who, size) VALUES (?,?,?,?)" );
		ps.setInt( 0, random );
		ps.setString( 1, fileName );
		ps.setString( 2, whoUpload );
		ps.setInt( 3, random );
		ps.executeUpdate();
	}
}
