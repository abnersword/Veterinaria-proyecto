/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.Clases;

import com.mycompany.veterinaria.proyecto.DAO.CitaDAO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author rivch
 */
public class Cita {
    private int ID_cita;
    private int NumeroCita;
    private int ID_mascota;
    private int ID_veterinario;
    private String FechaCita;
    private String HoraCita;
    private String Estado;
    
    public Cita (int ID_cita,int NumeroCita, int ID_mascota, int ID_veterinario, 
            String FechaCita, String HoraCita, String Estado){
        this.ID_cita = ID_cita;
        this.NumeroCita = NumeroCita;
        this.ID_mascota = ID_mascota;
        this.ID_veterinario = ID_veterinario;
        this.FechaCita = FechaCita;
        this.HoraCita = HoraCita;
        this.Estado = Estado;
    }
    
    
    public void CancelarCita(){
        
    }
    
    public Boolean ActualizarEstado(){ 
        return null;
    }
public void RegistrarCita() throws SQLException {
    new CitaDAO().guardar(this);
}

public void actualizarCita() throws SQLException {
    new CitaDAO().actualizar(this);
}

public void eliminarCita() throws SQLException {
    new CitaDAO().eliminar(this.ID_cita);
}

public static Cita buscarCita(int id) throws SQLException {
    return new CitaDAO().buscarPorId(id);
}

public static List<Cita> listarCitas() throws SQLException {
    return new CitaDAO().listar();
}
    /**
     * @return the ID_cita
     */
    public int getID_cita() {
        return ID_cita;
    }

    /**
     * @param ID_cita the ID_cita to set
     */
    public void setID_cita(int ID_cita) {
        this.ID_cita = ID_cita;
    }

    /**
     * @return the NumeroCita
     */
    public int getNumeroCita() {
        return NumeroCita;
    }

    /**
     * @param NumeroCita the NumeroCita to set
     */
    public void setNumeroCita(int NumeroCita) {
        this.NumeroCita = NumeroCita;
    }

    /**
     * @return the ID_mascota
     */
    public int getID_mascota() {
        return ID_mascota;
    }

    /**
     * @param ID_mascota the NombreMascota to set
     */
    public void setID_mascota(int ID_mascota) {
        this.ID_mascota = ID_mascota;
    }
    
    public int getID_veterinario(){
        return ID_veterinario;
    }
    /**
     * @param ID_veterinario the ID_veterinario to set
     */
    public void setID_veterinario(int ID_veterinario){
        this.ID_veterinario = ID_veterinario;
    }

    /**
     * @return the FechaCita
     */
    public String getFechaCita() {
        return FechaCita;
    }

    /**
     * @param FechaCita the FechaCita to set
     */
    public void setFechaCita(String FechaCita) {
        this.FechaCita = FechaCita;
    }

    /**
     * @return the HoraCita
     */
    public String getHoraCita() {
        return HoraCita;
    }

    /**
     * @param HoraCita the HoraCita to set
     */
    public void setHoraCita(String HoraCita) {
        this.HoraCita = HoraCita;
    }

    /**
     * @return the Estado
     */
    public String getEstado() {
        return Estado;
    }

    /**
     * @param Estado the Estado to set
     */
    public void setEstado(String Estado) {
        this.Estado = Estado;
    }
    
    
}
