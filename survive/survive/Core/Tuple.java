package survive.Core;

import java.io.Serializable;

public class Tuple implements Serializable {
    protected final int x;
    protected final int y;
    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
