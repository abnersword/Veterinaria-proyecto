/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Usuario
 */
public class ConsultaDAO {

    // Registrar consulta y todos sus detalles
    public int guardar(Consulta consulta, List<Detalle_Consulta> detalles)
            throws SQLException {

        if (consulta == null || detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "La consulta debe tener al menos un detalle");
        }

        String sqlConsulta =
                "INSERT INTO consulta " +
                "(numero_consulta, id_cita, fecha_consulta, estado_consulta) " +
                "VALUES (?, ?, ?, ?)";

        String sqlDetalle =
                "INSERT INTO detalle_consulta " +
                "(id_consulta, numero_detalle, motivo_detalle, " +
                "tratamiento, dosis, tiempo_dosis) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);

            try {
                int idConsulta;

                // Guardar consulta principal
                try (PreparedStatement ps = con.prepareStatement(
                        sqlConsulta, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, consulta.getNumero_consulta());
                    ps.setInt(2, consulta.getID_cita());
                    ps.setDate(3, Date.valueOf(consulta.getFecha()));
                    ps.setString(4, consulta.getEstado_consulta());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se genero el ID de consulta");
                        }
                        idConsulta = rs.getInt(1);
                    }
                }

                // Guardar los detalles
                try (PreparedStatement ps = con.prepareStatement(
                        sqlDetalle)) {

                    for (Detalle_Consulta detalle : detalles) {

                        ps.setInt(1, idConsulta);
                        ps.setInt(2, detalle.getNumero_detalle());
                        ps.setString(3, detalle.getMotivo_detalle());
                        ps.setString(4, detalle.getTratamiento());
                        ps.setString(5, detalle.getDosis());
                        ps.setString(6, detalle.getTiempo_dosis());

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                con.commit();

                consulta.setId_consulta(idConsulta);

                for (Detalle_Consulta detalle : detalles) {
                    detalle.setId_consulta(idConsulta);
                }

                return idConsulta;

            } catch (Exception e) {
                con.rollback();

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }
                throw new SQLException("Error al guardar consulta", e);
            }
        }
    }

    // Buscar una consulta por ID
    
public Consulta buscarPorId(int idConsulta) throws SQLException {

    String sql =
            "SELECT id_consulta, numero_consulta, id_cita, " +
            "fecha_consulta, estado_consulta " +
            "FROM consulta WHERE id_consulta = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idConsulta);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                Consulta consulta = new Consulta(
                        rs.getInt("id_consulta"),
                        rs.getInt("numero_consulta"),
                        rs.getInt("id_cita"),
                        rs.getString("fecha_consulta"),
                        "",
                        "",
                        "",
                        rs.getString("estado_consulta")
                );

                return consulta;
            }
        }
    }

    return null;
}
    // Listar todas las consultas
    public List<Consulta> listar() throws SQLException {

        List<Consulta> consultas = new ArrayList<>();

        String sql =
                "SELECT id_consulta, numero_consulta, id_cita, " +
                "fecha_consulta, estado_consulta " +
                "FROM consulta ORDER BY id_consulta DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Consulta consulta = new Consulta(
                rs.getInt("id_consulta"),
                rs.getInt("numero_consulta"),
                rs.getInt("id_cita"),
                rs.getString("fecha_consulta"),
                "",
                "",
                "",
                rs.getString("estado_consulta"));

                consulta.setNumero_consulta(
                        rs.getInt("numero_consulta"));
                consulta.setID_cita(rs.getInt("id_cita"));
                consulta.setEstado_consulta(
                        rs.getString("estado_consulta"));

                consultas.add(consulta);
            }
        }

        return consultas;
    }

    // Listar detalles de una consulta
    public List<Detalle_Consulta> listarDetalles(int idConsulta)
            throws SQLException {

        List<Detalle_Consulta> detalles = new ArrayList<>();

        String sql =
                "SELECT id_detalle_consulta, id_consulta, " +
                "numero_detalle, motivo_detalle, tratamiento, " +
                "dosis, tiempo_dosis " +
                "FROM detalle_consulta WHERE id_consulta = ? " +
                "ORDER BY numero_detalle";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idConsulta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Detalle_Consulta detalle = new Detalle_Consulta(
                    rs.getInt("id_detalle_consulta"),
                    rs.getInt("id_consulta"),
                    rs.getInt("numero_detalle"),
                    rs.getString("motivo_detalle"),
                    rs.getString("tratamiento"),
                    rs.getString("dosis"),
                    rs.getString("tiempo_dosis"));

                    detalle.setNumero_detalle(
                            rs.getInt("numero_detalle"));
                    detalle.setMotivo_detalle(
                            rs.getString("motivo_detalle"));
                    detalle.setDosis(rs.getString("dosis"));
                    detalle.setTiempo_dosis(
                            rs.getString("tiempo_dosis"));

                    detalles.add(detalle);
                }
            }
        }

        return detalles;
    }

    // Modificar el estado de una consulta
    public void actualizarEstado(int idConsulta, String estado)
            throws SQLException {

        String sql =
                "UPDATE consulta SET estado_consulta = ? " +
                "WHERE id_consulta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado);
            ps.setInt(2, idConsulta);

            ps.executeUpdate();
        }
    }

    // Eliminar consulta y detalles
    public void eliminar(int idConsulta) throws SQLException {

        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM detalle_consulta " +
                        "WHERE id_consulta = ?")) {
                    ps.setInt(1, idConsulta);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM consulta WHERE id_consulta = ?")) {
                    ps.setInt(1, idConsulta);
                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
}

