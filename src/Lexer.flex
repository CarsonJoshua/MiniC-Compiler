/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2000 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%%

%class Lexer
%byaccj
%line
%column

%{

  public Parser   parser;
  //public int      lineno;
  //public int      column;

  public Lexer(java.io.Reader r, Parser parser) {
    this(r);
    this.parser = parser;
    //this.lineno = 1;
    //this.column = 1;
  }

  public int getLine(){
    return yyline+1;
  }
  public int getColumn(){
    return yycolumn+1;
  }
%}

num          = [0-9]+("."[0-9]+)?
identifier   = [a-zA-Z][a-zA-Z0-9_]*
newline      = \n
whitespace   = [ \t\r]+
linecomment  = "//".*
blockcomment = "{"[^]*"}"

%%

"func"                              { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.FUNC       ; }
"return"                            { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.RETURN     ; }
"var"                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.VAR        ; }
"if"                             { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.IF      ; }
"then"                             { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.THEN      ; }
"else"                             { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.ELSE      ; }
"begin"                             { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.BEGIN      ; }
"end"                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.END        ; }
"while"                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.WHILE        ; }
"("                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.LPAREN     ; }
")"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.RPAREN     ; }
"["                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.LBRACKET     ; }
"]"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.RBRACKET     ; }
"new"                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.NEW        ; }
"num"                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.NUM        ; }
"bool"                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.BOOL        ; }
"print"                             { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.PRINT      ; }
"."                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.DOT        ; }
"size"                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.SIZE        ; }
":="                                { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.ASSIGN     ; }
"::"                                { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.TYPEOF     ; }
"+"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.ADD        ; }
"-"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.SUB        ; }
"*"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.MUL        ; }
"/"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.DIV        ; }
"%"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.MOD        ; }
"and"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.AND        ; }
"or"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.OR        ; }
"not"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.NOT        ; }
"<"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.LT        ; }
">"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.GT        ; }
"<="                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.LE        ; }
">="                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.GE        ; }
"="                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.EQ         ; }
"<>"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.NE        ; }
";"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.SEMI       ; }
","                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.COMMA       ; }
"true"|"false"                                 { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.BOOL_LIT       ; }
{num}                               { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.NUM_LIT    ; }
{identifier}                        { parser.yylval = new ParserVal(new Token(yytext(),getLine(),getColumn())); return Parser.IDENT      ; }
{linecomment}                       { /* skip */ }
{newline}                           { /* skip */ }
{whitespace}                        { /* skip */ }
{blockcomment}                      { /* skip */ }


\b     { System.err.println("Sorry, backspace doesn't work"); }

/* error fallback */
[^]    { System.err.println("Error: unexpected character '"+yytext()+"'"); return -1; }
