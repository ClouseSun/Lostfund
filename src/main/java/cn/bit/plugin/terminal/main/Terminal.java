package cn.bit.plugin.terminal.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import cn.bit.plugin.terminal.util.SSHHelper;
import cn.bit.plugin.terminal.util.ShellHelper;
import cn.bit.plugin.terminal.util.TerminalContent;
/**
 * 
 * @author Ericj
 * @category terminal main window entrance
 */
public class Terminal {

//	private static JFrame terminal_app;
	private static JPanel terminal_panel;
	private static JTextArea terminal_textarea;
	private static JScrollPane terminal_scroll;
	
	private static final String line_start = "$";
//	String usrHome = System.getProperty("user.home");
//   get System param's value
//	Key                     Meaning
//	-------------------     ------------------------------
//	"file.separator"        File separator (e.g., "/")
//	"Java.class.path"       Java classpath
//	"java.class.version"    Java class version number
//	"java.home"             Java installation directory
//	"java.vendor"           Java vendor-specific string
//
//	"java.vendor.url"       Java vendor URL
//	"java.version"          Java version number
//	"line.separator"        Line separator
//	"os.arch"               Operating system architecture
//	"os.name"               Operating system name
//
//	"path.separator"        Path separator (e.g., ":")
//	"user.dir"              User's current working directory
//	"user.home"             User home directory
//	"user.name"             User account name
	
	/**
	 * constructor
	 */
	public Terminal() {
		//System.out.println(System.getProperty("user.name"));
		// set main window's style and size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		terminal_app = new JFrame("Terminal");
//		terminal_app.setSize(700, 200);
//		terminal_app.setLocation(screenSize.width / 4, screenSize.height / 4);
//		terminal_app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		Container container = terminal_app.getContentPane();
		Font fontStyle = new Font("YaHei Consolas Hybrid", Font.PLAIN, 12);
//		terminal_app.setFont(fontStyle);
		
		terminal_textarea = new JTextArea(line_start);
		terminal_textarea.setSize(screenSize.width / 5, screenSize.height / 5);
		terminal_textarea.setEditable(true);
		terminal_textarea.setFont(fontStyle);
		terminal_textarea.setLineWrap(true);
		terminal_textarea.setWrapStyleWord(true);
		terminal_textarea.enableInputMethods(false);
		terminal_textarea.setBackground(Color.BLACK);
		terminal_textarea.setForeground(Color.WHITE);
		terminal_textarea.setCaretColor(Color.WHITE);
		terminal_textarea.setCaretPosition(terminal_textarea.getText().length());
		//terminal_textarea.setComponentPopupMenu(mouseRight_menu);
		terminal_textarea.addKeyListener(new TerminalKeyBoardListener());
		
		terminal_scroll = new JScrollPane(terminal_textarea);
		terminal_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// terminal_scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		terminal_panel = new JPanel(new BorderLayout(2, 2));
		terminal_panel.add(new JLabel(), BorderLayout.NORTH);
		terminal_panel.add(new JLabel(), BorderLayout.SOUTH);
		terminal_panel.add(new JLabel(), BorderLayout.WEST);
		terminal_panel.add(new JLabel(), BorderLayout.EAST);
		terminal_panel.add(terminal_scroll, BorderLayout.CENTER);
		terminal_panel.setSize(screenSize.width / 4, screenSize.height / 4);
//		container.add(terminal_panel);
		//save terminal history content
		TerminalContent.setHistory_content(terminal_textarea.getText());
//		terminal_app.setVisible(true);
	}
	
	/**
	 * 
	 * @author Ericj
	 * @category inner class for keyBoardListener
	 */
	class TerminalKeyBoardListener implements KeyListener {
		/**
		 * override keyboard pressing event
		 */
		int input_length = 0;
		@Override
		public void keyPressed(KeyEvent key) {
			if(isNotSpecialKey(key.getKeyCode())){
				input_length++;
				terminal_textarea.setEditable(true);
			}
//			if(key.isControlDown() || )
			if(key.getKeyCode() == KeyEvent.VK_BACK_SPACE ){
				if(input_length == 0){
					terminal_textarea.setEditable(false);
				}
				else{
					input_length--;
				}
			}
//			if(key.getKeyCode() == KeyEvent.VK_LEFT){
//				if(terminal_textarea.getCaretPosition() < TerminalContent.getHistory_content_length()){
//					terminal_textarea.setCaretPosition(terminal_textarea.getText().length());
//				}
//			}
			
			System.out.println("input length = " + input_length);
		}

		/**
		 * override released keyboard event
		 */
		@Override
		public void keyReleased(KeyEvent key) {
			if(key.getKeyCode() == KeyEvent.VK_ENTER){
				input_length = 0;
				doInterpretor();
			}
			if(key.getKeyCode() == KeyEvent.VK_LEFT){
				if(terminal_textarea.getCaretPosition() < TerminalContent.getHistory_content_length()){
					terminal_textarea.setCaretPosition(terminal_textarea.getText().length());
				}
			}
			if(key.getKeyCode() == KeyEvent.VK_UP){
				String command = TerminalContent.getHistory_cmd(-1);
				input_length = command.length();
				terminal_textarea.setText(TerminalContent.getHistory_content() + command);
				terminal_textarea.setCaretPosition(terminal_textarea.getText().length());
			}
			if(key.getKeyCode() == KeyEvent.VK_DOWN){
				String command = TerminalContent.getHistory_cmd(1);
				input_length = command.length();
				terminal_textarea.setText(TerminalContent.getHistory_content() + command);
				terminal_textarea.setCaretPosition(terminal_textarea.getText().length());
			}
		}

