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
public class MascotaDAO {
    public void guardar(Mascota mascota)
            throws SQLException {

        String sql = """
            INSERT INTO Mascota (Nombre, Especie, Raza, Sexo, Fecha_nacimiento)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setString(1, mascota.getNombre());
            consulta.setString(2, mascota.getEspecie());
            consulta.setString(3, mascota.getRaza());
            consulta.setString(4, mascota.getSexo());
            consulta.setString(5, mascota.getFecha_nacimiento());

            consulta.executeUpdate();

            // Recupera el ID que MySQL generó automáticamente.
            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    mascota.setID_mascota(resultado.getInt(1));
                }
            }
        }
    }
}
