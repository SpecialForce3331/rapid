package ru.rapid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;


@WebServlet("/")
@MultipartConfig
public class Server extends HttpServlet
{
	final static Logger logger = Logger.getLogger(Server.class);
	private int progress = 0;
	private final String path = "/tmp";
	private Mysql mysql;
	private Ldap ldap;
	private String prefix = "/rapid";
	
	public Server() throws ClassNotFoundException, FileNotFoundException, SQLException
	{
		org.apache.log4j.BasicConfigurator.configure();
		mysql = new Mysql();
		ldap = new Ldap();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		HttpSession session = request.getSession();
		logger.debug(request.getRequestURI());
		
		if ( request.getRequestURI().startsWith(prefix + "/download/"))
		{
			int number = Integer.parseInt(request.getRequestURI().split(prefix + "/download/")[1]);
			try {
				String file = mysql.getFileByNumber(number);
				FileInputStream fileStream = new FileInputStream(path + "/" + file);
				ServletOutputStream outputStream = response.getOutputStream();
				
				response.setHeader("Content-Disposition", "attachment; filename=\"" + file + "\"");
				
				IOUtils.copy(fileStream, outputStream);
				outputStream.flush();
				
				outputStream.close(); // depends on your application
			} catch (SQLException e) {
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().println(e.getMessage());
			}
			
		}
		else if ( session.getAttribute("authorized") != null && (boolean)session.getAttribute("authorized") )
		{
			if ( request.getRequestURI().equals( prefix + "/"))
			{
				response.sendRedirect( prefix + "/upload");
			}
			else if ( request.getRequestURI().equals( prefix + "/upload") )
			{
				RequestDispatcher rd = request.getRequestDispatcher( "/upload.jsp" );
				rd.forward( request, response );
			}
			else if ( request.getRequestURI().equals( prefix + "/upload/progress" ))
	        {
	        	response.setContentType("application/json;charset=UTF-8");
	        	JSONObject json = new JSONObject();
	        	json.append("progress", progress);
	        	PrintWriter writer = response.getWriter();
	        	writer.print(json);
	        }
		}
		else if ( request.getRequestURI().equals( prefix + "/") )
		{
			RequestDispatcher rd = request.getRequestDispatcher( "/index.jsp" );
			rd.forward( request, response );
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		logger.debug(request.getRequestURI());
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		
		if ( request.getRequestURI().equals( prefix + "/login" ) )
		{
			String login = request.getParameter( "login" );
			String password = request.getParameter( "password" );
			if ( login != null && !login.isEmpty() && password != null && !password.isEmpty() )
			{
				if( ldap.auth( login, password ) )
				{
					session.setAttribute( "authorized", true );
					session.setAttribute("login", login);
					response.sendRedirect(prefix + "/upload");
				}
				else
				{
					response.setContentType("text/html;charset=UTF-8");
					response.getWriter().println("�� ������� �� ���������� ����� ��� ������! \n" + "<a href='/rapid'>����������� ��� ���</a>");
				}
			}
		}
		else if ( request.getRequestURI().equals( prefix + "/upload" ))
		{
		    response.setContentType("text/html;charset=UTF-8");

		    final Part filePart = request.getPart("file");
		    final String fileName = getFileName(filePart);
		    final long fileSize = filePart.getSize();

		    OutputStream out = null;
		    InputStream filecontent = null;
		    final PrintWriter writer = response.getWriter();
		    
		    try 
		    {
		        out = new FileOutputStream( new File(path + File.separator + fileName) );
		        
		        filecontent = filePart.getInputStream();
		        logger.info( "File size: " + String.valueOf( fileSize ));
		        
		        int read = 0;
		        int readed = 0;
		        int bufferSize = 1024;
		        final byte[] bytes = new byte[bufferSize];
		        
		        logger.debug( "Session is null? -" + Boolean.valueOf(session == null).toString());
		        
		        while (( read = filecontent.read(bytes) ) != -1 ) 
		        {
		            out.write(bytes, 0, read);
		            readed += read;
		            logger.info( "File size: " + String.valueOf( fileSize ));

		            if ( session != null )
		            {
		            	progress = getProgressPercent((int) fileSize, readed);
		            	logger.debug(progress);
		            	session.setAttribute( "progress", progress );
		            }
		        }
		        writer.println("New file " + fileName + " created at " + path);
		        logger.info( "File "+fileName+" being uploaded." );
		        
		        mysql.addFile( fileName, (int) fileSize, (String)session.getAttribute("login"), generateRandom(6) );
		        
		    } 
		    catch (FileNotFoundException | SQLException fne) 
		    {
		        writer.println("You either did not specify a file to upload or are "
		                + "trying to upload a file to a protected or nonexistent "
		                + "location.");
		        writer.println("<br/> ERROR: " + fne.getMessage());

		        logger.error( "Problems during file upload. Error: " + fne.getMessage() );
		    } 
		    finally 
		    {
		    	progress = 0;
		    	
		        if (out != null) {
		            out.close();
		        }
		        if (filecontent != null) {
		            filecontent.close();
		        }
		        if (writer != null) {
		            writer.close();
		        }
		    }
		}

	}

	private String getFileName(final Part part) {
	    final String partHeader = part.getHeader("content-disposition");
	    logger.info(String.format("Part Header = {0}", partHeader));
	    for (String content : part.getHeader("content-disposition").split(";")) 
	    {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}	
	
	private int getProgressPercent(int fileSize, int readedByte)
	{
		return readedByte/(fileSize/100);
	}
	
	private int generateRandom( int length )
	{
		Random r = new Random();  
  
		StringBuilder number = new StringBuilder();  
		
        int counter=0;  
  
        while(counter++< length) number.append( r.nextInt(9) );  
  
        return Integer.parseInt(number.toString());  
	  
	}
}
