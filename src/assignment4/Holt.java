package assignment4;

public class Holt extends Critter {

    public Holt() {
        return;
    }

    @Override
    public String toString() {return "J";}

    @Override
    public boolean fight(String not_used) {
        return true;
    }

    @Override
    public void doTimeStep() {
        return;
    }
}
