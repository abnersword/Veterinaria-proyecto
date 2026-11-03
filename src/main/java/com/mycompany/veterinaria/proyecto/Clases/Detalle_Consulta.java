/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.Clases;

/**
 *
 * @author rivch
 */
public class Detalle_Consulta {
    
    
    private int id_detalle;
    private int id_consulta;
    private int numero_detalle;
    private String motivo_detalle;
    private String tratamiento;
    private String dosis;
    private String tiempo_dosis;

    public Detalle_Consulta(int id_detalle, int id_consulta, int numero_detalle, String motivo_detalle, String tratamiento, String dosis, String tiempo_dosis) {
        this.id_detalle = id_detalle;
        this.id_consulta = id_consulta;
        this.numero_detalle = numero_detalle;
        this.motivo_detalle = motivo_detalle;
        this.tratamiento = tratamiento;
        this.dosis = dosis;
        this.tiempo_dosis = tiempo_dosis;
    }

    public void registrarDetalle()
    {
        
    }

    public void actualizarDetalle()
    {

    }

    public void consultarDetalle()
    {

    }
    
    // Getters y Setters

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_consulta() {
        return id_consulta;
    }

    public void setId_consulta(int id_consulta) {
        this.id_consulta = id_consulta;
    }
    public int getNumero_detalle() {
        return numero_detalle;
    }

    public void setNumero_detalle(int numero_detalle) {
        this.numero_detalle = numero_detalle;
    }
    public String getMotivo_detalle() {
        return motivo_detalle;
    }

    public void setMotivo_detalle(String motivo_detalle) {
        this.motivo_detalle = motivo_detalle;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getTiempo_dosis() {
        return tiempo_dosis;
    }

    public void setTiempo_dosis(String tiempo_dosis) {
        this.tiempo_dosis = tiempo_dosis;
    }


    
}
