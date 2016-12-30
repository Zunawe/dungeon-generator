import java.util.Random;
import java.util.ArrayList;

public class Floor{
    public static final int DEFAULT_HEIGHT = 100;
    public static final int DEFAULT_WIDTH = 100;
    public static final double PROPORTION_WALLS = 0.47;
    public static final int NUM_STEPS = 3;
    public static final double TOLERANCE = 0.5;

    private Tile[][] tileMap;
    private int width, height;

    /* == Constructors == */
    public Floor(){
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;

        this.tileMap = uniformMap(width, height, Tile.Type.EMPTY);
    }

    public Floor(int width, int height){
        this.width = width;
        this.height = height;

        this.tileMap = uniformMap(width, height, Tile.Type.EMPTY);
    }

    /* == Instance Methods == */
    public void generateMap(){
        do{
            tileMap = randomMap(width, height, PROPORTION_WALLS);
            for (int i = 0; i < NUM_STEPS; ++i) {
                tileMap = stepAutomata(tileMap);
                fillEdges(tileMap);
            }
            tileMap = flood(tileMap);
        }while(proportionFloor(tileMap) < TOLERANCE);
    }

    /* == Static Methods == */
    public static Tile[][] stepAutomata(Tile[][] map){
        int width = map[0].length;
        int height = map.length;
        Tile[][] newMap = new Tile[height][width];

        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                ArrayList<Tile.Type> neighbors = getNeighborTypes(j, i, map);
                int walls = 0;
                for(Tile.Type neighborType: neighbors){
                    if(neighborType == Tile.Type.WALL){
                        ++walls;
                    }
                }

                walls += map[i][j].getType() == Tile.Type.WALL ? 1 : 0;
                newMap[i][j] = walls >= 5 ? new Tile(Tile.Type.WALL) : new Tile(Tile.Type.FLOOR);
            }
        }

        return newMap;
    }

    public static ArrayList<Tile.Type> getNeighborTypes(int x, int y, Tile[][] map){
        ArrayList<Tile.Type> neighbors = new ArrayList(8);

        for(int i = y - 1; i <= y + 1; ++i){
            for(int j = x - 1; j <= x + 1; ++j){
                if(i == y && j == x)
                    continue;

                try{
                    neighbors.add(map[i][j].getType());
                }catch(IndexOutOfBoundsException e){
                    neighbors.add(Tile.Type.WALL);
                }
            }
        }

        return neighbors;
    }

    public static void fillEdges(Tile[][] map){
        for(int i = 0; i < map[0].length; ++i){
            map[0][i].setType(Tile.Type.WALL);
            map[map.length - 1][i].setType(Tile.Type.WALL);
        }
        for(int i = 0; i < map.length; ++i){
            map[i][0].setType(Tile.Type.WALL);
            map[i][map[0].length - 1].setType(Tile.Type.WALL);
        }
    }

    public static Tile[][] flood(Tile[][] map){
        Random random = new Random();
        int width = map[0].length;
        int height = map.length;

        int x, y;
        do{
            x = random.nextInt(width);
            y = random.nextInt(height);
        }while(map[y][x].getType() != Tile.Type.FLOOR);

        try{
            return startFlood(map, x, y);
        }catch(StackOverflowError e){
            System.out.println("Stack Overflow: Cave too big");
            return uniformMap(width, height, Tile.Type.EMPTY);
        }
    }

    private static Tile[][] startFlood(Tile[][] map, int x, int y){
        boolean[][] flooded = new boolean[map.length][map[0].length];
        for(int i = 0; i < flooded.length; ++i){
            for(int j = 0; j < flooded[0].length; ++j){
                flooded[i][j] = false;
            }
        }
        spreadFlood(map, flooded, x, y);

        Tile[][] newMap = uniformMap(map[0].length, map.length, Tile.Type.WALL);
        for(int i = 0; i < flooded.length; ++i){
            for(int j = 0; j < flooded[0].length; ++j){
                if(flooded[i][j]){
                    newMap[i][j].setType(Tile.Type.FLOOR);
                }
            }
        }

        return newMap;
    }

    private static void spreadFlood(Tile[][] map, boolean[][] flooded, int x, int y){
        if(!flooded[y][x] && map[y][x].getType() != Tile.Type.FLOOR){
            return;
        }

        flooded[y][x] = true;
        if(!flooded[y][x - 1]) spreadFlood(map, flooded, x - 1, y);
        if(!flooded[y - 1][x]) spreadFlood(map, flooded, x, y - 1);
        if(!flooded[y][x + 1]) spreadFlood(map, flooded, x + 1, y);
        if(!flooded[y + 1][x]) spreadFlood(map, flooded, x, y + 1);
    }

    public static Tile[][] uniformMap(int width, int height, Tile.Type type){
        Tile[][] tileMap = new Tile[height][width];

        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                tileMap[i][j] = new Tile(type);
            }
        }

        return tileMap;
    }

    public static Tile[][] randomMap(int width, int height, double proportionWalls){
        Tile[][] tileMap = new Tile[height][width];
        Random random = new Random();

        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                Tile newTile;
                if(random.nextDouble() < proportionWalls){
                    newTile = new Tile(Tile.Type.WALL);
                }
                else{
                    newTile = new Tile(Tile.Type.FLOOR);
                }
                tileMap[i][j] = newTile;
            }
        }

        return tileMap;
    }

    public double proportionFloor(Tile[][] map){
        double numFloorTiles = 0;
        for(int i = 0; i < map.length; ++i){
            for(int j = 0; j < map[0].length; ++j){
                if(map[i][j].getType() == Tile.Type.FLOOR) {
                    ++numFloorTiles;
                }
            }
        }

        return numFloorTiles / (map.length * map[0].length);
    }

    @Override
    public String toString(){
        String output = "";

        for(int i = 0; i < height; ++i){
            for(int j = 0; j < width; ++j){
                switch(tileMap[i][j].getType()) {
                    case EMPTY:
                        output += " ";
                        break;
                    case FLOOR:
                        output += ".";
                        break;
                    case WALL:
                        output += "#";
                        break;
                    default:
                        output += "!";
                }
            }
            output += "\n";
        }

        return output;
    }
}
