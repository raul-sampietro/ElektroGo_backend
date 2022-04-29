package elektroGo.back.model;

import elektroGo.back.data.gateways.GatewayChats;

import java.util.ArrayList;


public class Chat {
    private String userA;
    private String userB;
    private ArrayList<GatewayChats> messages;

    public Chat(String userA, String userB, ArrayList<GatewayChats> messages) {
        this.userA = userA;
        this.userB = userB;
        this.messages = messages;
    }

    public String getUserA() {
        return userA;
    }

    public void setUserA(String userA) {
        this.userA = userA;
    }

    public String getUserB() {
        return userB;
    }

    public void setUserB(String userB) {
        this.userB = userB;
    }

    public ArrayList<GatewayChats> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<GatewayChats> messages) {
        this.messages = messages;
    }
}
