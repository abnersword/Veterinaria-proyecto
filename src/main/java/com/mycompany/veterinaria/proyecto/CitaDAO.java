/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author rivch
 */
public class CitaDAO {
    public void guardar(Cita cita)
            throws SQLException {

        String sql = """
            INSERT INTO Cita (NumeroCita, NombreMascota, FechaCita, HoraCita, Estado )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setInt(1, cita.getNumeroCita());
            consulta.setString(2, cita.getNombreMascota());
            consulta.setString(3, cita.getFechaCita());
            consulta.setString(4, cita.getHoraCita());
            consulta.setString(5, cita.getEstado());

            consulta.executeUpdate();

            // Recupera el ID que MySQL generó automáticamente.
            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    cita.setID_cita(resultado.getInt(1));
                }
            }
        }
    }
}