		@Override
		public void keyTyped(KeyEvent key) {
		}
	}
	private void doInterpretor() {
		String line_content = terminal_textarea.getText().substring(TerminalContent.getHistory_content_length());
		System.out.println(line_content);
		
		String command = line_content;
		TerminalContent.setHistory_cmd(command);
//		terminal_textarea.append(ShellUtil.execShellUtil(command));
		terminal_textarea.append(ShellHelper.execShellHelper(command));
		terminal_textarea.append(line_start);
		TerminalContent.setHistory_content(terminal_textarea.getText());
	}
	/**
	 * 
	 * @param keyCode
	 * @return
	 * @category judge the pressing key is weather special key or not
	 */
	private boolean isNotSpecialKey(int keyCode) {
		boolean res = true;
		switch (keyCode) {
		case KeyEvent.VK_ENTER:
			res = false;
			break;
		case KeyEvent.VK_CONTROL:
			res = false;
			break;
		case KeyEvent.VK_SHIFT:
			res = false;
			break;
		case KeyEvent.VK_CAPS_LOCK:
			res = false;
			break;
		case KeyEvent.VK_ESCAPE:
			res = false;
			break;
		// case KeyEvent.VK_TAB:
		// res = false;
		// break;
		case KeyEvent.VK_F1:
			res = false;
			break;
		case KeyEvent.VK_F2:
			res = false;
			break;
		case KeyEvent.VK_F3:
			res = false;
			break;
		case KeyEvent.VK_F4:
			res = false;
			break;
		case KeyEvent.VK_F5:
			res = false;
			break;
		case KeyEvent.VK_F6:
			res = false;
			break;
		case KeyEvent.VK_F7:
			res = false;
			break;
		case KeyEvent.VK_F8:
			res = false;
			break;
		case KeyEvent.VK_F9:
			res = false;
			break;
		case KeyEvent.VK_F10:
			res = false;
			break;
		case KeyEvent.VK_F11:
			res = false;
			break;
		case KeyEvent.VK_F12:
			res = false;
			break;
		case KeyEvent.VK_F13:
			res = false;
			break;
		case KeyEvent.VK_F14:
			res = false;
			break;
		case KeyEvent.VK_F15:
			res = false;
			break;
		case KeyEvent.VK_F16:
			res = false;
			break;
		case KeyEvent.VK_F17:
			res = false;
			break;
		case KeyEvent.VK_F18:
			res = false;
			break;
		case KeyEvent.VK_F19:
			res = false;
			break;
		case KeyEvent.VK_F20:
			res = false;
			break;
		case KeyEvent.VK_F21:
			res = false;
			break;
		case KeyEvent.VK_F22:
			res = false;
			break;
		case KeyEvent.VK_F23:
			res = false;
			break;
		case KeyEvent.VK_F24:
			res = false;
			break;
		case KeyEvent.VK_HOME:
			res = false;
			break;
		case KeyEvent.VK_END:
			res = false;
			break;
		case KeyEvent.VK_DELETE:
			res = false;
			break;
		case KeyEvent.VK_BACK_SPACE:
			res = false;
			break;
		case KeyEvent.VK_PAGE_UP:
			res = false;
			break;
		case KeyEvent.VK_PAGE_DOWN:
			res = false;
			break;
		case KeyEvent.VK_PRINTSCREEN:
			res = false;
			break;
		case KeyEvent.VK_WINDOWS:
			res = false;
			break;
		case KeyEvent.VK_NUM_LOCK:
			res = false;
			break;
		case KeyEvent.VK_INSERT:
			res = false;
			break;
		case KeyEvent.VK_SCROLL_LOCK:
			res = false;
			break;
		case KeyEvent.VK_PAUSE:
			res = false;
			break;
		case KeyEvent.VK_UP:
			res = false;
			break;
		case KeyEvent.VK_DOWN:
			res = false;
			break;
		case KeyEvent.VK_LEFT:
			res = false;
			break;
		case KeyEvent.VK_RIGHT:
			res = false;
			break;
		case KeyEvent.VK_CONTEXT_MENU:
			res = false;
			break;
		case KeyEvent.VK_KP_UP:
			res = false;
			break;
		case KeyEvent.VK_KP_DOWN:
			res = false;
			break;
		case KeyEvent.VK_KP_LEFT:
			res = false;
			break;
		case KeyEvent.VK_KP_RIGHT:
			res = false;
			break;
		}
		return res;
	}

	public static JPanel getTerminal_panel() {
		return terminal_panel;
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Terminal();
//	}

}
