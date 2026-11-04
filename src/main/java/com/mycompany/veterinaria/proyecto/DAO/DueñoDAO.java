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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author USER
 */ 
public class DueñoDAO {

    private Dueño leerDueño(ResultSet resultado)
            throws SQLException {

        // Orden del constructor de tu clase:
        // teléfono, nombre, dirección, identificador.
        return new Dueño(
                resultado.getString("telefono"),
                resultado.getString("nombre"),
                resultado.getString("direccion"),
                resultado.getInt("id_dueno")
        );
    }

    private void asignarDatos(
            PreparedStatement consulta, Dueño dueno)
            throws SQLException {

        consulta.setString(1, dueno.getNombre());
        consulta.setString(2, dueno.getTelefono());
        consulta.setString(3, dueno.getDireccion());
    }

    public void guardar(Dueño dueno) throws SQLException {

        String sql = "INSERT INTO dueno "
                + "(nombre, telefono, direccion) VALUES (?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            asignarDatos(consulta, dueno);
            consulta.executeUpdate();

            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    dueno.setId_dueño(resultado.getInt(1));
                }
            }
        }
    }

    public List<Dueño> listar() throws SQLException {

        List<Dueño> duenos = new ArrayList<>();

        String sql = "SELECT * FROM dueno ORDER BY nombre";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                duenos.add(leerDueño(resultado));
            }
        }

        return duenos;
    }

    public Dueño buscarPorId(int idDueno) throws SQLException {

        String sql = "SELECT * FROM dueno WHERE id_dueno = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idDueno);

            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leerDueño(resultado) : null;
            }
        }
    }

    public void actualizar(Dueño dueno) throws SQLException {

        String sql = "UPDATE dueno SET "
                + "nombre = ?, telefono = ?, direccion = ? "
                + "WHERE id_dueno = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            asignarDatos(consulta, dueno);
            consulta.setInt(4, dueno.getId_dueño());

            int filas = consulta.executeUpdate();

            if (filas == 0 && buscarPorId(dueno.getId_dueño()) == null) {
                throw new SQLException("El dueño no existe");
            }
        }
    }

    public void eliminar(int idDueno) throws SQLException {

        String sql = "DELETE FROM dueno WHERE id_dueno = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idDueno);

            if (consulta.executeUpdate() == 0) {
                throw new SQLException("El dueño no existe");
            }
        }
    }
}
