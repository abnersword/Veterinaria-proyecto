/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.DAO;
import com.mycompany.veterinaria.proyecto.Clases.Conexion;
import com.mycompany.veterinaria.proyecto.Clases.Veterinario;
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
public class VeterinarioDAO {

    private Veterinario leerVeterinario(ResultSet resultado)
            throws SQLException {

        return new Veterinario(
                resultado.getInt("id_veterinario"),
                resultado.getString("nombre"),
                resultado.getString("telefono"),
                resultado.getString("especialidad")
        );
    }

    private void asignarDatos(
            PreparedStatement consulta, Veterinario veterinario)
            throws SQLException {

        consulta.setString(1, veterinario.getNombre());
        consulta.setString(2, veterinario.getTelefono());
        consulta.setString(3, veterinario.getEspecialidad());
    }

    public void guardar(Veterinario veterinario)
            throws SQLException {

        String sql = "INSERT INTO veterinario "
                + "(nombre, telefono, especialidad) VALUES (?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            asignarDatos(consulta, veterinario);
            consulta.executeUpdate();

            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    veterinario.setID_veterinario(resultado.getInt(1));
                }
            }
        }
    }

    public List<Veterinario> listar() throws SQLException {

        List<Veterinario> veterinarios = new ArrayList<>();

        String sql = "SELECT * FROM veterinario ORDER BY nombre";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                veterinarios.add(leerVeterinario(resultado));
            }
        }

        return veterinarios;
    }

    public Veterinario buscarPorId(int idVeterinario)
            throws SQLException {

        String sql = "SELECT * FROM veterinario "
                + "WHERE id_veterinario = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idVeterinario);

            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next()
                        ? leerVeterinario(resultado) : null;
            }
        }
    }

    public void actualizar(Veterinario veterinario)
            throws SQLException {

        String sql = "UPDATE veterinario SET "
                + "nombre = ?, telefono = ?, especialidad = ? "
                + "WHERE id_veterinario = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            asignarDatos(consulta, veterinario);
            consulta.setInt(4, veterinario.getID_veterinario());

            int filas = consulta.executeUpdate();

            if (filas == 0
                    && buscarPorId(veterinario.getID_veterinario()) == null) {

                throw new SQLException("El veterinario no existe");
            }
        }
    }

    public void eliminar(int idVeterinario) throws SQLException {

        String sql = "DELETE FROM veterinario "
                + "WHERE id_veterinario = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idVeterinario);

            if (consulta.executeUpdate() == 0) {
                throw new SQLException("El veterinario no existe");
            }
        }
    }
}