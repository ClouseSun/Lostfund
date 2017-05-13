/**
 * 
 */
package cn.bit.plugin.terminal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHHelper {
	private static Session session = null;
	private static ChannelExec openChannel = null;
	private static JSch jsch = new JSch();
	private static Properties config = new Properties();
	
	public static String execSSHHelper(String command){
		String res = "";
		try {
			if(session == null){
				session = jsch.getSession(EnvironmentParams.getUsername(), EnvironmentParams.getHost(), EnvironmentParams.getPort());
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				session.setPassword(EnvironmentParams.getPwd());
				session.connect();
			}
					
			openChannel = (ChannelExec)session.openChannel("exec");
			
			openChannel.setCommand(command);
			
			int exitStatus = openChannel.getExitStatus();
			System.out.println("exitStatus: " + exitStatus);
			openChannel.connect();
			
			InputStream	 in = openChannel.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String buf = null;
			
			while((buf = reader.readLine()) != null){
				res += buf+ " \n";
			}
		} catch (JSchException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(res);
		return res;
	}
}
