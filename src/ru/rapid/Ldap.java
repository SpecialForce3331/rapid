package ru.rapid;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class Ldap
{
	private final String host = "192.168.2.210";
	private final String domain = "akvnzm";

	public boolean auth(String login, String password)
	{
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
		    e.printStackTrace();
		    return false;
		}
	}
}
