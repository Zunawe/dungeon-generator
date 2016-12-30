public class Tile{
    public enum Type{
        EMPTY,
        FLOOR,
        WALL
    }

    private Type type;

    public Tile(){
        this.type = Type.EMPTY;
    }

    public Tile(Type type){
        this.type = type;
    }

    public Type getType(){
        return type;
    }

    public void setType(Type type){
        this.type = type;
    }

    @Override
    public String toString(){
        switch(type){
            case EMPTY:
                return "Empty";
            case FLOOR:
                return "Floor";
            case WALL:
                return "Wall";
            default:
                return "No Type";
        }
    }
}
