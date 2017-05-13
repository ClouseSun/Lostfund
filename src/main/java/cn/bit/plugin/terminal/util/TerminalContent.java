package cn.bit.plugin.terminal.util;

import java.util.ArrayList;

/**
 * 
 *  @author xc
 * @category save terminal content
 */
public class TerminalContent {
	private static String history_content = "";
	private static ArrayList<String> history_cmd = new ArrayList<String>();
	private static int cmd_index = 0;
	public static String getHistory_cmd(int request_index) {
		if(history_cmd.size() == 0){
			return "";
		}
		cmd_index += request_index;
		if(cmd_index < 0){
			cmd_index = 0;
			return history_cmd.get(cmd_index);
		}
		else if(cmd_index >= history_cmd.size()){
			cmd_index = history_cmd.size();
			return history_cmd.get(cmd_index - 1);
		}
		return history_cmd.get(cmd_index);
	}

	public static void setHistory_cmd(String history_cmd) {
		TerminalContent.history_cmd.add(history_cmd.trim());
		TerminalContent.cmd_index = TerminalContent.history_cmd.size();
	}

	public static String getHistory_content() {
		if(history_content.compareTo("") == 0){
			history_content  = "$ ";
		}
		return history_content;
	}

	public static void setHistory_content(String history_content) {
		TerminalContent.history_content = history_content;
	}
	
	public static int getHistory_content_length(){
		return history_content.length();
	}
}
