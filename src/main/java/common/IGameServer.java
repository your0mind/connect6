package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameServer extends Remote {
    PlayerInfo connect() throws RemoteException;
    Game waitForStart() throws RemoteException;
    Game putStone(PlayerInfo playerInfo, int rowPos, int colPos) throws RemoteException;
    Game waitForOpponent() throws RemoteException;
}
