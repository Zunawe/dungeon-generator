public class Testing{
    public static void main(String[] args){
        Dungeon dungeon = new Dungeon();
        Floor floor = new Floor(200, 100);
        floor.generateMap();

        System.out.println(floor);
    }
}
