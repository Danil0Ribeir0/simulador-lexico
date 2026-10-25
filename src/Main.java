import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Uso: java Main <arquivo de casos>");
            return;
        }

        int total = 0, aceitos = 0, rejeitados = 0;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(args[0]), StandardCharsets.UTF_8)) {
            String linha;
            int numero = 0;

            while ((linha = br.readLine()) != null) {
                numero++;
                if (numero == 1 && linha.startsWith("\uFEFF")) {
                    linha = linha.substring(1); // remove BOM, se houver
                }
                String limpa = linha.trim();
                if (limpa.isEmpty() || limpa.startsWith("#")) continue;

                total++;
                System.out.println("==============================================================");
                System.out.println("Caso " + total + " (linha " + numero + " do arquivo): " + linha);
                System.out.println(AutomatoPilha.cabecalho());

                AutomatoPilha pda = new AutomatoPilha();
                Lexer lexer = new Lexer(new StringReader(linha));

                Token t;
                while (pda.getEstado() == AutomatoPilha.Estado.q0 && (t = lexer.yylex()) != null) {
                    pda.processar(t);
                }
                if (pda.getEstado() == AutomatoPilha.Estado.q0) {
                    pda.processar(new Token(Token.Tipo.EOF, "EOF", 1, linha.length() + 1));
                }

                for (String passo : pda.getTraco()) {
                    System.out.println(passo);
                }

                if (pda.isAceita()) {
                    aceitos++;
                    System.out.println("Resultado: ACEITA");
                } else {
                    rejeitados++;
                    System.out.println("Resultado: REJEITADA - " + pda.getMotivo());
                }
            }
        }

        System.out.println("==============================================================");
        System.out.println("Total: " + total + " | Aceitas: " + aceitos + " | Rejeitadas: " + rejeitados);
    }
}