package com.mycompany.veterinaria.proyecto.Vistas;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.mycompany.veterinaria.proyecto.Clases.Cita;
import com.mycompany.veterinaria.proyecto.DAO.CitaDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Pantallas visuales. No escribe en la base de datos hasta integrar los DAO. */
public class PantallasVeterinaria {
    private static final Color FONDO = new Color(12,17,35);
    private static final Color PANEL = new Color(28,35,61);
    private static final Color MORADO = new Color(135,83,218);
    private static final Color TEXTO = new Color(242,239,255);
    private static final Color CAMPO = new Color(21,28,48);

    private static final Map<String,String[]> CAMPOS = new LinkedHashMap<>();
    static {
        CAMPOS.put("Mascotas", new String[]{"ID mascota", "Nombre", "Especie", "Raza", "Sexo", "Fecha de nacimiento", "ID dueño"});
        CAMPOS.put("Dueños", new String[]{"ID dueño", "Nombre", "Teléfono", "Dirección"});
        CAMPOS.put("Citas", new String[]{"ID cita", "Número de cita", "ID mascota", "Fecha (AAAA-MM-DD)", "Hora (HH:MM)", "Estado", "ID veterinario"});
        CAMPOS.put("Veterinarios", new String[]{"ID veterinario", "Nombre", "Teléfono", "Especialidad"});
        CAMPOS.put("Consultas", new String[]{"ID consulta", "Número de consulta", "ID cita", "Fecha (AAAA-MM-DD)", "Estado", "Motivo", "Tratamiento", "Dosis"});
        CAMPOS.put("Productos / Servicios", new String[]{"ID producto", "Nombre", "Tipo", "Precio", "Descripción", "Stock", "ID categoría"});
        CAMPOS.put("Ventas", new String[]{"ID venta", "ID dueño", "Fecha (AAAA-MM-DD)", "Total"});
    }

    public static JPanel crear(String seccion) {
        JPanel pagina = new JPanel(new BorderLayout(0,18));
        pagina.setBackground(FONDO);
        pagina.setBorder(new EmptyBorder(25,25,25,25));
        JLabel encabezado = new JLabel(seccion);
        encabezado.setFont(new Font("Segoe UI",Font.BOLD,27));
        encabezado.setForeground(TEXTO);
        pagina.add(encabezado,BorderLayout.NORTH);
        if (CAMPOS.containsKey(seccion)) {
            pagina.add(crearFormulario(seccion,CAMPOS.get(seccion)),BorderLayout.CENTER);
        } else if (seccion.equals("Reportes")) {
    pagina.add(crearReportes(), BorderLayout.CENTER);
} else if (seccion.equals("Configuración")) {
    pagina.add(crearConfiguracion(), BorderLayout.CENTER);
} else {
    JPanel centro = panel();
    centro.add(new JLabel("Sección no disponible"));
    pagina.add(centro, BorderLayout.CENTER);
}
        return pagina;
    }
    
private static void configurarCitas(
        Map<String, JTextField> entradas,
        JButton guardar,
        JButton buscar,
        JTable tabla,
        DefaultTableModel modelo,
        JLabel aviso) {

    JTextField txtId = entradas.get("ID cita");
    JTextField txtNumero = entradas.get("Número de cita");
    JTextField txtMascota = entradas.get("ID mascota");
    JTextField txtFecha = entradas.get("Fecha (AAAA-MM-DD)");
    JTextField txtHora = entradas.get("Hora (HH:MM)");
    JTextField txtEstado = entradas.get("Estado");
    JTextField txtVeterinario = entradas.get("ID veterinario");

    // El ID de la cita lo genera MySQL
    txtId.setEditable(false);

    // Mostrar las citas existentes al abrir el formulario
    cargarCitas(modelo, aviso);

    // BOTON GUARDAR
    guardar.addActionListener(e -> {

        try {
            int numero = Integer.parseInt(
                    txtNumero.getText().trim());

            int mascota = Integer.parseInt(
                    txtMascota.getText().trim());

            int veterinario = Integer.parseInt(
                    txtVeterinario.getText().trim());

            String fecha = txtFecha.getText().trim();
            String hora = txtHora.getText().trim();
            String estado = txtEstado.getText().trim();

            // Validar fecha y hora
            LocalDate.parse(fecha);
            LocalTime.parse(hora);

            if (numero <= 0 || mascota <= 0 ||
                    veterinario <= 0 || estado.isEmpty()) {

                JOptionPane.showMessageDialog(tabla,
                        "Complete correctamente todos los campos.");
                return;
            }

            // Crear el objeto utilizando tu constructor actual
            Cita cita = new Cita(
                    0,
                    numero,
                    mascota,
                    veterinario,
                    fecha,
                    hora,
                    estado
            );

            // Llamar al metodo que ya tienes en Cita.java
            cita.RegistrarCita();

            JOptionPane.showMessageDialog(tabla,
                    "Cita registrada correctamente. ID: "
                    + cita.getID_cita());

            // Limpiar campos
            entradas.values().forEach(c -> c.setText(""));

            // Actualizar la tabla
            cargarCitas(modelo, aviso);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Numero de cita, ID mascota e ID veterinario "
                    + "deben ser numeros enteros.");

        } catch (DateTimeParseException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Fecha: AAAA-MM-DD. Hora: HH:MM.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al guardar la cita: "
                    + ex.getMessage());
        }
    });

    // BOTON BUSCAR
    buscar.addActionListener(e -> {

        String valor = JOptionPane.showInputDialog(
                tabla, "Ingrese el ID de la cita:");

        if (valor == null) {
            return;
        }

        try {
            int id = Integer.parseInt(valor.trim());

            Cita cita = Cita.buscarCita(id);

            if (cita == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se encontro la cita.");
                return;
            }

            mostrarDatosCita(cita, entradas);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID numerico valido.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al buscar: " + ex.getMessage());
        }
    });

    // MOSTRAR LOS DATOS AL SELECCIONAR UNA FILA
    tabla.getSelectionModel().addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()
                && tabla.getSelectedRow() >= 0) {

            int fila = tabla.convertRowIndexToModel(
                    tabla.getSelectedRow());

            txtId.setText(modelo.getValueAt(fila, 0).toString());
            txtNumero.setText(modelo.getValueAt(fila, 1).toString());
            txtMascota.setText(modelo.getValueAt(fila, 2).toString());
            txtFecha.setText(modelo.getValueAt(fila, 3).toString());
            txtHora.setText(modelo.getValueAt(fila, 4).toString());
            txtEstado.setText(modelo.getValueAt(fila, 5).toString());
            txtVeterinario.setText(
                    modelo.getValueAt(fila, 6).toString());
        }
    });
}

