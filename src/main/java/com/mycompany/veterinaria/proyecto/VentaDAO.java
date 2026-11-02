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
public class VentaDAO {
    // Guarda una venta junto con todos sus detalles
    public int guardar(Venta venta, List<Detalle_Venta> detalles)
            throws SQLException {

        if (venta == null || detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe contener al menos un detalle");
        }

        String sqlVenta =
                "INSERT INTO venta (id_dueno) VALUES (?)";

        String sqlDetalle =
                "INSERT INTO detalle_venta " +
                "(id_venta, id_producto, cantidad, precio_unitario) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);

            try {
                int idVenta;

                // Guardar cabecera
                try (PreparedStatement ps = con.prepareStatement(
                        sqlVenta, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, venta.getID_dueno());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "No se genero el ID de venta");
                        }
                        idVenta = rs.getInt(1);
                    }
                }

                // Guardar todos los productos
                try (PreparedStatement ps = con.prepareStatement(
                        sqlDetalle)) {

                    for (Detalle_Venta detalle : detalles) {

                        ps.setInt(1, idVenta);
                        ps.setInt(2, detalle.getID_producto());
                        ps.setInt(3, detalle.getCantidad());
                        ps.setDouble(4, detalle.getPrecio());
                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                con.commit();

                venta.setID_venta(idVenta);

                for (Detalle_Venta detalle : detalles) {
                    detalle.setID_venta(idVenta);
                }

                return idVenta;

            } catch (Exception e) {
                con.rollback();

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }
                throw new SQLException("Error al guardar venta", e);
            }
        }
    }

    // Buscar una venta por su ID
    
public Venta buscarPorId(int idVenta) throws SQLException {

    String sql =
            "SELECT v.id_venta, v.id_dueno, " +
            "v.fecha, t.total " +
            "FROM venta v " +
            "INNER JOIN vista_total_venta t " +
            "ON v.id_venta = t.id_venta " +
            "WHERE v.id_venta = ?";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idVenta);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                Venta venta = new Venta(
                        rs.getInt("id_venta"),
                        rs.getInt("id_dueno"),
                        rs.getString("fecha"),
                        rs.getDouble("total")
                );

                return venta;
            }
        }
    }

    return null;
}


    // Listar todas las ventas
    
public List<Venta> listar() throws SQLException {

    List<Venta> ventas = new ArrayList<>();

    String sql =
            "SELECT v.id_venta, v.id_dueno, " +
            "v.fecha, t.total " +
            "FROM venta v " +
            "INNER JOIN vista_total_venta t " +
            "ON v.id_venta = t.id_venta " +
            "ORDER BY v.id_venta DESC";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            Venta venta = new Venta(
                    rs.getInt("id_venta"),
                    rs.getInt("id_dueno"),
                    rs.getString("fecha"),
                    rs.getDouble("total")
            );

            ventas.add(venta);
        }
    }

    return ventas;
}


    // Consultar los productos de una venta
    public List<Detalle_Venta> listarDetalles(int idVenta)
            throws SQLException {

        List<Detalle_Venta> detalles = new ArrayList<>();

        String sql =
                "SELECT id_detalle, id_venta, id_producto, " +
                "cantidad, precio_unitario, subtotal " +
                "FROM detalle_venta WHERE id_venta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVenta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Detalle_Venta detalle = new Detalle_Venta(
                    rs.getInt("id_detalle"),
                    rs.getInt("id_venta"),
                    rs.getInt("id_producto"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("subtotal")
);

detalles.add(detalle);


                    detalle.setID_venta(rs.getInt("id_venta"));
                    detalle.setID_producto(rs.getInt("id_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));

                    detalles.add(detalle);
                }
            }
        }

        return detalles;
    }

    // Eliminar una venta y sus detalles
    public void eliminar(int idVenta) throws SQLException {

        try (Connection con = Conexion.conectar()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM detalle_venta WHERE id_venta = ?")) {
                    ps.setInt(1, idVenta);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM venta WHERE id_venta = ?")) {
                    ps.setInt(1, idVenta);
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
