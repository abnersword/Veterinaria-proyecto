/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto;

/**
 *
 * @author rivch
 */
public class Detalle_Venta {
    private int Id_detalle;
    private String Cantidad;
    private double Precio;
    private double Subtotal;
//probando comando
    public Detalle_Venta(int Id_detalle, String Cantidad, double Precio, double Subtotal) {
        this.Id_detalle = Id_detalle;
        this.Cantidad = Cantidad;
        this.Precio = Precio;
        this.Subtotal = Subtotal;
    }
    public void AgregarDetalle(){
        //aca se agregara el detalle
    }
    public void calcularSubtotal(){
        //agregar el calculo del subtotal
    }
    private void actualizarCantidad(){
        //codigo para actualizarlo desde la base de datos
    }
    //getters y seters
    public int getId_detalle() {
        return Id_detalle;
    }
    public void setId_detalle(int Id_detalle) {
        this.Id_detalle = Id_detalle;
    }
    public String getCantidad() {
        return Cantidad;
    }
    public void setPrecio(String Cantidad) {
        this.Cantidad = Cantidad;
    }
    public double getPrecio() {
        return Precio;
    }
    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }
    public double getSubtotal() {
        return Subtotal;
    }
    public void setSubtotal(double Subtotal) {
        this.Subtotal = Subtotal;
    }
}
