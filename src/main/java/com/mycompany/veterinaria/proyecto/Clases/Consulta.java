/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.Clases;
import com.mycompany.veterinaria.proyecto.DAO.ConsultaDAO;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author rivch
 */
public class Consulta {
    

    private int id_consulta;
    private int numero_consulta;
    private int ID_cita;
    private String fecha;
    private String hora;
    private String motivo;
    private String diagnostico;
    private String estado_consulta;

    public Consulta(int id_consulta,int numero_consulta, int ID_cita, String fecha, String hora,
                    String motivo, String diagnostico, String estado_consulta) {
        this.id_consulta = id_consulta;
        this.numero_consulta = numero_consulta;
        this.ID_cita = ID_cita;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.estado_consulta = estado_consulta;
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
    public void registrarConsulta(List<Detalle_Consulta> detalles)
        throws SQLException {

    new ConsultaDAO().guardar(this, detalles);
}

public void eliminarConsulta() throws SQLException {
    new ConsultaDAO().eliminar(this.id_consulta);
}

public static Consulta buscarConsulta(int id)
        throws SQLException {

    return new ConsultaDAO().buscarPorId(id);
}

public static List<Consulta> listarConsultas()
        throws SQLException {

    return new ConsultaDAO().listar();
}

public List<Detalle_Consulta> consultarDetalles()
        throws SQLException {

    return new ConsultaDAO().listarDetalles(this.id_consulta);
}

public void actualizarEstadoConsulta(String nuevoEstado)
        throws SQLException {

    new ConsultaDAO().actualizarEstado(
            this.id_consulta, nuevoEstado);

    // Cambia el objeto solo después del éxito del DAO.
    this.estado_consulta = nuevoEstado;
}
    // Getters y Setters

    public int getId_consulta() {
        return id_consulta;
    }

    public void setId_consulta(int id_consulta) {
        this.id_consulta = id_consulta;
    }
    public int getNumero_consulta() {
        return numero_consulta;
    }

    public void setNumero_consulta(int numero_consulta) {
        this.numero_consulta = numero_consulta;
    }
    public int getID_cita() {
        return ID_cita;
    }

    public void setID_cita(int ID_cita) {
        this.ID_cita = ID_cita;
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
    public String getEstado_consulta() {
        return estado_consulta;
    }

    public void setEstado_consulta(String estado_consulta) {
        this.estado_consulta = estado_consulta;
    }

    
}
