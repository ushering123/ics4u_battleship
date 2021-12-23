import java.io.IOException;
import java.util.*;

/* Sarina Li, Vivien Cai, Jiaan Li
* Mon December 20
* ICS4U1
* Main Class
*/

public class Main {
    static Scanner sc = new Scanner(System.in);
    static boolean gamestate = true;
    static boolean AIFirst = false;

    // public static Coordinate playerPlacementBoard[][] = new Coordinate[11][11];
    public static Coordinate playerAttackBoard[][] = new Coordinate[11][11];

    public static Coordinate AIPlacementBoard[][] = new Coordinate[11][11];
    public static Coordinate AIAttackBoard[][] = new Coordinate[11][11];

    public static void main(String[] args) {
        // instantiating Coordinate for boards
        initArrays();

        System.out.println("Hello, welcome to Sarina, Vivien, and Jiaan's battleship game.");
        System.out.println("______________________________________________________________");

        // Determining who goes first
        System.out.println("To determine who goes first, lets do a coin flip!");
        coinFlip();

        // generating random placement for AI PlacementBoard
        AI.place(AIPlacementBoard);
        System.out.println("AI Placement Board: ");
        Game.printPlacementArray(AIPlacementBoard);

        System.out.println("When inputting things in the code, make sure to use all capitals. ");
        System.out.println("If one of your ships were hit, type HIT, [SHIPTYPE]. EX. HIT, BATTLESHIP ");
        System.out.println("If one of your ships were sunk, type SUNK, [SHIPTYPE]. EX. SUNK, BATTLESHIP ");
        promptEnterKey();

        if (AIFirst == true) {
            while (gamestate) {
                ArrayList<Ship> shipsAlive = Ship.getList();
                ArrayList<String> playerShipsAlive = Ship.getPlayerListOfShipsAlive();
                if (shipsAlive.size() == 0) {
                    System.out.println("AI lost, player wins");
                    break;
                }
                if (playerShipsAlive.size() == 0) {
                    System.out.println("AI won, player lost");
                    break;
                }

                System.out.println("AI ships alive: ");
                for (Ship aliveShip : shipsAlive) {
                    System.out.println(aliveShip);
                }
                System.out.println("Player ships alive: ");
                for (String aliveShip : playerShipsAlive) {
                    System.out.println(aliveShip);
                }
                AIMoves(shipsAlive, playerShipsAlive);
                playerMoves(shipsAlive, playerShipsAlive);

            }
        }

        else {
            while (gamestate) {
                ArrayList<Ship> shipsAlive = Ship.getList();
                ArrayList<String> playerShipsAlive = Ship.getPlayerListOfShipsAlive();
                if (shipsAlive.size() == 0) {
                    System.out.println("AI lost, player wins");
                    break;
                }
                if (playerShipsAlive.size() == 0) {
                    System.out.println("AI won, player lost");
                    break;
                }

                System.out.println("AI ships alive: ");
                for (Ship aliveShip : shipsAlive) {
                    System.out.println(aliveShip);
                }
                System.out.println("Player ships alive: ");
                for (String aliveShip : playerShipsAlive) {
                    System.out.println(aliveShip);
                }
                playerMoves(shipsAlive, playerShipsAlive);
                AIMoves(shipsAlive, playerShipsAlive);

            }
        }

        // loop through all 5 ships
        // limit the range where they can place the ships based on its size
        // ask for vertical vs horizontal orientation and "home coordinate"
        // place each ship and assign the home coordinate for the ship and check if
        // there are any ships where it is trying to be placed
        // create the ship class and put it in the arraylist

    }

