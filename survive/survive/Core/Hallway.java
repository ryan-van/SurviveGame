package survive.Core;

import java.io.Serializable;

public class Hallway implements Serializable {
    private int p1, p2, lw, type;

    /**
     * @param p1 x or y depending on horizontal/vertical hallways
     * @param p2 x or y
     * @param lw length or width
     */
    public Hallway(int p1, int p2, int lw, int type) {
        this.p1 = p1;
        this.p2 = p2;
        this.lw = lw;
        this.type = type;
    }

    public int getP1() {
        return p1;
    }

    public int getP2() {
        return p2;
    }

    public int getLw() {
        return lw;
    }

    public int getType() {
        return type;
    }
}
