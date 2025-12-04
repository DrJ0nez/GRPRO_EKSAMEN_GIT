import java.awt.Color;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Scanner;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        // sætter size til første input linje.
        int size = Integer.parseInt(scanner.nextLine());
        
        int amount = 0;
        Random r = new Random();
        String type = null;
        
        
        
        Program p = new Program(size, 800, 2000);
        World w = p.getWorld();
        
        //dette køres når der stadig er flere input linjer.
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            
            if (line.isEmpty()) {
                break;
            }
            
            //split linje op i to dele, hvor første del er typen der skal indsættes i verden.
            String[] parts = line.split(" ");
            type = parts[0];
            
            //anden del er hvor mange der skal indsættes.
            String amountSize = parts[1];
            
            
            //hvis mængden der skal indsættes indeholder bindestreg.
            if(amountSize.contains("-")) {
                String[] numbers = amountSize.split("-");
                int min = Integer.parseInt(numbers[0]);
                int max = Integer.parseInt(numbers[1]);
                amount = min + r.nextInt(max - min + 1);
            }
            // hvis mængden der skal indsættes ikke indeholder bindestreg.
            else if(!amountSize.contains("-")) {
                amount = Integer.parseInt(amountSize);
            }
            
            //checker hvad der skal indsættes og indsætter det på tilfældige locationer.
            if(type.equals("burrow")) {
                for(int i = 0; i < amount; i++) {
                    Location n1 = getEmptyLocation(w, size, r);
                    w.setTile(n1, new Hole());
                }
            }
            
            else if(type.equals("rabbit")) {
                for(int i = 0; i < amount; i++) {
                    Location n1 = getEmptyLocation(w, size, r);
                    w.setTile(n1, new Rabbit());
                }
            }
            
            else if(type.equals("grass")) {
                for(int i = 0; i < amount; i++) {
                    Location n1 = getEmptyLocation(w, size, r);
                    w.setTile(n1, new Plant());
                }
            }
            
            else if(type.equals("berry")) {
                for(int i = 0; i < amount; i++) {
                    Location n1 = getEmptyLocation(w, size, r);
                    w.setTile(n1, new BerryBush());
                }
            }
            
            else if(type.equals("wolf")) {
                WolfPack pack = new WolfPack(r.nextInt(1000));
                for(int i = 0; i<amount; i++) {
                    Wolf wolf = new Wolf(); 
                    wolf.setWolfPack(pack);
                    pack.addWolf(wolf);
                    Location n1 = getEmptyLocation(w, size, r);
                    w.setTile(n1, wolf);
                }
            }
            
            else if (type.equals("bear")) {
                Location territory = null;
                if (line.contains("(") && line.contains(")")) {
                    String coordStr = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                    String[] coords = coordStr.split(",");
                    int x = Integer.parseInt(coords[0].trim());
                    int y = Integer.parseInt(coords[1].trim());
                    territory = new Location(x, y);
                }

                for (int i = 0; i < amount; i++) {
                    Location spawn;
                    
                    // Hvis der er et territorie, start der eller tæt på
                    if (territory != null && w.isTileEmpty(territory)) {
                        spawn = territory;
                    } else {
                        spawn = getEmptyLocation(w, size, r);
                    }
                    w.setTile(spawn, new Bear(territory));
                }
            }
            
            else if (type.equals("berry")) {
                for(int i = 0; i < amount; i++) {
                    Location n1 = getEmptyLocation(w, size, r);
                    w.setTile(n1, new BerryBush());
                }
            }

            //K3-1a & K2-2a
            else if (type.equals("carcass")) {
                boolean hasFungi = false;
                if (parts.length > 2 && parts[2].equals("fungi")) {
                    hasFungi = true;
                }

                for (int i = 0; i< amount; i++) {
                    Location n1 = getEmptyLocation(w, size, r);
                    w.setTile(n1, new Carcass());
                }
            }
        }
        
        
        
        // giver hver type en forskellig farve/sprite.
        DisplayInformation diRabbit = new DisplayInformation(Color.white, "rabbit-large");
        p.setDisplayInformation(Rabbit.class, diRabbit);
        
        DisplayInformation diPlant = new DisplayInformation(Color.red, "grass");
        p.setDisplayInformation(Plant.class, diPlant);
        
        DisplayInformation diHole = new DisplayInformation(Color.blue, "hole-small");
        p.setDisplayInformation(Hole.class, diHole);

        DisplayInformation diWolf = new DisplayInformation(Color.black, "wolf");
        p.setDisplayInformation(Wolf.class, diWolf);
        
        DisplayInformation diWolfCave = new DisplayInformation(Color.gray);
        p.setDisplayInformation(WolfCave.class, diWolfCave);
        
        DisplayInformation diBear = new DisplayInformation(Color.red, "bear");
        p.setDisplayInformation(Bear.class, diBear);
        
        DisplayInformation diBerryBush = new DisplayInformation(Color.red, "bush-berries");
        p.setDisplayInformation(BerryBush.class, diBerryBush);
        
        DisplayInformation diBush = new DisplayInformation(Color.red, "bush");
        p.setDisplayInformation(Bush.class, diBush);

        DisplayInformation diCarcass = new DisplayInformation(Color.darkGray, "carcass");
        p.setDisplayInformation(Carcass.class, diCarcass);
        
        DisplayInformation diFungi = new DisplayInformation(Color.orange, "fungi");
        p.setDisplayInformation(Fungi.class, diFungi);
        
        // w.setTile(new Location(0, 0), new <MyClass>());

        // p.setDisplayInformation(<MyClass>.class, new DisplayInformation(<Color>, "<ImageName>"));

        p.show();
        for (int i = 0; i < 200; i++) {
            p.simulate();
            }
        }
    
    
    //Hjælpemetode til tom lokation
    private static Location getEmptyLocation(World w, int size, Random r) {
           int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location loc = new Location(x, y);
        
        while(!w.isTileEmpty(loc) || w.containsNonBlocking(loc)) {
            x = r.nextInt(size);
            y = r.nextInt(size);
            loc = new Location(x, y);
        }
        
        return loc;
    }
}

