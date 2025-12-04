import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.Random;

public class Carcass implements Actor {
    public static final int default_meat = 20;

    private int remainingMeat;
    private int age = 0;
    private final int maxAge;
    private boolean hasInternalFungi;
    private final int originalSize;

    public Carcass(int meat) {
        this(meat, false);
    }
    
    public Carcass(int meat, boolean withFungi) {
        this.remainingMeat = meat;
        this.originalSize = Math.max(1, meat);


        // Større dyr → længere tid før ådselet forsvinder
        this.maxAge = 30 + meat;
        this.hasInternalFungi = withFungi;
    }

    @Override
    public act(World world) {
        if (!world.contains(this)) return;
        
        age++;

    //K3-2a: Lille chance for at der kan opstå svamp i ådslet af sig selv
    if (!hasInternalFungi) {
        Random r = new Random();
        if (r.nextDouble() < 0.02) {
            hasInternalFungi = true;
        }
    }

    if (age >= maxAge || remainingMeat <= 0) {
        Location loc = world.getLocation(this);
        world.delete(this);

        if (hasInternalFungi && loc != null) {
            int ttl = 10 + (originalSize / 2); //Større ådsel = svamp lever længere
            world.setTile(loc, new Fungi(ttl, originalSize));
        }
    }
    }
    public void depleteMeat() {
        remainingMeat -= 10;
        if(remainingMeat < 0) {
            remainingMeat = 0;
        }
    }
}


