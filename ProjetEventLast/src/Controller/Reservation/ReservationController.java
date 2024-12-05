
package Controller.Reservation;


import Modele.Reservations.Reservation;
import Modele.Reservations.ReservationDAO;
import Modele.client.clientDAO;
import java.sql.SQLException;
import java.util.List;

public class ReservationController {
    

    private final ReservationDAO reservationDAO ;
    private final clientDAO ClientDAO ;
            
   
 
    
    public ReservationController(ReservationDAO reservationDAO,clientDAO ClientDAO) {
        
        this.reservationDAO = reservationDAO ;
        this.ClientDAO = ClientDAO;
    }
    

     public List<Reservation> getAllReservations() throws SQLException{
         return reservationDAO.getAllReservations();
     }
     
     public void addReservation(Reservation reservation) throws SQLException 
     {
         reservationDAO.addReservation(reservation);
     }
     
     public void updateReservation(Reservation reservation) throws SQLException 
     {
         reservationDAO.updateReservation(reservation);
     }
     public void deleteReservation(int reservationId) throws SQLException
     {
         reservationDAO.deleteReservation(reservationId);
     }
     
     public List<Reservation> getReservationsByClientId(int clientId)
     {
         return reservationDAO.getReservationsByClientId(clientId);
     }
     
    
    public List<String> getAllClientNames() {
        return ClientDAO.getAllClientNames(); // Récupère tous les noms des clients
    }
    
    public String getClientNameById(int idclient)
    {
        return ClientDAO.getClientNameById(idclient);
    } 
    
    public boolean  addClient(String fullName, String email) throws Exception
    {
    try {
             ClientDAO.addClient(fullName, email);
             return true ;
         } catch (SQLException e) {
          System.err.println("Erreur SQL : " + e.getMessage());
          return false;
    }
        
    }
    
   

    public int getClientIdByName(String name) {
        return ClientDAO.getClientIdByName(name);
    }


 
}
