package server;

import common.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;

public class GameServer extends UnicastRemoteObject implements IGameServer {

    private Game game = new Game(6);
    private CyclicBarrier startBarrier = new CyclicBarrier(2);
    private List<PlayerInfo> players = new ArrayList<PlayerInfo>(2);

    public GameServer() throws RemoteException {
        super();
    }

    public synchronized PlayerInfo connect() throws RemoteException {
        if (players.size() == 2) {
            return null;
        }

        String playerUuid = UUID.randomUUID().toString();
        PlayerType playerType = PlayerType.values()[players.size()];
        PlayerInfo playerInfo = new PlayerInfo(playerType, playerUuid);

        players.add(playerInfo);
        System.out.println(String.format("Client connected. UUID: %s.", playerUuid));

        return playerInfo;
    }

    public Game waitForStart() throws RemoteException {
        try {
            startBarrier.await();
        } catch (Exception e) {
            System.err.println("Error while waiting for start.");
        }
        return game;
    }

    public synchronized Game putStone(PlayerInfo playerInfo, int rowPos, int colPos) throws RemoteException {
        int playerIndex = players.indexOf(playerInfo);
        if (playerIndex < 0 || playerInfo.getType() != game.getCurrentPlayer()) {
            game.setStatus(GameStatus.ERROR);
            return game;
        }

        GameStatus status = game.putStone(rowPos, colPos);

        if (status != GameStatus.ERROR) {
            System.out.println(String.format("Stone: i=%d, j=%d. PlayerType: %s", rowPos, colPos, playerInfo.getType()));

            if (game.getStatus() == GameStatus.IN_PROGRESS) {
                game.changePlayer();
            } else {
                System.out.println(game.getCurrentPlayer() + " won. Game ended.");
                this.notify();
                unexportObject(this, true);
            }
        }

        return game;
    }

    public synchronized Game waitForOpponent() throws RemoteException {
        this.notify();
        try {
            this.wait();
        } catch (InterruptedException e) {
            System.err.println("Error while waiting opponent.");
        }
        return game;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Initializing GameServer...");

        IGameServer gameServer = new GameServer();
        String serverName = "rmi://localhost/GameServer";
        Naming.rebind(serverName, gameServer);

        System.out.println("GameServer started.");
    }
}
