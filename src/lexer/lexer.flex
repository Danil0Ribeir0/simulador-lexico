%%

%class Lexer
%unicode
%line
%column
%type Token

ESPACO = [ \t\n\r]+
OUTROS = [^\{\}\(\)\[\] \t\r\n]+

%{
    private Token token(Token.Tipo tipo) {
        return new Token(tipo, yytext(), yyline + 1, yycolumn + 1);
    }
%}

%%

"{" {return token(Token.Tipo.ABRE_CHAVE);}
"}" {return token(Token.Tipo.FECHA_CHAVE);}
"(" {return token(Token.Tipo.ABRE_PARENTESE);}
")" {return token(Token.Tipo.FECHA_PARENTESE);}
"[" {return token(Token.Tipo.ABRE_COLCHETE);}
"]" {return token(Token.Tipo.FECHA_COLCHETE);}

{ESPACO}   {}
{OUTROS}   { return token(Token.Tipo.OUTRO); }