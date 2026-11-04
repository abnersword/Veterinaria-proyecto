/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.Clases;
import com.mycompany.veterinaria.proyecto.DAO.VentaDAO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author rivch
 */
public class Venta {
    private int ID_venta;
    private int ID_dueno;
    private String Fecha;
    private double Total;
//probando comando
    public Venta(int ID_venta,int ID_dueno, String Fecha, double Total) {
        this.ID_venta = ID_venta;
        this.ID_dueno = ID_dueno;
        this.Fecha = Fecha;
        this.Total = Total;
    }
        // Métodos del diagrama
    public void registrarVenta() {
        // previa a el registro de venta
    }

    public double calcularTotal() {
        // solo regresa el mismo total
        return Total;
    }
    public void consultarVenta() {
        System.out.println("ID de venta: " + ID_venta);
        System.out.println("Fecha: " + Fecha);
        System.out.println("Total: Q" + Total);
    }
    public void registrarVenta(List<Detalle_Venta> detalles)
        throws SQLException {

    new VentaDAO().guardar(this, detalles);
}

public void eliminarVenta() throws SQLException {
    new VentaDAO().eliminar(this.ID_venta);
}

public static Venta buscarVenta(int id) throws SQLException {
    return new VentaDAO().buscarPorId(id);
}

public static List<Venta> listarVentas() throws SQLException {
    return new VentaDAO().listar();
}

public List<Detalle_Venta> consultarDetalles()
        throws SQLException {

    return new VentaDAO().listarDetalles(this.ID_venta);
}
    public int getID_venta() {
        return ID_venta;
    }
    public void setID_venta(int ID_venta) {
        this.ID_venta = ID_venta;
    }
    public int getID_dueno(){
        return ID_dueno;
    }
    public void setID_dueno(int ID_dueno){
        this.ID_dueno = ID_dueno;
    }
    public String getFecha() {
        return Fecha;
    }
    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }
    public double getTotal() {
        return Total;
    }
    public void setTotal(double Total) {
        this.Total = Total;
    }
}
