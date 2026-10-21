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
public class VeterinarioDAO {
    public void guardar(Veterinario veterinario)
            throws SQLException {

        String sql = """
            INSERT INTO Veterinario (Nombre, Telefono, Especialidad)
            VALUES (?, ?, ?)
            """;

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setString(1, veterinario.getNombre());
            consulta.setInt(2, veterinario.getTelefono());
            consulta.setString(3, veterinario.getEspecialidad());
            

            consulta.executeUpdate();

            // Recupera el ID que MySQL generó automáticamente.
            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    veterinario.setID_veterinario(resultado.getInt(1));
                }
            }
        }
    }
}
