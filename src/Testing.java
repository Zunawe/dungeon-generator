public class Testing{
    public static void main(String[] args){
        int width = 100, height = 100;
        try{
            width = args.length < 1 ? width : Integer.parseInt(args[0]);
            height = args.length < 2 ? height : Integer.parseInt(args[1]);
        }catch(NumberFormatException e){

        }
        Dungeon dungeon = new Dungeon();
        Floor floor = new Floor(width, height);
        floor.generateMap();

        System.out.println(floor);
    }
}
