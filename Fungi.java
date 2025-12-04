import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;

public class Fungi implements Actor {
    int damage = 15;
    Carcass host;
    

    Fungi(Carcass host) {
        this.host = host; 
    }
    @Override
    public void act(World world) {
        if(world.contains(host)) {
            this.dealDamage(host);
        }
    }
    public void dealDamage(Carcass target) {
        target.depleteMeat();
    }
}