package hippodrome;

import java.util.ArrayList;
import java.util.List;

public class Hippodrome {

    private List<Horse> horses;

    public static Hippodrome game;

    public static void main(String[] args) {
        game = new Hippodrome(new ArrayList<>());

        Horse one = new Horse("One", 3, 0);
        Horse two = new Horse("Two", 3, 0);
        Horse thre = new Horse("Thre", 3, 0);
        game.getHorses().add(one);
        game.getHorses().add(two);
        game.getHorses().add(thre);

        game.run();

        game.printWinner();

    }

    public Hippodrome(List<Horse> horses) {
        this.horses = horses;
    }

    public void move(){
        for (Horse hors : horses) {
            hors.move();
        }
    }

    public void print(){
        for (Horse hors : horses) {
            hors.print();
        }
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
    }

    public void run(){
        for (int i = 1; i <= 100 ; i++) {
            move();
            print();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Horse getWinner(){
        double distance = 0;
        Horse horseWin = null;
        for (Horse hors : horses) {
            if (hors.getDistance() > distance){
                distance = hors.getDistance();
                horseWin = hors;
            }
        }

        return horseWin;
    }

    public void printWinner(){
        System.out.println("Winner is " + getWinner().getName() + "!");
    }


    public List<Horse> getHorses() {
        return horses;
    }
}
