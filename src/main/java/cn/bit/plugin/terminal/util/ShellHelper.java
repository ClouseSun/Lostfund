/**
 * 
 */
package cn.bit.plugin.terminal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author xc
 *
 */
public class ShellHelper {

	public static String execShellHelper(String command){
		String res = "";
		Process process = null;
		BufferedReader bufferedReader = null;
		
		try {
			String[] shell = {"/bin/sh", "-c", command};
			process = Runtime.getRuntime().exec(shell);
			System.out.println("pid = " + process.toString());
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			process.waitFor();
			
			String line = null;
			
			while(bufferedReader != null && (line = bufferedReader.readLine()) != null){
				res += line + "\n";
			}
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(res);
		return res;
	}
}
