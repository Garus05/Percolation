package percolation;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF grid; // Grid to make the connectivity.
    private WeightedQuickUnionUF full; // Grid to avoid backwash problem, suggested by other solutions.
    private int gridSquare;
    private int n; // Size of the grid.
    private int vTop; // Virtual top node.
    private int vBottom; // Virtual bottom node. 
    private boolean[] openNodes; // Array of the status of each node(whether opened or not). 
    private int openSites; // Count of the number of opened sites.


    // Constructor, initialises a grid in N * N size + 2 virtual nodes as well as a second grid to check for fullness.
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("The value of N must be greater than zero, please try again.");
        }

        gridSquare = n * n;

        grid = new WeightedQuickUnionUF((gridSquare + 2));
        full = new WeightedQuickUnionUF((gridSquare + 1)); // We only add 1 because we are not considering the virtual bottom to avoid the backwash problem. 
        this.n = n; // Size of the grid.
        vTop = singleArrayIdx(n, n) + 1; // Assigning index to virtual top.
        vBottom = singleArrayIdx(n, n) + 2; // Assigning index to virtual bottom. 
        openNodes = new boolean[n * n];
        openSites = 0;
    }

    private int singleArrayIdx(int i, int j) {
        boundsCheck(i, j);
        // Obtains and index for the virtual top and bottom sites.
        i -= 1;
        j -= 1;
        return (i * n) + j;
    }

    public void open(int i, int j) {
        // Check if the given values are within our constrains.
        boundsCheck(i, j);

        if (isOpen(i, j)) return; // No need to open it if it's already opened. 

        int idx = singleArrayIdx(i, j);
        openNodes[idx] = true; // We open the site changing its status.
        openSites++;

        // Case #1 - Site is in the top row, union the site with the virtual top site on both "grid" and "full". 
        if (i == 1) {
            grid.union(vTop, idx);
            full.union(vTop, idx);
        }

        // Case #2 - Site is in the bottom row, union the site with the virtual bottom site ONLY on the grid structure to avoid backwash. 
        if (i == n) grid.union(vBottom, idx);

        // Case #3 - Site is any other row, union the site with the site above it.
        if (isValid(i - 1, j) && isOpen(i - 1, j)) {
            grid.union(singleArrayIdx(i - 1, j), idx);
            full.union(singleArrayIdx(i - 1, j), idx);
        }

        // Case #4 - Site is any other row, union with the site to the right. 
        if (isValid(i, j + 1) && isOpen(i, j + 1)) {
            grid.union(singleArrayIdx(i, j + 1), idx);
            full.union(singleArrayIdx(i, j + 1), idx);
        }

        // Case #4 - Site is any other row, union with the site to the left.
        if (isValid(i, j - 1) && isOpen(i, j - 1)) {
            grid.union(singleArrayIdx(i, j - 1), idx);
            full.union(singleArrayIdx(i, j - 1), idx);
        }

        // Case #5 - Site is any other row, union with the site below it.
        if (isValid(i + 1, j) && isOpen(i + 1, j)) {
            grid.union(singleArrayIdx(i + 1, j), idx);
            full.union(singleArrayIdx(i + 1, j), idx);
        }
    }

    public boolean isOpen(int i, int j) {
        boundsCheck(i, j);

        return openNodes[singleArrayIdx(i, j)]; // We return the status of the given site, first we calculate its index on the grid. 
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean isFull(int i, int j) { // Checks if a given node is full, it is considered full if it connects to the virtual top site.
        boundsCheck(i, j);
        int idx = singleArrayIdx(i, j);
        int rootTop = full.find(vTop);
        int rootIdx = full.find(idx);
        // We use the "full" grid to avoid being affected by backwash. 
        if (rootTop != rootIdx) return;
        
        return true;
    }

    private boolean isValid(int i, int j) {
        return i > 0 && j > 0 && i <= n && j <= n;
    }

    private void boundsCheck(int i, int j) {
        if (!isValid(i, j)) {
            throw new IllegalArgumentException("Values out of bounds, please try again. ");
        }
    }

    public boolean percolates() {
        int rootTop = grid.find(vTop);
        int rootBottom = grid.find(vBottom);

        if (rootTop != rootBottom) return;

        return true;
    }
}