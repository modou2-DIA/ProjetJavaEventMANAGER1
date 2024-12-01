/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.client;

/**
 *
 * @author nadae
 */
public class ClientPreference {
    private String langue;
    private boolean receiveMail;

    
    public ClientPreference(String langue, boolean receiveMail) {
        this.langue = langue;
        this.receiveMail = receiveMail;
    }

    
    public String getLangue() {
        return langue;
    }

    public boolean isReceiveMail() {
        return receiveMail;
    }

    
    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setReceiveMail(boolean receiveMail) {
        this.receiveMail = receiveMail;
    }

    
    @Override
    public String toString() {
        return "ClientPreference{" +
                "langue='" + langue + '\'' +
                ", receiveMail=" + receiveMail +
                '}';
    }
}