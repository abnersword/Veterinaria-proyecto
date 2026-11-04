/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.DAO;

import com.mycompany.veterinaria.proyecto.Clases.Conexion;
import com.mycompany.veterinaria.proyecto.Clases.Mascota;
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
public class MascotaDAO {

    private Mascota leerMascota(ResultSet resultado)
            throws SQLException {

        return new Mascota(
                resultado.getInt("id_mascota"),
                resultado.getInt("id_dueno"),
                resultado.getString("nombre"),
                resultado.getString("especie"),
                resultado.getString("raza"),
                resultado.getString("sexo"),
                resultado.getString("fecha_nacimiento")
        );
    }

    private void asignarDatos(
            PreparedStatement consulta, Mascota mascota)
            throws SQLException {

        consulta.setInt(1, mascota.getID_dueno());
        consulta.setString(2, mascota.getNombre());
        consulta.setString(3, mascota.getEspecie());
        consulta.setString(4, mascota.getRaza());
        consulta.setString(5, mascota.getSexo());

        String fecha = mascota.getFecha_nacimiento();

        // La fecha de nacimiento es opcional.
        if (fecha == null || fecha.trim().isEmpty()) {
            consulta.setNull(6, Types.DATE);
        } else {
            consulta.setDate(6, Date.valueOf(fecha.trim()));
        }
    }

    public void guardar(Mascota mascota) throws SQLException {

        String sql = "INSERT INTO mascota "
                + "(id_dueno, nombre, especie, raza, sexo, "
                + "fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            asignarDatos(consulta, mascota);
            consulta.executeUpdate();

            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    mascota.setID_mascota(resultado.getInt(1));
                }
            }
        }
    }

    public List<Mascota> listar() throws SQLException {

        List<Mascota> mascotas = new ArrayList<>();

        String sql = "SELECT * FROM mascota ORDER BY nombre";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                mascotas.add(leerMascota(resultado));
            }
        }

        return mascotas;
    }

    public Mascota buscarPorId(int idMascota)
            throws SQLException {

        String sql = "SELECT * FROM mascota WHERE id_mascota = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idMascota);

            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? leerMascota(resultado) : null;
            }
        }
    }

    public void actualizar(Mascota mascota) throws SQLException {

        String sql = "UPDATE mascota SET "
                + "id_dueno = ?, nombre = ?, especie = ?, "
                + "raza = ?, sexo = ?, fecha_nacimiento = ? "
                + "WHERE id_mascota = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            asignarDatos(consulta, mascota);
            consulta.setInt(7, mascota.getID_mascota());

            int filas = consulta.executeUpdate();

            if (filas == 0
                    && buscarPorId(mascota.getID_mascota()) == null) {

                throw new SQLException("La mascota no existe");
            }
        }
    }

    public void eliminar(int idMascota) throws SQLException {

        String sql = "DELETE FROM mascota WHERE id_mascota = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {

            consulta.setInt(1, idMascota);

            if (consulta.executeUpdate() == 0) {
                throw new SQLException("La mascota no existe");
            }
        }
    }
}