private static void cargarCitas(
        DefaultTableModel modelo,
        JLabel aviso) {

    try {

        // Limpiar los registros actuales
        modelo.setRowCount(0);

        // Utilizar el metodo existente de Cita.java
        for (Cita cita : Cita.listarCitas()) {

            String hora = cita.getHoraCita();

            if (hora != null && hora.length() >= 5) {
                hora = hora.substring(0, 5);
            }

            modelo.addRow(new Object[]{
                cita.getID_cita(),
                cita.getNumeroCita(),
                cita.getID_mascota(),
                cita.getFechaCita(),
                hora,
                cita.getEstado(),
                cita.getID_veterinario()
            });
        }

        aviso.setText(
                "Total de citas registradas: "
                + modelo.getRowCount());

    } catch (SQLException ex) {

        aviso.setText("Error al cargar las citas.");

        JOptionPane.showMessageDialog(null,
                "Error de MySQL: " + ex.getMessage());
    }
}

private static void mostrarDatosCita(
        Cita cita,
        Map<String, JTextField> entradas) {

    entradas.get("ID cita").setText(
            String.valueOf(cita.getID_cita()));

    entradas.get("Número de cita").setText(
            String.valueOf(cita.getNumeroCita()));

    entradas.get("ID mascota").setText(
            String.valueOf(cita.getID_mascota()));

    entradas.get("Fecha (AAAA-MM-DD)").setText(
            cita.getFechaCita());

    String hora = cita.getHoraCita();

    if (hora != null && hora.length() >= 5) {
        hora = hora.substring(0, 5);
    }

    entradas.get("Hora (HH:MM)").setText(hora);

    entradas.get("Estado").setText(cita.getEstado());

    entradas.get("ID veterinario").setText(
            String.valueOf(cita.getID_veterinario()));
}


    private static JPanel crearFormulario(String seccion,String[] campos) {
        JPanel contenedor = new JPanel(new BorderLayout(0,16));
        contenedor.setOpaque(false);
        JPanel formulario = panel();
        formulario.setLayout(new BorderLayout(0,15));
        JLabel subtitulo = new JLabel("Datos de " + seccion.toLowerCase());
        subtitulo.setForeground(TEXTO);
        subtitulo.setFont(new Font("Segoe UI",Font.BOLD,19));
        formulario.add(subtitulo,BorderLayout.NORTH);
        JPanel grilla = new JPanel(new GridLayout(0,2,14,12));
        grilla.setOpaque(false);
        Map<String,JTextField> entradas = new LinkedHashMap<>();
        for (String campo : campos) {
            JPanel celda = new JPanel(new BorderLayout(0,5));
            celda.setOpaque(false);
            JLabel etiqueta = new JLabel(campo);
            etiqueta.setForeground(new Color(205,207,225));
            JTextField entrada = new JTextField();
            entrada.setFont(new Font("Segoe UI",Font.PLAIN,14));
            entrada.setForeground(TEXTO);
            entrada.setBackground(CAMPO);
            entrada.setCaretColor(TEXTO);
            entrada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(79,87,124)),new EmptyBorder(9,10,9,10)));
            celda.add(etiqueta,BorderLayout.NORTH);
            celda.add(entrada,BorderLayout.CENTER);
            grilla.add(celda);
            entradas.put(campo,entrada);
        }
        formulario.add(grilla,BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        acciones.setOpaque(false);
        JButton nuevo = boton("Limpiar campos",MORADO);
        nuevo.addActionListener(e -> entradas.values().forEach(c -> c.setText("")));
        JButton guardar = boton("Guardar",new Color(40,107,163));
        
        JButton buscar = boton("Buscar",new Color(65,75,113));
        
        acciones.add(nuevo); acciones.add(guardar); acciones.add(buscar);
        formulario.add(acciones,BorderLayout.SOUTH);
        contenedor.add(formulario,BorderLayout.NORTH);
        
        JPanel listado = panel();
        listado.setLayout(new BorderLayout(0,10));
        JLabel tituloTabla = new JLabel("Registros de " + seccion.toLowerCase());
        tituloTabla.setForeground(TEXTO);
        tituloTabla.setFont(new Font("Segoe UI",Font.BOLD,18));
        listado.add(tituloTabla,BorderLayout.NORTH);
        DefaultTableModel modelo = new DefaultTableModel(campos,0) {
            @Override public boolean isCellEditable(int fila,int columna) {return false;}
        };
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setBackground(CAMPO);
        tabla.setForeground(TEXTO);
        tabla.setGridColor(new Color(60,67,98));
        tabla.setSelectionBackground(MORADO);
        tabla.getTableHeader().setBackground(new Color(88,62,150));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i=0;i<tabla.getColumnCount();i++) tabla.getColumnModel().getColumn(i).setPreferredWidth(145);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(CAMPO);
        listado.add(scroll,BorderLayout.CENTER);
        JLabel aviso = new JLabel("Sin registros cargados: conecta los DAO para consultar los datos reales.");
        aviso.setForeground(new Color(190,194,216));
        listado.add(aviso,BorderLayout.SOUTH);
        contenedor.add(listado,BorderLayout.CENTER);
        if (seccion.equals("Citas")) {

        configurarCitas(
            entradas, guardar, buscar,
            tabla, modelo, aviso
        );
        } else {
        guardar.addActionListener(e ->
        JOptionPane.showMessageDialog(
                formulario,
                "Integración pendiente para " + seccion));
        buscar.addActionListener(e ->
        JOptionPane.showMessageDialog(
                formulario,
                "Búsqueda pendiente para " + seccion));
}
        return contenedor;
    }

    private static JPanel panel() {
        JPanel p = new JPanel();
        p.setBackground(PANEL);
        p.setBorder(new EmptyBorder(18,18,18,18));
        return p;
    }
    private static JButton boton(String nombre,Color color) {
        JButton b = new JButton(nombre);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(11,17,11,17));
        return b;
    }
    
