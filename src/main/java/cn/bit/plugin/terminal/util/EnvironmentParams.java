/**
 * 
 */
package cn.bit.plugin.terminal.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EnvironmentParams {
	private static String host = "";
	private static String username = "xc";
	private static String pwd = "123456";
	private static int port = 22;
	
	public static String getHost() {
		if(host.compareTo("") == 0){
			try {
				String addr = InetAddress.getLocalHost().toString();
				String[] content = addr.split("/");
				host = content[content.length - 1];
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return host;
	}
	public static void setHost(String host) {
		EnvironmentParams.host = host;
	}
	public static String getUsername() {
		if(username.compareTo("") == 0){
			username = System.getProperty("user.name");
		}
		return username;
	}
	public static void setUsername(String username) {
		EnvironmentParams.username = username;
	}
	public static int getPort() {
		return port;
	}
	public static void setPort(int port) {
		EnvironmentParams.port = port;
	}
	public static String getPwd() {
		return pwd;
	}
	public static void setPwd(String pwd) {
		EnvironmentParams.pwd = pwd;
	}
	
}
