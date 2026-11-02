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
    private int ID_venta;
    private int ID_producto;
    private int Cantidad;
    private double Precio;
    private double Subtotal;
//probando comando
    public Detalle_Venta(int Id_detalle, int ID_venta, int ID_producto, int Cantidad, double Precio, double Subtotal) {
        this.Id_detalle = Id_detalle;
        this.ID_venta = ID_venta;
        this.ID_producto = ID_producto;
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
    public int getID_venta(){
      return ID_venta;  
    }
    public void setID_venta(int ID_venta){
        this.ID_venta = ID_venta;
    }
    public int getID_producto(){
        return ID_producto;
    }
    public void setID_producto(int ID_producto){
        this.ID_producto = ID_producto;
    }
    public int getCantidad() {
        return Cantidad;
    }
    public void setCantidad(int Cantidad) {
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
