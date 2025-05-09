package com.example.chat;

public class PersonChat {

    private int id;

    private String name;

    private String chatMessage;

    private boolean isMeSend;

    private boolean addtime;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getChatMessage() {
        return chatMessage;
    }
    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
    public boolean isMeSend() {
        return isMeSend;
    }
    public boolean addTime() {
        return addtime;
    }
    public void setMeSend(boolean isMeSend) {
        this.isMeSend = isMeSend;
    }

    public PersonChat(int id, String name, String chatMessage, boolean isMeSend, boolean addtime) {
        super();
        this.id = id;
        this.name = name;
        this.chatMessage = chatMessage;
        this.isMeSend = isMeSend;
        this.addtime = addtime;
    }
    public PersonChat() {
        super();
    }
}
