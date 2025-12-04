import itumulator.executable.DisplayInformation;
import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;

public class Fungi implements Actor {
    int damage = 15;
    Carcass host;
    int energy = 0;
    boolean hasShape = false;

    /*if(hasShape) {

    }
    */

    Fungi(Carcass host) {
        this.host = host; 
    }
    @Override
    public void act(World world) {
        Random r = new Random();
        energy += 5;
        if(host != null && world.contains(host)) {
            this.dealDamage(world, host);
            return;
        }

        if(host != null && !world.contains(host)) {
            this.hasShape = true;
            this.host = null;

            if(!hasShape) {
                return;
            }
            
            Location fungiLoc = world.getLocation(this);

            Set<Location> visibleTiles = world.getSurroundingTiles(fungiLoc, 3);
            Set<Carcass> nearbyCarcasses = world.getAll(Carcass.class, visibleTiles);
            List<Carcass> nearbyCarcassList = new ArrayList<>(nearbyCarcasses);
            if(!nearbyCarcassList.isEmpty()) {
                
                Carcass target = nearbyCarcassList.get(r.nextInt(nearbyCarcassList.size()));
                while(target.hasInternalFungi()) {
                    nearbyCarcassList.remove(target);
                    target = nearbyCarcassList.get(r.nextInt(nearbyCarcassList.size()));

                }
                target.infectWithFungi();

            }
        }
    }
    public void dealDamage(World world, Carcass target) {
        target.consume(world ,this.damage);
    }
}