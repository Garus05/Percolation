package percolation;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private int n; // Size of percolation grid
    private int t; // Number of simulations.
    private double[] thresholds; // Array to store the resulting thresholds of each simulation.



    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) throw new IllegalArgumentException("N and T must be greater than zero.");
    
        this.n = n;
        this.t = t;
        thresholds = new double[t];

        // We create the simulations creating T instances of new Percolation objects of size N.
        for (int i = 0; i < t; i++) {
            Percolation p = new Percolation(n);

        
            // Continue opening nodes until the grid percolates.
            while (!p.percolates()) {
                openRandomNode(p);
            }

            int openSites = p.numberOfOpenSites();

            // Store the result in our array to further process, we cast it to double type.
            thresholds[i] = (double) openSites / n * n;
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }

    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }


    private void openRandomNode(Percolation p) {
        boolean openNode = true;
        int row = 0;
        int col = 0;

        // Iterate until we find a closed node randomly.
        while (openNode) {
            // Use StdRandom.uniform to create random values for row and col.
            row = StdRandom.uniformInt(1, n + 1);
            col = StdRandom.uniformInt(1, n + 1);

            openNode = p.isOpen(row, col);
        }

        // Since the node found on the prior loop is closed, we open it.
        p.open(row, col);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, t);

        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");

    }
}