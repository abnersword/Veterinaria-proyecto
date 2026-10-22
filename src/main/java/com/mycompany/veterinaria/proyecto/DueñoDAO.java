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
 * @author USER
 */ 
public class DueñoDAO {
    public void guardar(Dueño Dueño)
            throws SQLException {
        
    
    String sql = """
            INSERT INTO dueño (id_dueño, nombre, direccion, telefono)
            VALUES (?, ?, ?, ?)
            """;
    try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setInt(1, Dueño.getId_dueño());
            consulta.setString(2, Dueño.getNombre());
            consulta.setString(3, Dueño.getDireccion());
            consulta.setString(4, Dueño.getTelefono());

            consulta.executeUpdate();
    
    try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    Dueño.setId_dueño(resultado.getInt(1));
                }
            }
        }
    }
    
    
}
