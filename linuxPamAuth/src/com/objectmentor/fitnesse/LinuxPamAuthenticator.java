// Copyright (C) 2003,2004 by Robert C. Martin and Micah D. Martin. All rights reserved.
// Released under the terms of the GNU General Public License version 2 or later.

package com.objectmentor.fitnesse;

import fitnesse.authentication.Authenticator;
import java.io.*;
import java.util.*;

public class LinuxPamAuthenticator extends Authenticator
{
	private Map authenticatedUsers = new HashMap();

	public static void main(String[] args) throws Exception
	{
		if(args.length != 2)
		{
			System.out.println("Usage: java com.objectmentor.fitnesse.LinuxPamAuthenticator <username> <password>");
			System.exit(1);
		}

		boolean authenticated = new LinuxPamAuthenticator().isAuthenticated(args[0], args[1]);
		if(authenticated)
			System.out.println("Authenticated");
		else
			System.out.println("Not Authenticated");
	}

	public LinuxPamAuthenticator()
	{
	}

	public LinuxPamAuthenticator(Properties p)
	{
	}

	public boolean isAuthenticated(String username, String password) throws Exception
	{
		if(username == null || "".equals(username))
			return false;

		boolean authenticated = false;
		if(authenticatedUsers.containsKey(username) && authenticatedUsers.get(username).equals(password))
			authenticated = true;
		else if(runValidation(username, password))
		{
		  authenticatedUsers.put(username, password);
			authenticated = true;
		}
		return authenticated;
	}

	private boolean runValidation(String username, String password) throws Exception
	{
		Process validation = Runtime.getRuntime().exec("/usr/bin/validate " + username);

		String passwordInput = password + '\n';
		OutputStream programInputStream = validation.getOutputStream();
		programInputStream.write(passwordInput.getBytes());
		programInputStream.flush();

		int exitCode = validation.waitFor();
		return exitCode == 0;
	}
}
