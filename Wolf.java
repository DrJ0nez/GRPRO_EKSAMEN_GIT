import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.world.NonBlocking;
import itumulator.simulator.Actor;
import java.util.Scanner;
import java.util.*;

public class Wolf extends Animal {

    
    private Location caveLocation;
    private WolfPack pack;
    
    public Wolf() {
        super(25, 100, 25);
        
    }
    @Override
    public void act(World world) {
        super.act(world);
        if (!world.contains(this)) {
            return;
        }
        Random r = new Random();
        //Location nuværendeLoc = world.getCurrentLocation();
        Location nuværendeLoc = world.getLocation(this);
        if (nuværendeLoc == null) {
            return;
        }
        Set<Location> neighbourTiles = world.getSurroundingTiles(nuværendeLoc, 2);
        Set<Rabbit> neighbourRabbits = world.getAll(Rabbit.class, neighbourTiles);
        if (!neighbourRabbits.isEmpty()) {
            List<Rabbit> rabbitList = new ArrayList<>(neighbourRabbits);
            Rabbit target = rabbitList.get(r.nextInt(rabbitList.size()));

            Location rabbitLocation = world.getLocation(target);
            Set<Location> emptyAroundRabbit = world.getEmptySurroundingTiles(rabbitLocation);
            List<Location> tilesAroundRabbit = new ArrayList<>(emptyAroundRabbit);
            world.move(this, tilesAroundRabbit.get(r.nextInt(tilesAroundRabbit.size())));

            world.delete(target);
            // så reduceres sult.
            hunger = 0;
            energy = energy + 25;
        }

        if(caveLocation == null) {
            //Set<Location> neighbourTilesForCave = world.getEmptySurroundingTiles();
            Location myLoc = world.getLocation(this);
            if (myLoc == null) return;
            Set<Location> neighbourTilesForCave = world.getEmptySurroundingTiles(myLoc);
            //
            List<Location> caveSpawns = new ArrayList<>(neighbourTilesForCave);
            if(!caveSpawns.isEmpty()) {
                Location newCaveLocation = caveSpawns.get(r.nextInt(caveSpawns.size()));
            while(!world.isTileEmpty(newCaveLocation) || world.containsNonBlocking(newCaveLocation)) {
                newCaveLocation = caveSpawns.get(r.nextInt(caveSpawns.size()));
            }
            WolfCave wC = new WolfCave();
            world.setTile(newCaveLocation, wC);
            this.caveLocation = newCaveLocation;
            pack.setCaveLocation(newCaveLocation);
            }
            List<Wolf> packMedlemmer = pack.getWolves();
            for(Wolf w: packMedlemmer) {
                if(w.   getCaveLocation() != pack.getCaveLocation()){
                    w.setCaveLocation(pack.getCaveLocation());
                }
            }
        }
        
        //Location myLoc = world.getCurrentLocation();
        Location myLoc = world.getLocation(this);
        Set<Location> visibleTiles = world.getSurroundingTiles(myLoc, 3);
        Set<Wolf> nearbyWolves = world.getAll(Wolf.class, visibleTiles);

        // Filter wolves from same pack
        List<Wolf> nearbyWolf = new ArrayList<>(nearbyWolves);
        List<Wolf> samePack = new ArrayList<>();
        List<Wolf> enemyPack = new ArrayList<>();

        for (Wolf w : nearbyWolf){
            if (w == this) continue;

            if (w.getWolfPack() == this.getWolfPack()) {
                samePack.add(w);
            } else {
                enemyPack.add(w);
            }
        }


    if (!enemyPack.isEmpty()) {
        Wolf enemy = enemyPack.get(r.nextInt(enemyPack.size()));

        Location enemyLoc = world.getLocation(enemy);
        Set<Location> emptyAroundEnemy = world.getEmptySurroundingTiles(enemyLoc);
            if (!emptyAroundEnemy.isEmpty()) {
                List<Location> options = new ArrayList<>(emptyAroundEnemy);
                world.move(this, options.get(r.nextInt(options.size())));
                this.dealDamage(enemy);
            }

        if (enemy.getCurrentHealth() <= 0 || !world.contains(enemy)) {
            this.energy += 25;
            world.delete(enemy);
        }
    }

        // Pick random wolf from same pack
        if(!samePack.isEmpty()){
            Wolf target = samePack.get(r.nextInt(samePack.size()));
            Location targetLoc = world.getLocation(target);
            // get empty tiles around the target and walk onto one of the emtpy tiles
            Set<Location> emptyAroundTarget = world.getEmptySurroundingTiles(targetLoc);
            if (!emptyAroundTarget.isEmpty()) {
                List<Location> options = new ArrayList<>(emptyAroundTarget);
                world.move(this, options.get(r.nextInt(options.size())));
            }

        }
        
        if(world.isNight()) {
            Set<Location> omkringCave = world.getSurroundingTiles(this.caveLocation);
            
            // behold kun felter som er tomme (ingen blocking ulv/bjørn osv.)
            omkringCave.removeIf(loc -> !world.isTileEmpty(loc));
            
            if (omkringCave.isEmpty()) {
                // ingen ledige felter rundt om hulen → gør ingenting denne tur
                return;
            }
            
            List<Location> omkringCaveFelter = new ArrayList<>(omkringCave);
            Location moveTo = omkringCaveFelter.get(r.nextInt(omkringCaveFelter.size()));
            
            // VIGTIGT: flyt ulven som objekt, ikke fra en location
            world.move(this, moveTo);
            
            if(r.nextDouble() < 0.15) {
            
            
            
                //Set<Location> emptyAround = world.getEmptySurroundingTiles();
                Location myLoc2 = world.getLocation(this);
                if (myLoc2 == null) return;
                Set<Location> emptyAround = world.getEmptySurroundingTiles(myLoc2);
                //
                emptyAround.removeIf(loc -> world.getTile(loc) != null);
            if(!emptyAround.isEmpty()) {
                List<Location> emptyList = new ArrayList<>(emptyAround);
                Location spreadLocation = emptyList.get(r.nextInt(emptyList.size()));
                
                while(world.containsNonBlocking(spreadLocation) || !world.isTileEmpty(spreadLocation)){
                    spreadLocation = emptyList.get(r.nextInt(emptyList.size()));
                }
                
                Wolf w = new Wolf();
                w.setWolfPack(this.pack);
                pack.addWolf(w);
                w.setCaveLocation(this.caveLocation);
                world.setTile(spreadLocation, w);
                }
            }
        }
        
        
    }
    
    public void setWolfPack(WolfPack pack) {
        this.pack = pack;
    }
    public Location getCaveLocation() {
        return this.caveLocation;
    }
    public void setCaveLocation(Location caveLocation) {
        this.caveLocation = caveLocation;
    }

    public WolfPack getWolfPack() {
        return this.pack;
    }
}