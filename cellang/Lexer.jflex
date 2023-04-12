/* Specification for ArithExp tokens */

// user customisations
import java_cup.runtime.*;
import lib3652.util.TokenException;

// Jlex directives
    
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

%{
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

nl = [\n\r]

cc = ([\b\f]|{nl})

ws = {cc}|[\t ]

alpha = [a-zA-Z_]

alphanum = {alpha}|[0-9]

%%

<YYINITIAL>	{nl}	{//skip newline, but reset char counter
			 yychar = 0;
			}
<YYINITIAL>	{ws}	{
			 // skip whitespace
			}
/// Arithmetic Symbols 
<YYINITIAL>	"+"	{return new Symbol(sym.PLUS);}
<YYINITIAL>	"-"	{return new Symbol(sym.MINUS);}
<YYINITIAL>	"*"	{return new Symbol(sym.MUL);}
<YYINITIAL>	"/"	{return new Symbol(sym.DIV);}
<YYINITIAL>	"%"	{return new Symbol(sym.MOD);}

/// Assignment 
<YYINITIAL>	":="	{return new Symbol(sym.ASSIGN);}

/// Braces 
<YYINITIAL>	"("	{return new Symbol(sym.LPAREN);}
<YYINITIAL>	")"	{return new Symbol(sym.RPAREN);}
<YYINITIAL>	"{"	{return new Symbol(sym.LBRACE);} 
<YYINITIAL>	"}"	{return new Symbol(sym.RBRACE);}

//SEMI COLON 
<YYINITIAL>	";"	{return new Symbol(sym.SEMI);}
<YYINITIAL>	","	{return new Symbol(sym.COMMA);}
<YYINITIAL> "fun" {return new Symbol(sym.FUN);}


/// IF syntax 
<YYINITIAL> "if" {return new Symbol(sym.IF);}
<YYINITIAL> "else" {return new Symbol(sym.ELSE);}
<YYINITIAL> "end" {return new Symbol(sym.END);}

<YYINITIAL> ":" {return new Symbol(sym.COLON);}

/// LOGICAL Symbols 
<YYINITIAL>	"="	{return new Symbol(sym.EQU);}
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
/// Variable Names 
<YYINITIAL>    {alpha}{alphanum}* {
	       // VAR
	       return new Symbol(sym.VAR, yytext());
		}

<YYINITIAL>    \S		{ // error situation
	       String msg = String.format("Unrecognised Token: %s", yytext());
	       throw new TokenException(msg);
	       }