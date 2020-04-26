package common;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    private PlayerType type;
    private String uuid;

    public PlayerInfo(PlayerType type, String uuid) {
        this.type = type;
        this.uuid = uuid;
    }

    public PlayerType getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PlayerInfo)) {
            return false;
        }

        PlayerInfo pi = (PlayerInfo) obj;
        return this.getType() == pi.getType() && this.getUuid().equals(pi.getUuid());
    }
}