    public static void playerMoves(ArrayList<Ship> shipsAlive, ArrayList<String> playerShipsAlive) {
        // print the placement array of the player and the hit array of the player
        System.out.println("Your attack board: ");
        Game.printPlacementArray(playerAttackBoard);

        System.out.println("Please enter a letter from A-J for the vertical part of your coordinate: ");
        char inputy = AI.getInputY();
        System.out.println("Please enter a number from 1-10 for the horizontal part of your coordinate: ");
        int inputx = AI.getInputX();

        // check if the players hit hits a ship and mark it on the ai placement board
        Coordinate cur = AIPlacementBoard[inputy][inputx];
        Coordinate curPlayer = playerAttackBoard[inputy][inputx];
        if (cur.getIsShip() && !cur.getIsHit()) {
            String key = Game.getAccessKey(inputy, inputx);
            Ship shipHit = Game.AIMapOfCoor.get(key);
            shipHit.addTimesHit();
            String shipHitName = shipHit.getName();

            if (shipHit.getTimesHit() == shipHit.getSize()) {
                Ship remove = new Ship();
                // Ship.getList().
                for (Ship aliveShip : shipsAlive) {
                    if (aliveShip.getName().equals(shipHitName)) {
                        remove = aliveShip;
                        Game.removeShipFromGrid(shipHit);

                    }
                }
                shipsAlive.remove(remove);
                System.out.printf("SUNK, %s\n", shipHit.getName());
            } else {
                System.out.printf("HIT, %s\n", shipHit.getName());

            }
            cur.setIsHit(true);
            curPlayer.setIsHit(true);
            curPlayer.setIsShip(true);
        } else if (cur.getIsShip() && cur.getIsHit() || !cur.getIsShip()) {
            cur.setIsHit(true);
            System.out.println("MISS");

        }
    }

    public static void AIMoves(ArrayList<Ship> shipsAlive, ArrayList<String> playerShipsAlive) {
        // ai generate a hit using hit or hunt
        System.out.println("AI's turn");
        // generate a hit
        if (AI.isHunting) {
            if (Hunting.uniqueHitPoints.size() > 0) {
                System.out.println("Still hunting!!!!");
                Coordinate h = Hunting.uniqueHitPoints.get(0);
                String ship = Hunting.shipsHit.get(0);
                Hunting.hunt(h, ship);
            } else {
                System.out.println("Some error occured ur so fcked hahsldkfjalsdkjf");
            }
            // Hunting.hunt(h, ship);
        } else {
            AI.findProbability();
        }
        Game.printPlacementArray(AIAttackBoard);
        // verify if hit or not
        // print the probability board and AIATtackBoard
    }

    // Method that initilalzies our game boards so they do not have null values
    public static void initArrays() {
        Ship emptyShip = new Ship(false, 0, new Coordinate(0, 0));

        for (int i = 0; i <= 10; i++) {
            for (int j = 0; j <= 10; j++) {
                char keyChar = Coordinate.convertIntToChar(i);
                String key = String.valueOf(keyChar) + String.valueOf(j);
                Game.AIMapOfCoor.put(key, emptyShip);
                // playerPlacementBoard[i][j] = new Coordinate(i, j);
                playerAttackBoard[i][j] = new Coordinate(i, j);
                AIPlacementBoard[i][j] = new Coordinate(i, j);
                AIAttackBoard[i][j] = new Coordinate(i, j);
                Hunting.huntingProbability[i][j] = new Coordinate(i, j);
            }
        }
    }

    public static void coinFlip() {
        String coin;
        int iCoin;
        int coinToss;
        boolean coinValid = false;
        while (!coinValid) {
            System.out.println("Please enter 1 if you want to pick heads and 2 if you want to pick tails.");

            coin = sc.next();
            coinToss = (int) (Math.random() * 2 + 1);

            if (!coin.equals("1") && !coin.equals("2")) {
                System.out.println("You did not enter a valid input. Please try again.");
            } else {
                iCoin = Integer.parseInt(coin);
                if (iCoin == 1 && iCoin == coinToss) {
                    System.out.print("You picked heads.");
                    System.out.println(" The coin landed on heads.");
                    System.out.println("You are going first!");
                    AIFirst = false;
                } else if (iCoin == coinToss) {
                    System.out.print("You picked tails.");
                    System.out.println(" The coin landed on tails.");
                    System.out.println("You are going first!");
                    AIFirst = false;
                } else if (iCoin == 1) {
                    System.out.println("You picked heads.");
                    System.out.println(" The coin landed on tails.");
                    System.out.println("The AI is going first.");
                    AIFirst = true;
                } else {
                    System.out.println("You picked tails.");
                    System.out.println(" The coin landed on heads.");
                    System.out.println("The AI is going first.");
                    AIFirst = true;
                }
                coinValid = true;
            }

        }
        promptEnterKey();
    }

    // copied from stack overflow LMAO
    public static void promptEnterKey() {
        System.out.println("Press \"ENTER\" to continue...");
        try {
            int read = System.in.read(new byte[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
