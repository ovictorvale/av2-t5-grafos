

public class Main {
    private static final String[] ESTADOS = {
            "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA",
            "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN",
            "RO", "RR", "RS", "SC", "SE", "SP", "TO"
    };

    private static final String[] NOMES_CORES = {
                "Verde", "Amarelo", "Azul", "Branco", "Vermelho", "Laranja", "Roxo"
    };

    public static void main(String[] args) {
        // Validar entrada por linha de comando
        if (args.length < 1) {
            StdOut.println("Erro: Forneça o caminho do arquivo. Ex: java Main dados/brasil.txt");
            return;
        }

        StdOut.println("--- Lendo arquivo: " + args[0] + " ---");
        In in = new In(args[0]);
        Graph brasil = new Graph(in);


        StdOut.println("\n--- Lista de Adjacência do Brasil ---");
        StdOut.println(brasil.toString());

        GraphColoringDSatur dsatur = new GraphColoringDSatur(brasil);

        StdOut.println("--- Ordem de Coloração ---");
        int count = 1;
        for (int v : dsatur.getOrder()) {
            StdOut.printf("%dº: %s\n", count++, ESTADOS[v]);
        }

        // cor atribuída a cada estado
        StdOut.println("\n--- Coloração Final por Estado ---");
        int[] colors = dsatur.getColors();
        for (int i = 0; i < ESTADOS.length; i++) {
            int indiceCor = colors[i];

            // Se o índice da cor estiver dentro do array de cores, printa o nome
            if (indiceCor < NOMES_CORES.length) {
                StdOut.printf("%s: %s (Cor %d)\n", ESTADOS[i], NOMES_CORES[indiceCor], indiceCor);
            } else {
                // Caso use mais cores
                StdOut.printf("%s: Cor %d\n", ESTADOS[i], indiceCor);
            }
        }

        StdOut.println("\nTotal de cores utilizadas: " + dsatur.getNumColors());

        // Verificar e informar se a coloração é válida
        if (isColoringValid(brasil, colors)) {
            StdOut.println("STATUS: Coloração VÁLIDA! Ninguém briga com o vizinho.");
        } else {
            StdOut.println("STATUS: Erro! Coloração INVÁLIDA. Verifique a lógica.");
        }
    }


    // percorre todas as arestas do grafo para validar

    private static boolean isColoringValid(Graph G, int[] colors) {
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (colors[v] == colors[w]) {
                    StdOut.printf("Conflito detectado entre %s e %s (ambos cor %d)\n",
                            ESTADOS[v], ESTADOS[w], colors[v]);
                    return false;
                }
            }
        }
        return true;
    }
}