import java.util.Random;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        final String playerOne = "John";
        final String playerTwo = "Jack";
        final char pencilSign = '|';
        final int maxPencilsToRemove = 3;

        Game game = new Game(playerOne, playerTwo, pencilSign, maxPencilsToRemove);

        game.start();
    }
}

class Game {
    private final Scanner scanner = new Scanner(System.in);
    private final char pencilSign;
    private final int maxPencilsToRemove;
    private final String playerOne;
    private final String playerTwo;
    private String playerOnMove;
    private int pencilsCount;
    Game(String playerOne, String playerTwo, char pencilSign, int maxPencilsToRemove) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.pencilSign = pencilSign;
        this.maxPencilsToRemove = maxPencilsToRemove;
    }
    void start() {
        init();
        run();
    }
    private void init() {
        setPencilsCount(inputInitialPencilsCount());
        setPlayerOnMove(inputPlayerOnMove());
    }
    private void run() {
        while (! isZeroPencilsCount()) {
            printPencils();
            printPlayerOnMove();
            makeNextMove();
        }
        printWinner();
    }
    private void setPencilsCount(int count) {
        pencilsCount = count;
    }
    private void setPlayerOnMove(String player) {
        playerOnMove = player;
    }
    private int inputInitialPencilsCount() {
        int initialPencilsCount = 0;
        boolean isValidInput = false;

        System.out.println("How many pencils would you like to use:");

        while (! isValidInput) {
            try {
                initialPencilsCount  = Integer.parseInt(scanner.nextLine());

                if (initialPencilsCount <= 0) {
                    throw new RuntimeException("The number of pencils should be positive");
                }
                isValidInput = true;
            } catch (NumberFormatException e) {
                System.out.println("The number of pencils should be numeric");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        return initialPencilsCount;
    }
    private String inputPlayerOnMove() {
        String playerOnMove = "";
        boolean isValidInput = false;

        System.out.printf("Who will be the first (%s, %s):\n", playerOne, playerTwo);

        while (! isValidInput) {
            try {
                playerOnMove = scanner.nextLine();

                if (! playerOnMove.equals(playerOne) && ! playerOnMove.equals(playerTwo)) {
                    throw new WrongNameException(playerOne, playerTwo);
                }

                isValidInput = true;
            } catch (WrongNameException e) {
                System.out.println(e.getMessage());
            }
        }
        return playerOne.equals(playerOnMove) ? playerOne : playerTwo;
    }
    private void printPencils() {
        final String pens = Character.toString(pencilSign).repeat(pencilsCount);
        System.out.println(pens);
    }
    private void printPlayerOnMove() {
        System.out.printf("%s's turn!\n", playerOnMove);
    }
    private boolean isZeroPencilsCount() {
        return pencilsCount <= 0;
    }
    private void makeNextMove() {
        int pencilsCountToRemove = inputPencilsCountToRemove();
        decreasePencilsCount(pencilsCountToRemove);
        moveTurnToNextPlayer();
    }
    private int inputPencilsCountToRemove() {
        int pencilsCountToRemove = 0;
        boolean isValidInput = false;

        while (! isValidInput) {
            try {
                if (playerOnMove.equals(playerOne)) {
                    pencilsCountToRemove = Integer.parseInt(scanner.nextLine());
                } else {
                    pencilsCountToRemove = botPencilsCountToRemove();
                    System.out.println(pencilsCountToRemove);
                }

                if (pencilsCountToRemove < 1 || pencilsCountToRemove > maxPencilsToRemove) {
                    throw new NotAllowedPencilsCountToRemoveException();
                } else if (pencilsCountToRemove > pencilsCount) {
                    throw new TooManyPencilsToRemoveException();
                }
                isValidInput = true;
            } catch(NumberFormatException e) {
                System.out.println("Possible values: '1', '2' or '3");
            } catch(NotAllowedPencilsCountToRemoveException | TooManyPencilsToRemoveException e) {
                System.out.println(e.getMessage());
            }
        }
        return pencilsCountToRemove;
    }
    private int botPencilsCountToRemove() {
        int num = 0;
        switch (pencilsCount % (maxPencilsToRemove + 1)) {
            case 0:
                num = 3;
                break;
            case 1:
                num =  randomPencilsCountToRemove();
                break;
            case 2:
                num =  1;
                break;
            case 3:
                num =  2;
                break;
        }
        return num;
    }
    private int randomPencilsCountToRemove() {
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
        return randomNum;
    }
    private void decreasePencilsCount(int pencilsCountToRemove) {
        pencilsCount -= pencilsCountToRemove;
    }
    private void moveTurnToNextPlayer() {
        if (playerOnMove.equals(playerOne)) {
            playerOnMove = playerTwo;
        } else {
            playerOnMove = playerOne;
        }
    }
    private void printWinner() {
        System.out.printf("%s won!\n", playerOnMove);
    }
}
class WrongNameException extends Exception {
    WrongNameException(String nameOne, String nameTwo) {
        super(String.format("Choose between %s and %s", nameOne, nameTwo));
    }
}
class NotAllowedPencilsCountToRemoveException extends Exception {
    NotAllowedPencilsCountToRemoveException() {
        super("Possible values: '1', '2' or '3'");
    }
}
class TooManyPencilsToRemoveException extends Exception {
    TooManyPencilsToRemoveException() {
        super("Too many pencils were taken");
    }
}