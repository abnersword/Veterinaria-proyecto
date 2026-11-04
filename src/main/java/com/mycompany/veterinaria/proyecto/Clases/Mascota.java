/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.veterinaria.proyecto.Clases;

import com.mycompany.veterinaria.proyecto.DAO.MascotaDAO;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author rivch
 */
public class Mascota {
    private int ID_mascota;
    private int ID_dueno;
    private String Nombre;
    private String Especie;
    private String Raza;
    private String Sexo;
    private String Fecha_nacimiento;
    
    public Mascota (int ID_mascota,int ID_dueno, String Nombre, String Especie, 
            String Raza, String Sexo, String Fecha_nacimiento){
        this.ID_mascota = ID_mascota;
        this.ID_dueno = ID_dueno;
        this.Nombre = Nombre;
        this.Especie = Especie;
        this.Raza = Raza;
        this.Sexo = Sexo;
        this.Fecha_nacimiento = Fecha_nacimiento;
    }
    

    

    
    public void consultarMascota(){
        System.out.println("El ID de la mascota es: " + getID_mascota());
        System.out.println("El nombre de la mascota es: " + getNombre());
        System.out.println("La especie de la mascota es: " + getEspecie());
        System.out.println("La raza de la mascota es: " + getRaza());
        System.out.println("El sexo de la mascota es: " + getSexo());
        System.out.println("La fecha de nacimiento de la mascota es: " + getFecha_nacimiento());
        
    }
    public void registrarMascota() throws SQLException {
    new MascotaDAO().guardar(this);
}

public void actualizarDatos() throws SQLException {
    new MascotaDAO().actualizar(this);
}

public void eliminarMascota() throws SQLException {
    new MascotaDAO().eliminar(this.ID_mascota);
}

public static Mascota buscarMascota(int id) throws SQLException {
    return new MascotaDAO().buscarPorId(id);
}

public static List<Mascota> listarMascotas() throws SQLException {
    return new MascotaDAO().listar();
}

    /**
     * @return the ID_mascota
     */
    public int getID_mascota() {
        return ID_mascota;
    }

    /**
     * @param ID_mascota the ID_mascota to set
     */
    public void setID_mascota(int ID_mascota) {
        this.ID_mascota = ID_mascota;
    }
    public int getID_dueno(){
        return ID_dueno;
    }
    
    /**
     * @return the Nombre
     */
    public String getNombre() {
        return Nombre;
    }

    /**
     * @param Nombre the Nombre to set
     */
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    /**
     * @return the Especie
     */
    public String getEspecie() {
        return Especie;
    }

    /**
     * @param Especie the Especie to set
     */
    public void setEspecie(String Especie) {
        this.Especie = Especie;
    }

    /**
     * @return the Raza
     */
    public String getRaza() {
        return Raza;
    }

    /**
     * @param Raza the Raza to set
     */
    public void setRaza(String Raza) {
        this.Raza = Raza;
    }

    /**
     * @return the Sexo
     */
    public String getSexo() {
        return Sexo;
    }

    /**
     * @param Sexo the Sexo to set
     */
    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    /**
     * @return the Fecha_nacimiento
     */
    public String getFecha_nacimiento() {
        return Fecha_nacimiento;
    }

    /**
     * @param Fecha_nacimiento the Fecha_nacimiento to set
     */
    public void setFecha_nacimiento(String Fecha_nacimiento) {
        this.Fecha_nacimiento = Fecha_nacimiento;
    }
}
