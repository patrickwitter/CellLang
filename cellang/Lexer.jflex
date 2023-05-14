/* Specification for ArithExp tokens */

// user customisations
import java_cup.runtime.*;
import lib3652.util.TokenException;

// Jlex directives
//TODO Add Booleans true / false
    
%%

%cup
%public

%class Lexer

%type java_cup.runtime.Symbol

%throws TokenException

%eofval{
	return new Symbol(sym.EOF);
%eofval}

%eofclose false

%char
%column
%line
%state YYINITIAL_QUOTE
%state PARSE_STRING, ESCAPE_SEQUENCE
%state BLK_COMMENT

%{
	StringBuffer stringBuffer;

	StringBuffer strBuff = new StringBuffer();

	int nestedBlockCommentCount = 0;

    public int getChar() {
	return (int) (yychar + 1);
    }

    public int getColumn() {
    	return yycolumn + 1;
    }

    public int getLine() {
	return yyline + 1;
    }

    public String getText() {
	return yytext();
    }
%}

// Match newline characters (\n or \r)
nl = [\n\r]

// Match control characters (\b or \f) or newline characters
cc = ([\b\f]|{nl})

// Match whitespace characters, including control characters and tabs
ws = {cc}|[\t ]

// Match alphabetic characters (upper- and lower-case) and underscores
alpha = [a-zA-Z_]

// Match alphanumeric characters (letters and digits)
alphanum = {alpha}|[0-9]

blockCommentChar = [^"/*""*/"]

%%

<YYINITIAL>	{nl}	{//skip newline, but reset char counter
			 yychar = 0;
			}
<YYINITIAL>	{ws}	{
			 // skip whitespace
			}

/**
NOTE:  
	The order of the rules in the lexer file matters, as the lexer reads the 
	file from top to bottom and tries to match each input character against the defined rules.
**/


/// CELLANG KEYWORDS
<YYINITIAL> "table" {return new Symbol(sym.TABLE);}
<YYINITIAL> "out" {return new Symbol(sym.OUT);}
<YYINITIAL> "closeOut" {return new Symbol(sym.CLOSEOUT);}
<YYINITIAL> "import" {return new Symbol(sym.IMPORT);}
<YYINITIAL> "@"{alphanum}+ {return new Symbol(sym.MAPVAR,yytext());}

//<YYINITIAL> "mut" {return new Symbol(sym.MUTATE);}

/// Arithmetic Symbols 
<YYINITIAL>	"+"	{return new Symbol(sym.PLUS);}
<YYINITIAL>	"-"	{return new Symbol(sym.MINUS);}
<YYINITIAL>	"*"	{return new Symbol(sym.MUL);}
<YYINITIAL>	"/"	{return new Symbol(sym.DIV);}
<YYINITIAL>	"%"	{return new Symbol(sym.MOD);}

/// Assignment 
<YYINITIAL>	"="	{return new Symbol(sym.ASSIGN);}

/// Braces 
<YYINITIAL>	"("	{return new Symbol(sym.LPAREN);}
<YYINITIAL>	")"	{return new Symbol(sym.RPAREN);}
<YYINITIAL>	"{"	{return new Symbol(sym.LBRACE);} 
<YYINITIAL>	"}"	{return new Symbol(sym.RBRACE);}
<YYINITIAL>	"["	{return new Symbol(sym.LBRAK);}
<YYINITIAL> "]" {return new Symbol(sym.RBRAK);}

//SEMI COLON 
<YYINITIAL>	";"	{return new Symbol(sym.SEMI);}
<YYINITIAL>	","	{return new Symbol(sym.COMMA);}
<YYINITIAL> "fun" {return new Symbol(sym.FUN);}


/// IF syntax 
<YYINITIAL> "if" {return new Symbol(sym.IF);}
<YYINITIAL> "else" {return new Symbol(sym.ELSE);}
<YYINITIAL> "end" {return new Symbol(sym.END);}
//TODO MOVE TO LANG KEYWORDS
<YYINITIAL> "print"     {return new Symbol(sym.PRINT);}
<YYINITIAL> "println"   {return new Symbol(sym.PRINTLN);}

// OTHER SYMBOLS
<YYINITIAL> ":" {return new Symbol(sym.COLON);}
<YYINITIAL> "." {return new Symbol(sym.DOT);}

/// RELATIONAL Symbols 
<YYINITIAL>	"=="	{return new Symbol(sym.EQU);}
<YYINITIAL> "<" {return new Symbol(sym.CMP,Cmp.LT);}
<YYINITIAL> "<=" {return new Symbol(sym.CMP,Cmp.LE);}
<YYINITIAL> "!=" {return new Symbol(sym.CMP,Cmp.NE);}
<YYINITIAL> ">" {return new Symbol(sym.CMP,Cmp.GT);}
<YYINITIAL> ">=" {return new Symbol(sym.CMP,Cmp.GE);}

/// Integers 
<YYINITIAL>    [0-9]+ {
	       // INTEGER
	       return new Symbol(sym.INT, 
				 Integer.valueOf(yytext()));
		}
<YYINITIAL>    [0-9]*"."[0-9]+ {
	       // DOUBLE
	       return new Symbol(sym.DOUBLE, 
				 Double.valueOf(yytext()));
		}

/// Variable Names 
<YYINITIAL>    {alpha}{alphanum}* {
	       // VAR
	       return new Symbol(sym.VAR, yytext());
		}
// STRINGS
<YYINITIAL> \"    { yybegin(YYINITIAL_QUOTE); stringBuffer = new StringBuffer(); } // switch to quote state

<YYINITIAL_QUOTE> [^\\\"\n]+   { stringBuffer.append(yytext()); } // match any characters except double quotes and backslashes
<YYINITIAL_QUOTE> "\\".   { stringBuffer.append(yytext().substring(1)); } // match escaped characters and append the unescaped character
<YYINITIAL_QUOTE> \n     { throw new TokenException("Unterminated string literal"); } // error on newline
<YYINITIAL_QUOTE> \"    { yybegin(YYINITIAL); return new Symbol(sym.STRING, stringBuffer.toString()); } // return string token and switch back to initial state

<YYINITIAL>	"//"~{nl}	{/* Line comment. Do Nothing*/}

<YYINITIAL>	"/*"	{nestedBlockCommentCount += 1;
					yychar -= 2;
					yybegin(BLK_COMMENT);}
<BLK_COMMENT> {
	"/*"	{nestedBlockCommentCount += 1;
			yychar -= 2;}
	"*/"	{nestedBlockCommentCount -= 1;
			yychar -= 2;
			if (nestedBlockCommentCount == 0){
				yybegin(YYINITIAL);
			}}
	{blockCommentChar}+	{yychar -= yytext().length();}
}

<YYINITIAL>    \S		{ // error situation
	       String msg = String.format("Unrecognised Token: %s", yytext());
	       throw new TokenException(msg);
	       }