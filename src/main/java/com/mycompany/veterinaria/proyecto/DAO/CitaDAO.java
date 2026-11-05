/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.DAO;
import com.mycompany.veterinaria.proyecto.Clases.Cita;
import com.mycompany.veterinaria.proyecto.Clases.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rivch
 */
public class CitaDAO {

    // Convierte una fila de MySQL en un objeto Cita.
    private Cita leerCita(ResultSet resultado) throws SQLException {

        return new Cita(
                resultado.getInt("id_cita"),
                resultado.getInt("numero_cita"),
                resultado.getInt("id_mascota"),
                resultado.getInt("id_veterinario"),
                resultado.getString("fecha_cita"),
                resultado.getString("hora_cita"),
                resultado.getString("estado")
        );
    }

    // Parámetros compartidos por INSERT y UPDATE.
    private void asignarDatos(PreparedStatement consulta, Cita cita)
            throws SQLException {

        consulta.setInt(1, cita.getNumeroCita());
        consulta.setInt(2, cita.getID_mascota());
        consulta.setInt(3, cita.getID_veterinario());

        // Fecha: yyyy-MM-dd. Hora: HH:mm:ss.
        consulta.setDate(4, Date.valueOf(cita.getFechaCita()));
        consulta.setTime(5, java.sql.Time.valueOf(java.time.LocalTime.parse(cita.getHoraCita())));

        consulta.setString(6, cita.getEstado());
    }

    public void guardar(Cita cita) throws SQLException {

        String sql = "INSERT INTO cita "
                + "(numero_cita, id_mascota, id_veterinario, "
                + "fecha_cita, hora_cita, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            asignarDatos(consulta, cita);
            consulta.executeUpdate();

            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    cita.setID_cita(resultado.getInt(1));
                }
            }
        }
    }

    public List<Cita> listar() throws SQLException {

        List<Cita> citas = new ArrayList<>();

        String sql = "SELECT * FROM cita ORDER BY fecha_cita, hora_cita";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                citas.add(leerCita(resultado));
            }
        }

        return citas;
    }

    public Cita buscarPorId(int idCita) throws SQLException {

        String sql = "SELECT * FROM cita WHERE id_cita = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idCita);

            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leerCita(resultado) : null;
            }
        }
    }

    public void actualizar(Cita cita) throws SQLException {

        String sql = "UPDATE cita SET "
                + "numero_cita = ?, id_mascota = ?, "
                + "id_veterinario = ?, fecha_cita = ?, "
                + "hora_cita = ?, estado = ? "
                + "WHERE id_cita = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            asignarDatos(consulta, cita);
            consulta.setInt(7, cita.getID_cita());

            int filas = consulta.executeUpdate();

            if (filas == 0 && buscarPorId(cita.getID_cita()) == null) {
                throw new SQLException("La cita no existe");
            }
        }
    }

    public void actualizarEstado(int idCita, String estado)
            throws SQLException {

        String sql = "UPDATE cita SET estado = ? WHERE id_cita = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setString(1, estado);
            consulta.setInt(2, idCita);

            int filas = consulta.executeUpdate();

            if (filas == 0 && buscarPorId(idCita) == null) {
                throw new SQLException("La cita no existe");
            }
        }
    }

    public void eliminar(int idCita) throws SQLException {

        String sql = "DELETE FROM cita WHERE id_cita = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idCita);

            if (consulta.executeUpdate() == 0) {
                throw new SQLException("La cita no existe");
            }
        }
    }
    
}
