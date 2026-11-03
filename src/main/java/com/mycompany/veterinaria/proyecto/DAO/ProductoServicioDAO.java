/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.DAO;

/**
 *
 * @author Usuario
 */
import com.mycompany.veterinaria.proyecto.Clases.Conexion;
import com.mycompany.veterinaria.proyecto.Clases.Producto_Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoServicioDAO {

    // Validar datos antes de guardar o actualizar
    private void validar(Producto_Servicio producto) {

        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto o servicio no puede ser nulo");
        }

        if (producto.getId_categoria() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una categoria valida");
        }

        if (producto.getNombre() == null ||
                producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio");
        }

        if (!"Producto".equals(producto.getTipo()) &&
                !"Servicio".equals(producto.getTipo())) {
            throw new IllegalArgumentException(
                    "El tipo debe ser Producto o Servicio");
        }

        if (!Double.isFinite(producto.getPrecio()) ||
                producto.getPrecio() < 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser cero o mayor");
        }

        if ("Producto".equals(producto.getTipo())
                && producto.getStock() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo");
        }
    }

    // Asignar los parametros comunes del INSERT y UPDATE
    private void asignarParametros(
            PreparedStatement ps,
            Producto_Servicio producto) throws SQLException {

        ps.setInt(1, producto.getId_categoria());
        ps.setString(2, producto.getNombre());
        ps.setString(3, producto.getTipo());
        ps.setBigDecimal(4, java.math.BigDecimal.valueOf(
                producto.getPrecio()));
        ps.setString(5, producto.getDescripcion());

        // Los servicios no manejan stock en MySQL
        if ("Servicio".equals(producto.getTipo())) {
            ps.setNull(6, Types.INTEGER);
        } else {
            ps.setInt(6, producto.getStock());
        }
    }

    // Convertir una fila SQL en un objeto Java
    private Producto_Servicio convertir(ResultSet rs)
            throws SQLException {

        Producto_Servicio producto = new Producto_Servicio(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getString("tipo"),
                rs.getDouble("precio"),
                rs.getString("descripcion"),
                rs.getInt("stock")
        );

        producto.setId_categoria(rs.getInt("id_categoria"));

        return producto;
    }

    // 1. GUARDAR
    public void guardar(Producto_Servicio producto)
            throws SQLException {

        validar(producto);

        String sql =
                "INSERT INTO producto_servicio " +
                "(id_categoria, nombre, tipo, precio, " +
                "descripcion, stock) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            asignarParametros(ps, producto);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setIdProducto(rs.getInt(1));
                }
            }
        }
    }

    // 2. BUSCAR POR ID
    public Producto_Servicio buscarPorId(int idProducto)
            throws SQLException {

        String sql =
                "SELECT id_producto, id_categoria, nombre, " +
                "tipo, precio, descripcion, stock " +
                "FROM producto_servicio " +
                "WHERE id_producto = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertir(rs);
                }
            }
        }

        return null;
    }

    // 3. LISTAR TODOS
    public List<Producto_Servicio> listar()
            throws SQLException {

        List<Producto_Servicio> productos = new ArrayList<>();

        String sql =
                "SELECT id_producto, id_categoria, nombre, " +
                "tipo, precio, descripcion, stock " +
                "FROM producto_servicio " +
                "ORDER BY id_producto DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(convertir(rs));
            }
        }

        return productos;
    }

    // 4. ACTUALIZAR
    public boolean actualizar(Producto_Servicio producto)
            throws SQLException {

        validar(producto);

        String sql =
                "UPDATE producto_servicio SET " +
                "id_categoria = ?, nombre = ?, tipo = ?, " +
                "precio = ?, descripcion = ?, stock = ? " +
                "WHERE id_producto = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            asignarParametros(ps, producto);
            ps.setInt(7, producto.getIdProducto());

            return ps.executeUpdate() > 0;
        }
    }

    // 5. ELIMINAR
    public boolean eliminar(int idProducto)
            throws SQLException {

        String sql =
                "DELETE FROM producto_servicio " +
                "WHERE id_producto = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            return ps.executeUpdate() > 0;
        }
    }
}

