/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.DAO;
import com.mycompany.veterinaria.proyecto.Clases.Conexion;
import com.mycompany.veterinaria.proyecto.Clases.Dueño;
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
    public void guardar(Dueño dueno) throws SQLException {

    String sql = """
        INSERT INTO dueno (nombre, direccion, telefono)
        VALUES (?, ?, ?)
        """;

    try (Connection conexion = Conexion.conectar();
         PreparedStatement ps = conexion.prepareStatement(
                 sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, dueno.getNombre());
        ps.setString(2, dueno.getDireccion());
        ps.setString(3, dueno.getTelefono());

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                dueno.setId_dueño(rs.getInt(1));
                }
            }
        }
    }
    
    
}
