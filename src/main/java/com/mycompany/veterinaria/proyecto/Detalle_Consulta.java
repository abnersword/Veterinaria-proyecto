/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto;

/**
 *
 * @author rivch
 */
public class Detalle_Consulta {
    

    private int id_detalle;
    private int id_consulta;
    private String tratamiento;
    private String medicamento;
    private String observaciones;

    public Detalle_Consulta(int id_detalle, int id_consulta,
                            String tratamiento, String medicamento,
                            String observaciones) {
        this.id_detalle = id_detalle;
        this.id_consulta = id_consulta;
        this.tratamiento = tratamiento;
        this.medicamento = medicamento;
        this.observaciones = observaciones;
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

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    
}
