import java_cup.runtime.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import lib3652.util.Result;
import lib3652.util.TokenException;
// import java.lang.StringBuilder;

public class Main {

    public static final String PROMPT = "> ";

    private static final String MESSAGE = "Type your input at the prompt." +
	"  Terminate with a '.' on a line by itself.\n" +
	"Quit by entering a '.' as the only line or by sending EOF to input.";

    public static void usage() {
	String[] usageMsg = new String[]{
	    String.format("Usage: <javaexec> %s [-w <walker-class-name>] " +
			  "[file ...]", Main.class.getName()),
	    "walker-class-name must be the name of a class that subclasses ",
	    "the class PersistentWalker and has a constructor that takes no",
	    "arguments.  Defaults to Evaluator.",
	    "",
	    "The sequence of filenames provided afterwards is optional.  Each",
	    "will be read and traversed in the order given.  If a '-' is",
	    "specified, input will be read from stdin.  If no files are given,",
	    "input willl be read from stdin."
	};
	for (String line : usageMsg) {
	    System.out.println(line);
	}
    }

    public static <S, T> void main(String args[]) {
	int n = args.length;
	String walkerName = "";
	ArrayList<String> filenames = new ArrayList<>();

	if (n == 0) {
	    usage();
	    System.exit(0);
	}
	
	for (int i = 0; i  < n; i++) {
	    String arg = args[i];
	    if (arg.equals("-h") || arg.equals("--help")) {
		usage();
		System.exit(0);
	    } else if (arg.equals("-w")) {
		walkerName = args[i+1];
		i += 1;
	    } else {
		filenames.add(arg);
	    }
	}

	try {
	    AssessmentVisitor<S, T> walker;	// to be set
	    if (walkerName.equals("")) {
		// walker = new ArithInterpreter();
		walkerName = "CellInterpereter";
	    } 
	    Class<? extends AssessmentVisitor<S, T>> wclass =
		(Class<? extends AssessmentVisitor<S, T>>) Class.forName(walkerName);
	    walker = (wclass.newInstance());

	    for (String fname : filenames) {
		Reader fr = null;
		try {
		    if (fname.equals("-")) {
			fr = new InputStreamReader(System.in);
			System.out.println(MESSAGE);
		    } else {
			fr = new FileReader(fname);
			System.out.println("Processing " + fname + "...");
		    }
		    readLines(fr, walker);
		} catch (FileNotFoundException fnfe) {
		    System.err.println(fnfe.getMessage());
		} finally {
		    try { 
			if (fr != null)
			    fr.close();
		    } catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		    }
		}
	    }
	} catch (ClassNotFoundException cnfe) {
	    System.err.println(cnfe.getMessage());
	    cnfe.printStackTrace();
	    System.exit(1);
	} catch (InstantiationException ie) {
	    System.err.println(ie.getMessage());
	    ie.printStackTrace();
	    System.exit(1);
	} catch (IllegalAccessException iae) {
	    System.err.println(iae.getMessage());
	    iae.printStackTrace();
	    System.exit(1);
	}
    }

	/***
	 * reads input lines from a given Reader until 
	 * it encounters a line with a single ''.'' character, 
	 * and then sends that input to be parsed,
	 *  walked, and displayed by the parseWalkShow method.
	 * 
	 */
    public static <S, T> void readLines(Reader in,
					AssessmentVisitor<S, T> walker) {
	LineNumberReader scanner = null;
	try {
	    StringBuilder input = new StringBuilder(256);
	    scanner = new LineNumberReader(in);
	    while (true) {
		try {
		    System.out.print(PROMPT);
		    String line = scanner.readLine();
		    while (line != null && !line.equals(".")) {
			// we add a newline character so the lexer can see it
			input.append(line + "\n");
			line = scanner.readLine();
		    }
		    if (input.toString().equals("")) {
			break;
		    } else {
			parseWalkShow(new StringReader(input.toString()),
				      walker);
			input.delete(0, input.length());
		    }
		    if (line == null) {
			break;
		    }
		} catch (IOException ioe) {
		    System.err.println(ioe.getMessage());
		}
	    }
	} finally {
	    try { 
		scanner.close();
	    } catch(IOException ioe) {
		System.err.println(ioe.getMessage());
	    }
	}
    }

	/**
	 * The parseWalkShow method reads a program from a given 
	 * Reader using the AssessmentVisitor and prints the resulting 
	 * value to the console.
	 * 
	 */
    public static <S, T> void parseWalkShow(Reader reader,
					    AssessmentVisitor<S, T> avisitor) {
	Result res = avisitor.run(reader);
	String out = "NONE";
	switch (res.getType()) {
	case ERROR_LEXER:
	    System.out.println("Lexer Error: " + res.getValue());
	    break;
	case ERROR_PARSER:
	    System.out.println("Parse Error: " + res.getValue());
	    break;
	case ERROR_RUNTIME:
	    System.out.println("Runtime Error: " + res.getValue());
	    break;
	default:
	    out = res.getValue().toString();
	}
	System.out.println("Result: " + out);
    }

}
