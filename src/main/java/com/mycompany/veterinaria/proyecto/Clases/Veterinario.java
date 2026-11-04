/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.Clases;
import com.mycompany.veterinaria.proyecto.DAO.VeterinarioDAO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author rivch
 */
public class Veterinario {
    private int ID_veterinario;
    private String Nombre;
    private String Telefono;
    private String Especialidad;
    
    public Veterinario (int ID_veterinario, String Nombre, String Telefono, String Especialidad){
        this.ID_veterinario = ID_veterinario;
        this.Nombre = Nombre;
        this.Telefono = Telefono;
        this.Especialidad = Especialidad;
    }   
    public void consultarVeterinario(){
        System.out.println("El ID del veterinario es: " + getID_veterinario());
        System.out.println("El nombre del veterinario es: " + getNombre());
        System.out.println("La especie del veterinario es: " + getTelefono());
        System.out.println("La raza del veterinario es: " + getEspecialidad());
        
    }
    public void registrarVeterinario() throws SQLException {
    new VeterinarioDAO().guardar(this);
}

public void actualizarDatos() throws SQLException {
    new VeterinarioDAO().actualizar(this);
}

public void eliminarVeterinario() throws SQLException {
    new VeterinarioDAO().eliminar(this.ID_veterinario);
}

public static Veterinario buscarVeterinario(int id)
        throws SQLException {

    return new VeterinarioDAO().buscarPorId(id);
}

public static List<Veterinario> listarVeterinarios()
        throws SQLException {

    return new VeterinarioDAO().listar();
}

    /**
     * @return the ID_veterinario
     */
    public int getID_veterinario() {
        return ID_veterinario;
    }

    /**
     * @param ID_veterinario the ID_veterinario to set
     */
    public void setID_veterinario(int ID_veterinario) {
        this.ID_veterinario = ID_veterinario;
    }

    /**
     * @return the Nombre
     */
    public String getNombre() {
        return Nombre;
    }

    /**
     * @param Nombre the Nombre to set
     */
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    /**
     * @return the Telefono
     */
    public String getTelefono() {
        return Telefono;
    }

    /**
     * @param Telefono the Telefono to set
     */
    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    /**
     * @return the Especialidad
     */
    public String getEspecialidad() {
        return Especialidad;
    }

    /**
     * @param Especialidad the Especialidad to set
     */
    public void setEspecialidad(String Especialidad) {
        this.Especialidad = Especialidad;
    }
    
}
