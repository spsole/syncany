/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2014 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.connection.plugin.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.syncany.connection.plugins.StorageException;
import org.syncany.connection.plugins.TransferManager.StorageTestResult;
import org.syncany.connection.plugins.sftp.SftpConnection;

/**
 * @author Vincent Wiencek <vwiencek@gmail.com>
 *
 */
public class SftpTransferManagerTest {
	static {
		try {
			String credentialFile = System.getProperty("user.home") 
				+ File.separator + "syncany.test.credential";
		
			Properties prop = new Properties();
			prop.load(new FileInputStream(credentialFile));
			SANDBOX = prop.getProperty("sftp.sandbox");
			USERNAME = prop.getProperty("sftp.username");
			PASSWORD = prop.getProperty("sftp.password");
			HOST = prop.getProperty("sftp.host");
		}
		catch (Exception e) {
			
		}
	}

	private static String SANDBOX;
	private static String USERNAME;
	private static String PASSWORD;
	private static String HOST;
	
	@Test
	public void testSftpTransferManager() throws StorageException {
		Assert.assertEquals(StorageTestResult.NO_REPO, test(SANDBOX + "repoValid"));
		Assert.assertEquals(StorageTestResult.NO_REPO, test(SANDBOX + "emptyRepo"));
		Assert.assertEquals(StorageTestResult.NO_REPO_CANNOT_CREATE, test(SANDBOX + "canNotWrite/inside"));
		Assert.assertEquals(StorageTestResult.REPO_EXISTS, test(SANDBOX + "notEmptyRepo"));
	}
		
	public StorageTestResult test(String host, String path) throws StorageException{
		SftpConnection cnx = con(host);
		cnx.setPath(path);
		return cnx.createTransferManager().test();
	}
	
	public StorageTestResult test(String path) throws StorageException{
		SftpConnection cnx = con();
		cnx.setPath(path);
		return cnx.createTransferManager().test();
	}
	
	public SftpConnection con(){
		return con(HOST);
	}
	
	public SftpConnection con(String host){
		SftpConnection connection = new SftpConnection();
		connection.setHostname(host);
		connection.setPort(22);
		connection.setUsername(USERNAME);
		connection.setPassword(PASSWORD);
		return connection;
	}
}
