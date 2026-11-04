/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.Clases;
import com.mycompany.veterinaria.proyecto.DAO.DueñoDAO;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author rivch
 */
public class Dueño {
    private String telefono;
    private String nombre;
    private String direccion;
    private int id_dueño;
    
    public Dueño (String telefono, String nombre,String direccion, int id_dueño )
    {
        this.telefono=telefono;
        this.nombre=nombre;
        this.id_dueño=id_dueño;
        this.direccion=direccion;
        
    }
    public void registrarDueño() throws SQLException {
        new DueñoDAO().guardar(this);
    }

    public void actualizarDatos() throws SQLException {
        new DueñoDAO().actualizar(this);
    }
    public void consultarDueño()
    {
        
    }
    

public void eliminarDueño() throws SQLException {
    new DueñoDAO().eliminar(this.id_dueño);
}

public static Dueño buscarDueño(int id) throws SQLException {
    return new DueñoDAO().buscarPorId(id);
}

public static List<Dueño> listarDueños() throws SQLException {
    return new DueñoDAO().listar();
}
    // Getters y setters
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getId_dueño() {
        return id_dueño;
    }

    public void setId_dueño(int id_dueño) {
        this.id_dueño = id_dueño;
    }

    
}
