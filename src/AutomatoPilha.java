import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class AutomatoPilha {

    public enum Estado { q0, q_aceita, q_erro }

    private static final String Z0 = "Z0";
    private static final String ABRES  = "{([";
    private static final String FECHAS = "})]";

    private Estado estado = Estado.q0;
    private final Deque<String> pilha = new ArrayDeque<>();
    private final List<String> traco = new ArrayList<>();
    private int passo = 0;
    private String motivo = "";

    public AutomatoPilha() {
        pilha.push(Z0);
    }

    public static String cabecalho() {
        return String.format("%-5s %-10s %-6s %-6s %-22s %-9s %s",
                "Passo", "Le", "Topo", "Regra", "Operacao", "Estado", "Pilha");
    }

    public void processar(Token t) {
        if (estado != Estado.q0) return;

        String topo = pilha.peek();
        String lexema = t.getLexema();
        String le = lexema.length() > 8 ? lexema.substring(0, 8) + "~" : lexema;
        String regra;
        String operacao;

        if (t.isAbertura()) {
            // regras 1, 2, 3
            pilha.push(lexema);
            regra = String.valueOf(ABRES.indexOf(lexema) + 1);
            operacao = "empilha " + lexema;

        } else if (t.isFechamento()) {
            // regras 4, 5, 6 (ok) ou 7, 8, 9 (erro)
            int i = FECHAS.indexOf(lexema);
            String esperado = String.valueOf(ABRES.charAt(i));

            if (topo.equals(esperado)) {
                pilha.pop();
                regra = String.valueOf(i + 4);
                operacao = "desempilha " + esperado;
            } else {
                estado = Estado.q_erro;
                regra = String.valueOf(i + 7);
                operacao = "nenhuma (erro)";
                if (topo.equals(Z0)) {
                    motivo = "'" + lexema + "' na coluna " + t.getColuna()
                            + ": fechamento sem abertura correspondente (pilha vazia)";
                } else {
                    String fechaTopo = String.valueOf(FECHAS.charAt(ABRES.indexOf(topo)));
                    motivo = "'" + lexema + "' na coluna " + t.getColuna()
                            + ": esperava fechar com '" + fechaTopo
                            + "' mas encontrou '" + lexema + "'";
                }
            }

        } else if (t.getTipo() == Token.Tipo.EOF) {
            // regras 11 e 12
            le = "EOF";
            if (topo.equals(Z0)) {
                estado = Estado.q_aceita;
                regra = "11";
                operacao = "aceita";
            } else {
                estado = Estado.q_erro;
                regra = "12";
                operacao = "nenhuma (erro)";
                motivo = "fim da entrada com " + (pilha.size() - 1)
                        + " delimitador(es) aberto(s) sem fechar: " + abertosTexto();
            }

        } else {
            // regra 10: OUTRO
            regra = "10";
            operacao = "nenhuma";
        }

        registrar(le, topo, regra, operacao);
    }

    public List<String> getTraco() { return traco; }
    public Estado getEstado() { return estado; }
    public boolean isAceita() { return estado == Estado.q_aceita; }
    public String getMotivo() { return motivo; }

    private void registrar(String le, String topo, String regra, String operacao) {
        passo++;
        traco.add(String.format("%-5d %-10s %-6s %-6s %-22s %-9s %s",
                passo, le, topo, regra, operacao, estado.name(), pilhaTexto()));
    }

    // pilha escrita do fundo para o topo: [Z0 { (]
    private String pilhaTexto() {
        StringBuilder sb = new StringBuilder("[");
        Iterator<String> it = pilha.descendingIterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) sb.append(' ');
        }
        return sb.append(']').toString();
    }

    // delimitadores ainda abertos (sem o Z0), do fundo para o topo
    private String abertosTexto() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = pilha.descendingIterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals(Z0)) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(s);
        }
        return sb.toString();
    }
}