package ru.rapid;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;


public class Mysql
{
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String mysqlHost = "jdbc:mysql://localhost/rapid";

	private static String login = "rapid";
	private static String password = "123456";
	
	private Connection conn;
	private Properties mysqlProp;
	

	private Logger log = Logger.getLogger(Mysql.class);
	
	public Mysql() throws ClassNotFoundException, SQLException, FileNotFoundException
	{
		Class.forName(JDBC_DRIVER);
		
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
		ps.setInt( 1, random );
		ps.setString( 2, fileName );
		ps.setString( 3, whoUpload );
		ps.setInt( 4, random );
		ps.executeUpdate();
	}
	
	public String getFileByNumber(int number) throws SQLException, FileNotFoundException 
	{
		conn = DriverManager.getConnection(mysqlHost, mysqlProp);
		PreparedStatement ps = conn.prepareStatement( "SELECT file FROM files WHERE random = ?" );
		ps.setInt( 1, number );
		ResultSet rs = ps.executeQuery();
		
		if ( rs.next() )
		{
			return rs.getString("file");
		}
		else
		{
			throw new FileNotFoundException("File in DB not found");
		}
	}
}
