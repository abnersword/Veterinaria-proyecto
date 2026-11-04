/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.DAO;

/**
 *
 * @author Usuario
 */
import com.mycompany.veterinaria.proyecto.Clases.Categoría_Producto;
import com.mycompany.veterinaria.proyecto.Clases.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public void guardar(Categoría_Producto categoria)
            throws SQLException {

        String sql = """
            INSERT INTO categoria_producto (nombre, descripcion)
            VALUES (?, ?)
            """;    

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            consulta.setString(1, categoria.getNombre());
            consulta.setString(2, categoria.getDescripcion());

            consulta.executeUpdate();

            // Recupera el ID que MySQL generó automáticamente.
            try (ResultSet resultado = consulta.getGeneratedKeys()) {
                if (resultado.next()) {
                    categoria.setIdCategoria(resultado.getInt(1));
                }
            }
        }
    }
    public List<Categoría_Producto> listar() throws SQLException {

        List<Categoría_Producto> categorias = new ArrayList<>();

        String sql = "SELECT id_categoria, nombre, descripcion "
            + "FROM categoria_producto ORDER BY id_categoria";

        try (Connection conexion = Conexion.conectar();
            PreparedStatement consulta = conexion.prepareStatement(sql);
            ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {

                Categoría_Producto categoria = new Categoría_Producto(
                        resultado.getInt("id_categoria"),
                        resultado.getString("nombre"),
                        resultado.getString("descripcion"));

                categorias.add(categoria);
            }
        }

        return categorias;
    }
    public void actualizar(Categoría_Producto categoria)
        throws SQLException {

    String sql = "UPDATE categoria_producto "
            + "SET nombre = ?, descripcion = ? "
            + "WHERE id_categoria = ?";

    try (Connection conexion = Conexion.conectar();
         PreparedStatement consulta = conexion.prepareStatement(sql)) {

        consulta.setString(1, categoria.getNombre());
        consulta.setString(2, categoria.getDescripcion());
        consulta.setInt(3, categoria.getIdCategoria());

        consulta.executeUpdate();
    }
}
    public void eliminar(int idCategoria) throws SQLException {

    String sql = "DELETE FROM categoria_producto "
            + "WHERE id_categoria = ?";

    try (Connection conexion = Conexion.conectar();
         PreparedStatement consulta = conexion.prepareStatement(sql)) {

        consulta.setInt(1, idCategoria);

        int filas = consulta.executeUpdate();

        if (filas == 0) {
            throw new SQLException("La categoria ya no existe");
        }
    }
    
}
    public Categoría_Producto buscarPorId(int idCategoria)
        throws SQLException {

    String sql = "SELECT id_categoria, nombre, descripcion "
            + "FROM categoria_producto WHERE id_categoria = ?";

    try (Connection conexion = Conexion.conectar();
         PreparedStatement consulta = conexion.prepareStatement(sql)) {

        consulta.setInt(1, idCategoria);

        try (ResultSet resultado = consulta.executeQuery()) {

            if (resultado.next()) {
                return new Categoría_Producto(
                        resultado.getInt("id_categoria"),
                        resultado.getString("nombre"),
                        resultado.getString("descripcion")
                );
            }
        }
    }

    return null;
}
}
