/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Event;

/**
 *
 * @author DELL
 */
public class EventCtegory {
    private int id;
    private String categoryName;
    private String colorCode ;

    public EventCtegory() {
    }
    
    
    public EventCtegory(int id, String categoryName, String colorCode) {
        this.id = id;
        this.categoryName = categoryName;
        this.colorCode = colorCode;
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    
}

