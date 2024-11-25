/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.client;

import java.util.Scanner;

/**
 *
 * @author nadae
 */
public class Client {
    private int Id ;
    private String fullName ;
    private String email ;
    public Client(){
        Scanner x = new Scanner(System.in);
        System.out.print("tapez l'Id du client \n");       
        Id=x.nextInt();
        x.nextLine();
        System.out.print("tapez le nom complet du client \n");
        fullName=x.nextLine();
        System.out.print("tapez l'email du client \n");
        email=x.nextLine(); 
    }
    @Override
    public String toString(){
        return("l'Id du client : "+Id+"\n"+"Le nom complet du client : "
                +fullName+"\nL'eamil du client: "+email );
    }
    
}

