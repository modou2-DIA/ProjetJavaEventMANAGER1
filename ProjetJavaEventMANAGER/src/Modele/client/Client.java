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
    public Client(){}
    public Client(int Id , String fullName, String email) 
    {
        this.Id= Id;        
        this.fullName= fullName;
        this.email= email;  
    }
    public void setId(int Id)
    {
        this.Id=Id;
    }
    public void setfullName(String fullName)
    {
        this.fullName=fullName;
    }
        public void setemail(String email)
    {
        this.email=email;
    }
    public int getInt()
    {
        return (Id);
    }
    public String getfullName() 
    {
        return (fullName) ;
    }    
    public String getemail()
    {
      return (email);  
    }

    @Override
    public String toString(){
        return("l'Id du client : "+Id+"\n"+"Le nom complet du client : "
                +fullName+"\nL'eamil du client: "+email );
    }
    
}
