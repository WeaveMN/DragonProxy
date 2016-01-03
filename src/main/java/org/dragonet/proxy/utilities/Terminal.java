package org.dragonet.proxy.utilities;

public abstract class Terminal {
	
	public static String BOLD = "";
	public static String OBFUSCATED = "";
	public static String ITALIC = "";
	public static String UNDERLINE = ""; 
	public static String STRIKETHROUGH = "";
	public static String RESET = "";
	
	public static String BLACK = "";
	public static String DARK_BLUE = "";
	public static String DARK_GREEN = "";
	public static String DARK_AQUA = "";
	public static String DARK_RED = "";
	public static String PURPLE = "";
	public static String GOLD = "";
	public static String GRAY = "";
	public static String DARK_GRAY = "";
	public static String BLUE = "";
	public static String GREEN = "";
	public static String AQUA = "";
	public static String RED = "";
	public static String LIGHT_PURPLE = "";
	public static String YELLOW = "";
	public static String WHITE = "";
	
	protected static void getWindowsEscapeCodes(){
		BOLD = "\x1b[1m";
		OBFUSCATED = "";
		ITALIC = "\x1b[3m";
		UNDERLINE = "\x1b[4m";
		STRIKETHROUGH = "\x1b[9m";
		RESET = "\x1b[m";
		BLACK = "\x1b[38;5;16m";
		DARK_BLUE = "\x1b[38;5;19m";
		DARK_GREEN = "\x1b[38;5;34m";
		DARK_AQUA = "\x1b[38;5;37m";
		DARK_RED = "\x1b[38;5;124m";
		PURPLE = "\x1b[38;5;127m";
		GOLD = "\x1b[38;5;214m";
		GRAY = "\x1b[38;5;145m";
		DARK_GRAY = "\x1b[38;5;59m";
		BLUE = "\x1b[38;5;63m";
		GREEN = "\x1b[38;5;83m";
		AQUA = "\x1b[38;5;87m";
		RED = "\x1b[38;5;203m";
		LIGHT_PURPLE = "\x1b[38;5;207m";
		YELLOW = "\x1b[38;5;227m";
		WHITE = "\x1b[38;5;231m";
	}

	public static void init(){
		if(System.getProperty("os.name").startsWith("Windows")){
			Terminal.getWindowsEscapeCodes();
			return;
		} else if(System.getProperty("os.name").startsWith("Linux")){
			   System.out.println("Color codes on Linux not yet supported");
			   return;
		}
	}
	
}