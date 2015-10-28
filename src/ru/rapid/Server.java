package ru.rapid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.json.JSONObject;


@WebServlet(name = "Uploader", urlPatterns = {"/upload/*"})
@MultipartConfig
public class Server extends HttpServlet
{
	final static Logger logger = Logger.getLogger(Server.class);
	private int progress = 0;
	private HttpSession session;
	private final String path = "E:\\test";
	
	public Server()
	{
		org.apache.log4j.BasicConfigurator.configure();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
        session = request.getSession();
        logger.debug(request.getRequestURI());
        if ( request.getRequestURI().equals( "/rapid/upload/progress" ))
        {
        	response.setContentType("application/json;charset=UTF-8");
        	JSONObject json = new JSONObject();
        	json.append("progress", progress);
        	PrintWriter writer = response.getWriter();
        	writer.print(json);
        }
	}
	
	protected void doPost(HttpServletRequest request,
	        HttpServletResponse response)
	        throws ServletException, IOException 
	{
		if ( request.getRequestURI().equals( "/rapid/upload" ))
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
		        
		        logger.debug( "Session is null? -" + Boolean.valueOf(session==null).toString());
		        
		        while (( read = filecontent.read(bytes) ) != -1 ) 
		        {
		            out.write(bytes, 0, read);
		            readed += read;
		            logger.info( "File size: " + String.valueOf( fileSize ));
		            session = request.getSession();
		            if ( session != null )
		            {
		            	progress = getProgressPercent((int) fileSize, readed);
		            	logger.debug(progress);
		            	session.setAttribute( "progress", progress );
		            }
		        }
		        writer.println("New file " + fileName + " created at " + path);
		        logger.info( "File "+fileName+" being uploaded." );
		        
		    } 
		    catch (FileNotFoundException fne) 
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
}
