package lk.ijse.chat_app.bo;

import lk.ijse.chat_app.bo.custom.impl.ClientBOImpl;
import lk.ijse.chat_app.bo.custom.impl.LoginBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        LOGIN, CLIENT
    }

    public SuperBO getBO(BOFactory.BOTypes type) {
        switch (type) {
            case LOGIN:
                return new LoginBOImpl();
            case CLIENT:
                return new ClientBOImpl();
            default:
                return null;
        }
    }
}