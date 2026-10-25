public class Token {
    public enum Tipo {
        ABRE_CHAVE, FECHA_CHAVE,
        ABRE_COLCHETE, FECHA_COLCHETE,
        ABRE_PARENTESE, FECHA_PARENTESE,
        OUTRO, EOF
    }

    private final Tipo tipo;
    private final String lexema;
    private final int linha;
    private final int coluna;

    public Token(Tipo tipo, String lexema, int linha, int coluna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
        this.coluna = coluna;
    }

    public Tipo getTipo() {return tipo;}
    public String getLexema() {return lexema;}
    public int getLinha() {return linha;}
    public int getColuna() {return coluna;}

    public boolean isAbertura() {
        return tipo == Tipo.ABRE_CHAVE
        || tipo == Tipo.ABRE_COLCHETE
        || tipo == Tipo.ABRE_PARENTESE;
    }

    public boolean isFechamento() {
        return tipo == Tipo.FECHA_CHAVE
        || tipo == Tipo.FECHA_COLCHETE
        || tipo == Tipo.FECHA_PARENTESE;
    }

    @Override
    public String toString() {
        return tipo + " '" + lexema + "' (linha " + linha + ", coluna " + coluna + ")";
    }
}