private static JPanel crearReportes() {
    JPanel principal = new JPanel(new BorderLayout(15, 20));
    principal.setOpaque(false);

    JPanel tarjetas = new JPanel(new GridLayout(2, 2, 15, 15));
    tarjetas.setOpaque(false);

    String[] titulos = {
        "Mascotas registradas",
        "Citas programadas",
        "Consultas realizadas",
        "Ventas registradas"
    };

    String[] valores = {"--", "--", "--", "--"};

    for (int i = 0; i < titulos.length; i++) {
        JPanel tarjeta = panel();
        tarjeta.setLayout(new BorderLayout(8, 10));
        tarjeta.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel(titulos[i]);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titulo.setForeground(TEXTO);

        JLabel valor = new JLabel(valores[i]);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 30));
        valor.setForeground(new Color(170, 125, 255));

        tarjeta.add(titulo, BorderLayout.NORTH);
        tarjeta.add(valor, BorderLayout.CENTER);
        tarjetas.add(tarjeta);
    }

    principal.add(tarjetas, BorderLayout.NORTH);

    JPanel resumen = panel();
    resumen.setLayout(new BorderLayout(10, 15));
    resumen.setBorder(new EmptyBorder(20, 20, 20, 20));

    JLabel tituloResumen = new JLabel("Resumen general del sistema");
    tituloResumen.setForeground(TEXTO);
    tituloResumen.setFont(new Font("Segoe UI", Font.BOLD, 20));
    resumen.add(tituloResumen, BorderLayout.NORTH);

    String[] columnas = {"Categoría", "Descripción", "Total"};
    Object[][] filas = {
        {"Mascotas", "Registros de mascotas", "Pendiente"},
        {"Citas", "Citas veterinarias", "Pendiente"},
        {"Consultas", "Atenciones realizadas", "Pendiente"},
        {"Ventas", "Operaciones registradas", "Pendiente"}
    };

    javax.swing.JTable tabla = new javax.swing.JTable(filas, columnas);
    tabla.setRowHeight(32);
    tabla.setBackground(new Color(28, 35, 61));
    tabla.setForeground(TEXTO);
    tabla.setGridColor(new Color(60, 65, 90));
    tabla.getTableHeader().setBackground(new Color(70, 50, 120));
    tabla.getTableHeader().setForeground(java.awt.Color.WHITE);
    tabla.setFillsViewportHeight(true);

    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(tabla);
    scroll.getViewport().setBackground(new Color(28, 35, 61));
    resumen.add(scroll, BorderLayout.CENTER);

    JLabel aviso = new JLabel(
        "Los indicadores se actualizarán al conectar la base de datos."
    );
    aviso.setForeground(new Color(185, 185, 210));
    resumen.add(aviso, BorderLayout.SOUTH);

    principal.add(resumen, BorderLayout.CENTER);
    return principal;
}

