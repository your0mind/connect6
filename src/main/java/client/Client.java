package client;

import common.Game;
import common.GameStatus;
import common.IGameServer;
import common.PlayerInfo;

import java.rmi.Naming;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        String objectName = "rmi://" + args[0] + "/GameServer";
        IGameServer gameServer = (IGameServer) Naming.lookup(objectName);

        PlayerInfo myInfo = gameServer.connect();
        System.out.println(String.format("Successfully connected to GameServer. UUID: %s.\n", myInfo.getUuid()));

        Game game = gameServer.waitForStart();
        System.out.println("Game started!");

        if (game.getCurrentPlayer() != myInfo.getType()) {
            System.out.println("Waiting for opponent...\n");
            game = gameServer.waitForOpponent();
        }

        Scanner input = new Scanner(System.in);
        while (game.getStatus() != GameStatus.END) {
            game.printBoard();

            System.out.println("\nYour turn. Please select cell position.");

            System.out.print("Row position: ");
            int rowPos = input.nextInt();

            System.out.print("Col position: ");
            int colPos = input.nextInt();

            game = gameServer.putStone(myInfo, rowPos, colPos);

            if (game.getStatus() == GameStatus.ERROR) {
                System.out.println("\nError! Try again.\n");
                continue;
            } else if (game.getStatus() == GameStatus.END) {
                break;
            }

            System.out.println("Waiting for opponent...\n");
            game = gameServer.waitForOpponent();
        }

        game.printBoard();
        System.out.println("\nYou " + (game.getCurrentPlayer() == myInfo.getType() ? "won!" : "lose!"));
    }
}
