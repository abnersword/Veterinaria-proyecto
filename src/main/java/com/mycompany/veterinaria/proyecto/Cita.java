/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto;

import java.sql.SQLException;

/**
 *
 * @author rivch
 */
public class Cita {
    private int ID_cita;
    private int NumeroCita;
    private String NombreMascota;
    private String FechaCita;
    private String HoraCita;
    private String Estado;
    
    public Cita (int ID_cita,int NumeroCita, String NombreMascota, 
            String FechaCita, String HoraCita, String Estado){
        this.ID_cita = ID_cita;
        this.NumeroCita = NumeroCita;
        this.NombreMascota = NombreMascota;
        this.FechaCita = FechaCita;
        this.HoraCita = HoraCita;
        this.Estado = Estado;
    }
    
    public void RegistrarCita() throws SQLException{
        CitaDAO cdao = new CitaDAO();
        cdao.guardar(this);
    }
    
    public void CancelarCita(){
        
    }
    
    public Boolean ActualizarEstado(){ 
        return null;
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
     * @return the NombreMascota
     */
    public String getNombreMascota() {
        return NombreMascota;
    }

    /**
     * @param NombreMascota the NombreMascota to set
     */
    public void setNombreMascota(String NombreMascota) {
        this.NombreMascota = NombreMascota;
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
