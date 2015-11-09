package ru.rapid;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

public class Ldap
{
	private final String host = "192.168.2.210";
	private final String domain = "akvnzm";
	private final Logger logger = Logger.getLogger(Ldap.class);
	
	public boolean auth(String login, String password)
	{
		logger.debug("Before split: " + login);
		login = login.split("@")[0];
		logger.debug("After: " + login);
		
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, 
		    "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://"+ host +":389/");

		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + login);
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
		    DirContext ctx = new InitialDirContext(env);
		    ctx.close();
		    return true;
		} 
		catch (NamingException e) 
		{
		    return false;
		}
	}
}
