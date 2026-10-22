/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto;

/**
 *
 * @author rivch
 */
public class Consulta {
    

    private int id_consulta;
    private String fecha;
    private String hora;
    private String motivo;
    private String diagnostico;

    public Consulta(int id_consulta, String fecha, String hora,
                    String motivo, String diagnostico) {
        this.id_consulta = id_consulta;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
    }

    public void registrarConsulta()
    {

    }

    public void actualizarConsulta()
    {

    }

    public void consultarConsulta()
    {

    }
    
    // Getters y Setters

    public int getId_consulta() {
        return id_consulta;
    }

    public void setId_consulta(int id_consulta) {
        this.id_consulta = id_consulta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }


    
}