private static JPanel crearConfiguracion() {
    JPanel principal = new JPanel(new BorderLayout(15, 20));
    principal.setOpaque(false);

    JPanel formulario = panel();
    formulario.setLayout(new GridLayout(5, 2, 15, 18));
    formulario.setBorder(new EmptyBorder(25, 25, 25, 25));

    String[] etiquetas = {
        "Nombre de la veterinaria:",
        "Dirección:",
        "Teléfono:",
        "Correo electrónico:",
        "Responsable:"
    };

    for (String etiqueta : etiquetas) {
        JLabel label = new JLabel(etiqueta);
        label.setForeground(TEXTO);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        javax.swing.JTextField campo = new javax.swing.JTextField();
        campo.setBackground(new Color(38, 45, 75));
        campo.setForeground(java.awt.Color.WHITE);
        campo.setCaretColor(java.awt.Color.WHITE);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        formulario.add(label);
        formulario.add(campo);
    }

    principal.add(formulario, BorderLayout.NORTH);

    JPanel opciones = panel();
    opciones.setLayout(new GridLayout(4, 1, 10, 12));
    opciones.setBorder(new EmptyBorder(25, 25, 25, 25));

    JLabel titulo = new JLabel("Preferencias del sistema");
    titulo.setForeground(TEXTO);
    titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

    javax.swing.JCheckBox tema = new javax.swing.JCheckBox(
        "Utilizar tema oscuro", true
    );
    tema.setForeground(TEXTO);
    tema.setOpaque(false);
    tema.setEnabled(false);

    JLabel version = new JLabel("Sistema de Gestión Veterinaria - Versión 1.0");
    version.setForeground(TEXTO);

    JLabel aviso = new JLabel(
        "La edición y el guardado estarán disponibles al integrar el sistema."
    );
    aviso.setForeground(new Color(185, 185, 210));

    opciones.add(titulo);
    opciones.add(tema);
    opciones.add(version);
    opciones.add(aviso);

    principal.add(opciones, BorderLayout.CENTER);
    return principal;
}

}
