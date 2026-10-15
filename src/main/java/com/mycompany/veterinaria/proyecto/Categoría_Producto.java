/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.veterinaria.proyecto;

/**
 *
 * @author rivch
 */
import java.util.ArrayList;
import java.util.List;
public class Categoría_Producto {
    // Atributos
    private int idCategoria;
    private String nombre;
    private String descripcion;

    // Productos que pertenecen a la categoría
    private List<Producto_Servicio> productos;

    // Constructor
    public Categoría_Producto(int idCategoria, String nombre,
            String descripcion) {

        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.productos = new ArrayList<>();
    }

    public void cambiarNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    public void actualizarDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    public void agregarProducto(Producto_Servicio producto) {
        if (producto != null && !productos.contains(producto)) {
            productos.add(producto);
        }
    }

    public void eliminarProducto(Producto_Servicio producto) {
        productos.remove(producto);
    }

    public List<Producto_Servicio> obtenerProductos() {
        // Devuelve una copia de la lista
        return new ArrayList<>(productos);
    }

    // Getters
    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Setter del identificador
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
