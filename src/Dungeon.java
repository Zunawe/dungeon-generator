

public class Dungeon{
    public static final int DEFAULT_DEPTH = 5;

    private Floor[] floors;
    private int depth;

    public Dungeon(){
        this.depth = DEFAULT_DEPTH;
        this.floors = new Floor[depth];
    }


}
