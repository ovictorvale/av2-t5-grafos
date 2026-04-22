import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class GraphColoringDSatur {
    private int[] colors;       // Armazena a cor de cada estado (índice = estado)
    private List<Integer> order; // Ordem em que os estados foram coloridos
    private int numColors;      // Quantidade total de cores utilizadas

    public GraphColoringDSatur(Graph G) {
        int V = G.V();
        this.colors = new int[V];
        this.order = new ArrayList<>();
        this.numColors = 0;

        // Inicializa todos os estados como "não coloridos" (-1)
        for (int i = 0; i < V; i++) colors[i] = -1;

        solve(G);
    }

    private void solve(Graph G) {
        int V = G.V();
        boolean[] colored = new boolean[V];

        // O primeiro vértice é sempre o de maior grau (critério inicial)
        int currentV = findMaxDegreeVertex(G, colored);

        for (int i = 0; i < V; i++) {
            // Atribui a menor cor disponível ao vértice escolhido
            int color = findLowestAvailableColor(currentV, G);
            colors[currentV] = color;
            colored[currentV] = true;
            order.add(currentV);

            // Atualiza o total de cores
            if (color + 1 > numColors) numColors = color + 1;

            //Escolhe o próximo vértice baseado no Grau de Saturação
            if (i < V - 1) {
                currentV = selectNextVertex(G, colored);
            }
        }
    }


    //  Calcula o numero de cores DISTINTAS nos vizinhos já coloridos.

    private int calculateDS(int v, Graph G) {
        Set<Integer> neighborColors = new HashSet<>();
        for (int neighbor : G.adj(v)) {
            if (colors[neighbor] != -1) {
                neighborColors.add(colors[neighbor]);
            }
        }
        return neighborColors.size();
    }

    /**
     * Escolhe o proximo vertice não colorido com maior DS.
     * Em caso de empate, usa o maior grau como criterio de desempate.
     */
    private int selectNextVertex(Graph G, boolean[] colored) {
        int bestV = -1;
        int maxDS = -1;
        int maxDegree = -1;

        for (int v = 0; v < G.V(); v++) {
            if (!colored[v]) {
                int currentDS = calculateDS(v, G);
                int currentDegree = G.degree(v);

                if (currentDS > maxDS) {
                    maxDS = currentDS;
                    maxDegree = currentDegree;
                    bestV = v;
                } else if (currentDS == maxDS) {
                    // em caso de empate usa o grau do vértice
                    if (currentDegree > maxDegree) {
                        maxDegree = currentDegree;
                        bestV = v;
                    }
                }
            }
        }
        return bestV;
    }

    private int findLowestAvailableColor(int v, Graph G) {
        boolean[] usedColors = new boolean[G.V()];
        for (int neighbor : G.adj(v)) {
            if (colors[neighbor] != -1) {
                usedColors[colors[neighbor]] = true;
            }
        }
        int color = 0;
        while (usedColors[color]) color++;
        return color;
    }

    private int findMaxDegreeVertex(Graph G, boolean[] colored) {
        int maxDeg = -1;
        int bestV = -1;
        for (int v = 0; v < G.V(); v++) {
            if (!colored[v] && G.degree(v) > maxDeg) {
                maxDeg = G.degree(v);
                bestV = v;
            }
        }
        return bestV;
    }

    public int[] getColors() { return colors; }
    public List<Integer> getOrder() { return order; }
    public int getNumColors() { return numColors; }
}