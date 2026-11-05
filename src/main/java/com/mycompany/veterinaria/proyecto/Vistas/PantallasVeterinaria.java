package com.mycompany.veterinaria.proyecto.Vistas;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.mycompany.veterinaria.proyecto.Clases.Cita;
import com.mycompany.veterinaria.proyecto.Clases.Mascota;
import com.mycompany.veterinaria.proyecto.Clases.Dueño;
import com.mycompany.veterinaria.proyecto.Clases.Veterinario;
import com.mycompany.veterinaria.proyecto.Clases.Producto_Servicio;
import com.mycompany.veterinaria.proyecto.Clases.Venta;
import com.mycompany.veterinaria.proyecto.Clases.Detalle_Venta;
import com.mycompany.veterinaria.proyecto.Clases.Consulta;
import com.mycompany.veterinaria.proyecto.Clases.Detalle_Consulta;
import com.mycompany.veterinaria.proyecto.DAO.CitaDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.veterinaria.proyecto.Clases.Mascota;
import com.mycompany.veterinaria.proyecto.Clases.Cita;
import com.mycompany.veterinaria.proyecto.Clases.Consulta;
import com.mycompany.veterinaria.proyecto.Clases.Venta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

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
    pagina.add(crearPanelReportes(), BorderLayout.CENTER);
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

private static JPanel crearPanelDetalleVenta(
        JTextField txtTotal,
        JTable tablaDetalle,
        DefaultTableModel modeloDetalle) {

    JPanel panel = new JPanel(new BorderLayout(10, 15));
    panel.setBackground(new Color(29, 37, 64));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel titulo = new JLabel("Detalle de venta");
    titulo.setFont(new Font("Arial", Font.BOLD, 20));
    titulo.setForeground(Color.WHITE);

    panel.add(titulo, BorderLayout.NORTH);

    // Campos para ingresar productos
    JPanel campos = new JPanel(new GridLayout(2, 4, 10, 8));
    campos.setOpaque(false);

    JTextField txtProducto = new JTextField();
    JTextField txtCantidad = new JTextField();
    JTextField txtPrecio = new JTextField();
    JTextField txtSubtotal = new JTextField();

    txtSubtotal.setEditable(false);

    String[] etiquetas = {
        "ID producto",
        "Cantidad",
        "Precio unitario",
        "Subtotal"
    };

    JTextField[] controles = {
        txtProducto,
        txtCantidad,
        txtPrecio,
        txtSubtotal
    };

    for (String etiqueta : etiquetas) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        campos.add(lbl);
    }

    for (JTextField control : controles) {
        control.setBackground(new Color(21, 28, 48));
        control.setForeground(Color.WHITE);
        control.setCaretColor(Color.WHITE);
        control.setBorder(BorderFactory.createLineBorder(
                new Color(70, 82, 118)));
        campos.add(control);
    }

    // Botones
    JButton btnAgregar = new JButton("Agregar producto");
    JButton btnQuitar = new JButton("Quitar producto");

    btnAgregar.setBackground(new Color(40, 108, 161));
    btnAgregar.setForeground(Color.WHITE);

    btnQuitar.setBackground(new Color(130, 70, 210));
    btnQuitar.setForeground(Color.WHITE);

    JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
    botones.setOpaque(false);
    botones.add(btnAgregar);
    botones.add(btnQuitar);

    JPanel superior = new JPanel(new BorderLayout(0, 10));
    superior.setOpaque(false);
    superior.add(campos, BorderLayout.CENTER);
    superior.add(botones, BorderLayout.SOUTH);

    // Configurar tabla
    tablaDetalle.setRowHeight(28);
    tablaDetalle.setBackground(new Color(21, 28, 48));
    tablaDetalle.setForeground(Color.WHITE);
    tablaDetalle.setGridColor(new Color(70, 82, 118));

    tablaDetalle.getTableHeader().setBackground(
            new Color(90, 65, 150));
    tablaDetalle.getTableHeader().setForeground(Color.WHITE);

    JScrollPane scroll = new JScrollPane(tablaDetalle);
    scroll.setPreferredSize(new Dimension(600, 180));

    JPanel contenido = new JPanel(new BorderLayout(0, 10));
    contenido.setOpaque(false);
    contenido.add(superior, BorderLayout.NORTH);
    contenido.add(scroll, BorderLayout.CENTER);

    panel.add(contenido, BorderLayout.CENTER);

    // Calcular total de todos los detalles
    Runnable calcularTotal = () -> {

        double total = 0;

        for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
            total += ((Number) modeloDetalle.getValueAt(i, 4))
                    .doubleValue();
        }

        txtTotal.setText(String.format(
                java.util.Locale.US, "%.2f", total));
    };

    // Agregar producto a la tabla
    btnAgregar.addActionListener(e -> {

        try {
            int idProducto = Integer.parseInt(
                    txtProducto.getText().trim());

            int cantidad = Integer.parseInt(
                    txtCantidad.getText().trim());

            Producto_Servicio producto =
            Producto_Servicio.buscarProducto(idProducto);

            if (producto == null) {
            JOptionPane.showMessageDialog(panel,
            "El producto no existe en la base de datos.");
             return;
                }

            double precio = producto.getPrecio();

            if (idProducto <= 0 || cantidad <= 0
                    || !Double.isFinite(precio) || precio < 0) {

                JOptionPane.showMessageDialog(panel,
                        "Verifique los datos del producto.");
                return;
            }
            if ("Producto".equals(producto.getTipo())
            && cantidad > producto.getStock()) {

            JOptionPane.showMessageDialog(panel,
            "Stock insuficiente. Disponible: "
            + producto.getStock());
            return;
            }

            double subtotal = cantidad * precio;

            txtSubtotal.setText(String.format(
                    java.util.Locale.US, "%.2f", subtotal));

            modeloDetalle.addRow(new Object[]{
                idProducto,
                producto.getNombre(),
                cantidad,
                precio,
                subtotal
            });

            calcularTotal.run();

            txtProducto.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            txtSubtotal.setText("");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(panel,
                    "Ingrese valores numericos validos.");
        }
        catch (SQLException ex) {
        JOptionPane.showMessageDialog(panel,
            "Error al consultar el producto: "
            + ex.getMessage());
        }
    });

    // Quitar producto seleccionado
    btnQuitar.addActionListener(e -> {

        int fila = tablaDetalle.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(panel,
                    "Seleccione un producto.");
            return;
        }

        int indice = tablaDetalle.convertRowIndexToModel(fila);
        modeloDetalle.removeRow(indice);

        calcularTotal.run();
    });

    return panel;
}

private static void configurarMascotas(
        Map<String, JTextField> entradas,
        JButton guardar,
        JButton buscar,
        JTable tabla,
        DefaultTableModel modelo,
        JLabel aviso) {

    JTextField txtId = entradas.get("ID mascota");
    JTextField txtNombre = entradas.get("Nombre");
    JTextField txtEspecie = entradas.get("Especie");
    JTextField txtRaza = entradas.get("Raza");
    JTextField txtSexo = entradas.get("Sexo");
    JTextField txtFecha = entradas.get("Fecha de nacimiento");
    JTextField txtDueno = entradas.get("ID dueño");

    // El ID de mascota se genera automaticamente
    txtId.setEditable(false);

    // Cargar los registros al abrir Mascotas
    cargarMascotas(modelo, aviso);

    // BOTON GUARDAR
    guardar.addActionListener(e -> {

        try {
            String nombre = txtNombre.getText().trim();
            String especie = txtEspecie.getText().trim();
            String raza = txtRaza.getText().trim();
            String sexo = txtSexo.getText().trim();
            String fecha = txtFecha.getText().trim();

            int idDueno = Integer.parseInt(
                    txtDueno.getText().trim());

            // Validaciones
            if (nombre.isEmpty() ||
                    especie.isEmpty() ||
                    raza.isEmpty() ||
                    sexo.isEmpty() ||
                    idDueno <= 0) {

                JOptionPane.showMessageDialog(tabla,
                        "Complete correctamente los campos.");
                return;
            }

            // La fecha es opcional
            if (!fecha.isEmpty()) {
                java.time.LocalDate.parse(fecha);
            }

            // Crear objeto con tu constructor original
            Mascota mascota = new Mascota(
                    0,
                    idDueno,
                    nombre,
                    especie,
                    raza,
                    sexo,
                    fecha
            );

            // Utilizar el metodo de Mascota.java
            mascota.registrarMascota();

            JOptionPane.showMessageDialog(tabla,
                    "Mascota registrada correctamente. ID: "
                    + mascota.getID_mascota());

            // Limpiar campos
            entradas.values().forEach(c -> c.setText(""));

            // Actualizar JTable
            cargarMascotas(modelo, aviso);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "El ID del dueño debe ser un numero entero.");

        } catch (java.time.format.DateTimeParseException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "La fecha debe tener el formato AAAA-MM-DD.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al guardar mascota: "
                    + ex.getMessage());
        }
    });

    // BOTON BUSCAR
    buscar.addActionListener(e -> {

        String valor = JOptionPane.showInputDialog(
                tabla, "Ingrese el ID de la mascota:");

        if (valor == null) {
            return;
        }

        try {
            int id = Integer.parseInt(valor.trim());

            Mascota mascota = Mascota.buscarMascota(id);

            if (mascota == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se encontro la mascota.");
                return;
            }

            mostrarDatosMascota(mascota, entradas);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID numerico valido.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al buscar mascota: "
                    + ex.getMessage());
        }
    });

    // SELECCIONAR UNA FILA DEL JTABLE
    tabla.getSelectionModel().addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()
                && tabla.getSelectedRow() >= 0) {

            int fila = tabla.convertRowIndexToModel(
                    tabla.getSelectedRow());

            txtId.setText(
                    String.valueOf(modelo.getValueAt(fila, 0)));

            txtNombre.setText(
                    String.valueOf(modelo.getValueAt(fila, 1)));

            txtEspecie.setText(
                    String.valueOf(modelo.getValueAt(fila, 2)));

            txtRaza.setText(
                    String.valueOf(modelo.getValueAt(fila, 3)));

            txtSexo.setText(
                    String.valueOf(modelo.getValueAt(fila, 4)));

            Object fechaValor = modelo.getValueAt(fila, 5);
            txtFecha.setText(
                    fechaValor == null ? "" : fechaValor.toString());

            txtDueno.setText(
                    String.valueOf(modelo.getValueAt(fila, 6)));
        }
    });
}

private static void cargarMascotas(
        DefaultTableModel modelo,
        JLabel aviso) {

    try {
        modelo.setRowCount(0);

        // Recuperar las mascotas desde MySQL
        for (Mascota mascota : Mascota.listarMascotas()) {

            modelo.addRow(new Object[]{
                mascota.getID_mascota(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getSexo(),
                mascota.getFecha_nacimiento(),
                mascota.getID_dueno()
            });
        }

        aviso.setText(
                "Total de mascotas registradas: "
                + modelo.getRowCount());

    } catch (SQLException ex) {

        aviso.setText("Error al cargar las mascotas.");

        JOptionPane.showMessageDialog(null,
                "Error de MySQL: " + ex.getMessage());
    }
}

private static void mostrarDatosMascota(
        Mascota mascota,
        Map<String, JTextField> entradas) {

    entradas.get("ID mascota").setText(
            String.valueOf(mascota.getID_mascota()));

    entradas.get("Nombre").setText(
            mascota.getNombre());

    entradas.get("Especie").setText(
            mascota.getEspecie());

    entradas.get("Raza").setText(
            mascota.getRaza());

    entradas.get("Sexo").setText(
            mascota.getSexo());

    entradas.get("Fecha de nacimiento").setText(
            mascota.getFecha_nacimiento() == null
                    ? ""
                    : mascota.getFecha_nacimiento());

    entradas.get("ID dueño").setText(
            String.valueOf(mascota.getID_dueno()));
}

private static void configurarDuenos(
        Map<String, JTextField> entradas,
        JButton guardar,
        JButton buscar,
        JTable tabla,
        DefaultTableModel modelo,
        JLabel aviso) {

    JTextField txtId = entradas.get("ID dueño");
    JTextField txtNombre = entradas.get("Nombre");
    JTextField txtTelefono = entradas.get("Teléfono");
    JTextField txtDireccion = entradas.get("Dirección");

    // El ID lo genera MySQL
    txtId.setEditable(false);

    // Cargar registros al abrir el formulario
    cargarDuenos(modelo, aviso);

    // BOTON GUARDAR
    guardar.addActionListener(e -> {

        try {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String direccion = txtDireccion.getText().trim();

            if (nombre.isEmpty() ||
                    telefono.isEmpty() ||
                    direccion.isEmpty()) {

                JOptionPane.showMessageDialog(tabla,
                        "Complete todos los campos.");
                return;
            }

            // Validacion de telefono guatemalteco
            if (!telefono.matches("[0-9]{8}")) {
                JOptionPane.showMessageDialog(tabla,
                        "El telefono debe contener 8 digitos.");
                return;
            }

            Dueño dueno = new Dueño(
                    telefono, nombre, direccion, 0
            );

            dueno.registrarDueño();

            JOptionPane.showMessageDialog(tabla,
                    "Dueño registrado correctamente.");

            entradas.values().forEach(c -> c.setText(""));
            cargarDuenos(modelo, aviso);

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al guardar dueño: "
                    + ex.getMessage());
        }
    });

    // BOTON BUSCAR
    buscar.addActionListener(e -> {

        String valor = JOptionPane.showInputDialog(
                tabla, "Ingrese el ID del dueño:");

        if (valor == null) {
            return;
        }

        try {
            int id = Integer.parseInt(valor.trim());

            Dueño dueno = Dueño.buscarDueño(id);

            if (dueno == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se encontro el dueño.");
                return;
            }

            mostrarDatosDueno(dueno, entradas);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID numerico valido.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al buscar: " + ex.getMessage());
        }
    });

    // SELECCIONAR UNA FILA DEL JTABLE
    tabla.getSelectionModel().addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()
                && tabla.getSelectedRow() >= 0) {

            int fila = tabla.convertRowIndexToModel(
                    tabla.getSelectedRow());

            txtId.setText(
                    String.valueOf(modelo.getValueAt(fila, 0)));

            txtNombre.setText(
                    String.valueOf(modelo.getValueAt(fila, 1)));

            txtTelefono.setText(
                    String.valueOf(modelo.getValueAt(fila, 2)));

            txtDireccion.setText(
                    String.valueOf(modelo.getValueAt(fila, 3)));
        }
    });
}

private static void cargarDuenos(
        DefaultTableModel modelo,
        JLabel aviso) {

    try {
        modelo.setRowCount(0);

        for (Dueño dueno : Dueño.listarDueños()) {

            modelo.addRow(new Object[]{
                dueno.getId_dueño(),
                dueno.getNombre(),
                dueno.getTelefono(),
                dueno.getDireccion()
            });
        }

        aviso.setText(
                "Total de dueños registrados: "
                + modelo.getRowCount());

    } catch (SQLException ex) {

        aviso.setText("Error al cargar dueños.");

        JOptionPane.showMessageDialog(null,
                "Error de MySQL: " + ex.getMessage());
    }
}

private static void mostrarDatosDueno(
        Dueño dueno,
        Map<String, JTextField> entradas) {

    entradas.get("ID dueño").setText(
            String.valueOf(dueno.getId_dueño()));

    entradas.get("Nombre").setText(
            dueno.getNombre());

    entradas.get("Teléfono").setText(
            dueno.getTelefono());

    entradas.get("Dirección").setText(
            dueno.getDireccion());
}

private static void configurarVeterinarios(
        Map<String, JTextField> entradas,
        JButton guardar,
        JButton buscar,
        JTable tabla,
        DefaultTableModel modelo,
        JLabel aviso) {

    JTextField txtId = entradas.get("ID veterinario");
    JTextField txtNombre = entradas.get("Nombre");
    JTextField txtTelefono = entradas.get("Teléfono");
    JTextField txtEspecialidad = entradas.get("Especialidad");

    // El ID se genera automaticamente en MySQL
    txtId.setEditable(false);

    // Cargar los registros al abrir el formulario
    cargarVeterinarios(modelo, aviso);

    // BOTON GUARDAR
    guardar.addActionListener(e -> {

        try {

            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String especialidad = txtEspecialidad.getText().trim();

            // Validar campos vacios
            if (nombre.isEmpty() ||
                telefono.isEmpty() ||
                especialidad.isEmpty()) {

                JOptionPane.showMessageDialog(tabla,
                        "Complete todos los campos.");
                return;
            }

            // Validar telefono
            if (!telefono.matches("[0-9]{8}")) {

                JOptionPane.showMessageDialog(tabla,
                        "El telefono debe contener 8 digitos.");
                return;
            }

            // Crear objeto utilizando tu constructor
            Veterinario veterinario = new Veterinario(
                    0,
                    nombre,
                    telefono,
                    especialidad
            );

            // Registrar mediante tu clase Veterinario
            veterinario.registrarVeterinario();

            JOptionPane.showMessageDialog(tabla,
                    "Veterinario registrado correctamente. ID: "
                    + veterinario.getID_veterinario());

            // Limpiar los campos
            entradas.values().forEach(c -> c.setText(""));

            // Actualizar la tabla
            cargarVeterinarios(modelo, aviso);

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al guardar veterinario: "
                    + ex.getMessage());
        }
    });

    // BOTON BUSCAR
    buscar.addActionListener(e -> {

        String valor = JOptionPane.showInputDialog(
                tabla,
                "Ingrese el ID del veterinario:");

        if (valor == null) {
            return;
        }

        try {

            int id = Integer.parseInt(valor.trim());

            // Buscar mediante tu clase Veterinario
            Veterinario veterinario =
                    Veterinario.buscarVeterinario(id);

            if (veterinario == null) {

                JOptionPane.showMessageDialog(tabla,
                        "No se encontro el veterinario.");
                return;
            }

            // Mostrar los datos encontrados
            mostrarDatosVeterinario(veterinario, entradas);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID numerico valido.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al buscar veterinario: "
                    + ex.getMessage());
        }
    });

    // SELECCIONAR UNA FILA DEL JTABLE
    tabla.getSelectionModel().addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()
                && tabla.getSelectedRow() >= 0) {

            int fila = tabla.convertRowIndexToModel(
                    tabla.getSelectedRow());

            txtId.setText(
                    String.valueOf(modelo.getValueAt(fila, 0)));

            txtNombre.setText(
                    String.valueOf(modelo.getValueAt(fila, 1)));

            txtTelefono.setText(
                    String.valueOf(modelo.getValueAt(fila, 2)));

            txtEspecialidad.setText(
                    String.valueOf(modelo.getValueAt(fila, 3)));
        }
    });
}

private static void cargarVeterinarios(
        DefaultTableModel modelo,
        JLabel aviso) {

    try {

        // Limpiar registros anteriores
        modelo.setRowCount(0);

        // Obtener veterinarios desde MySQL
        for (Veterinario veterinario :
                Veterinario.listarVeterinarios()) {

            modelo.addRow(new Object[]{
                veterinario.getID_veterinario(),
                veterinario.getNombre(),
                veterinario.getTelefono(),
                veterinario.getEspecialidad()
            });
        }

        aviso.setText(
                "Total de veterinarios registrados: "
                + modelo.getRowCount());

    } catch (SQLException ex) {

        aviso.setText("Error al cargar veterinarios.");

        JOptionPane.showMessageDialog(null,
                "Error de MySQL: " + ex.getMessage());
    }
}

private static void mostrarDatosVeterinario(
        Veterinario veterinario,
        Map<String, JTextField> entradas) {

    entradas.get("ID veterinario").setText(
            String.valueOf(
                    veterinario.getID_veterinario()));

    entradas.get("Nombre").setText(
            veterinario.getNombre());

    entradas.get("Teléfono").setText(
            veterinario.getTelefono());

    entradas.get("Especialidad").setText(
            veterinario.getEspecialidad());
}

private static void configurarProductosServicios(
        Map<String, JTextField> entradas,
        JButton guardar,
        JButton buscar,
        JTable tabla,
        DefaultTableModel modelo,
        JLabel aviso) {

    JTextField txtId = entradas.get("ID producto");
    JTextField txtNombre = entradas.get("Nombre");
    JTextField txtTipo = entradas.get("Tipo");
    JTextField txtPrecio = entradas.get("Precio");
    JTextField txtDescripcion = entradas.get("Descripción");
    JTextField txtStock = entradas.get("Stock");
    JTextField txtCategoria = entradas.get("ID categoría");

    // El ID lo genera MySQL
    txtId.setEditable(false);

    // Cargar los productos al abrir el formulario
    cargarProductosServicios(modelo, aviso);

    // BOTON GUARDAR
    guardar.addActionListener(e -> {

        try {

            String nombre = txtNombre.getText().trim();
            String tipo = txtTipo.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            double precio = Double.parseDouble(
                    txtPrecio.getText().trim());

            int categoria = Integer.parseInt(
                    txtCategoria.getText().trim());

            // Validar los campos
            if (nombre.isEmpty() ||
                    tipo.isEmpty() ||
                    categoria <= 0 ||
                    !Double.isFinite(precio) ||
                    precio < 0) {

                JOptionPane.showMessageDialog(tabla,
                        "Complete correctamente los campos.");
                return;
            }

            // El DAO solamente permite Producto o Servicio
            if (!tipo.equals("Producto") &&
                    !tipo.equals("Servicio")) {

                JOptionPane.showMessageDialog(tabla,
                        "El tipo debe ser Producto o Servicio.");
                return;
            }

            int stock;

            if (tipo.equals("Servicio")) {

                // Los servicios no manejan stock
                stock = 0;

            } else {

                stock = Integer.parseInt(
                        txtStock.getText().trim());

                if (stock < 0) {
                    JOptionPane.showMessageDialog(tabla,
                            "El stock no puede ser negativo.");
                    return;
                }
            }

            // Crear objeto utilizando tu constructor original
            Producto_Servicio producto = new Producto_Servicio(
                    0,
                    nombre,
                    tipo,
                    precio,
                    descripcion,
                    stock
            );

            // Asignar categoria mediante tu setter
            producto.setId_categoria(categoria);

            // Guardar mediante tu clase Producto_Servicio
            producto.registrarProducto();

            JOptionPane.showMessageDialog(tabla,
                    "Registro guardado correctamente. ID: "
                    + producto.getIdProducto());

            // Limpiar los campos
            entradas.values().forEach(c -> c.setText(""));

            // Actualizar el JTable
            cargarProductosServicios(modelo, aviso);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Precio, stock e ID categoria "
                    + "deben contener valores numericos.");

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Datos invalidos: " + ex.getMessage());

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al guardar: " + ex.getMessage());
        }
    });

    // BOTON BUSCAR
    buscar.addActionListener(e -> {

        String valor = JOptionPane.showInputDialog(
                tabla, "Ingrese el ID del producto o servicio:");

        if (valor == null) {
            return;
        }

        try {

            int id = Integer.parseInt(valor.trim());

            Producto_Servicio producto =
                    Producto_Servicio.buscarProducto(id);

            if (producto == null) {

                JOptionPane.showMessageDialog(tabla,
                        "No se encontro el registro.");
                return;
            }

            mostrarDatosProductoServicio(producto, entradas);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID numerico valido.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al buscar: " + ex.getMessage());
        }
    });

    // SELECCIONAR UNA FILA DEL JTABLE
    tabla.getSelectionModel().addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()
                && tabla.getSelectedRow() >= 0) {

            int fila = tabla.convertRowIndexToModel(
                    tabla.getSelectedRow());

            txtId.setText(
                    String.valueOf(modelo.getValueAt(fila, 0)));

            txtNombre.setText(
                    String.valueOf(modelo.getValueAt(fila, 1)));

            txtTipo.setText(
                    String.valueOf(modelo.getValueAt(fila, 2)));

            txtPrecio.setText(
                    String.valueOf(modelo.getValueAt(fila, 3)));

            Object descripcion = modelo.getValueAt(fila, 4);
            txtDescripcion.setText(
                    descripcion == null ? "" : descripcion.toString());

            Object stock = modelo.getValueAt(fila, 5);
            txtStock.setText(
                    stock == null ? "" : stock.toString());

            txtCategoria.setText(
                    String.valueOf(modelo.getValueAt(fila, 6)));
        }
    });
}

private static void cargarProductosServicios(
        DefaultTableModel modelo,
        JLabel aviso) {

    try {

        // Limpiar los registros actuales
        modelo.setRowCount(0);

        // Obtener los productos y servicios de MySQL
        for (Producto_Servicio producto :
                Producto_Servicio.listarProductos()) {

            modelo.addRow(new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getTipo(),
                producto.getPrecio(),
                producto.getDescripcion(),

                // Los servicios no manejan stock
                producto.getTipo().equals("Servicio")
                        ? null
                        : producto.getStock(),

                producto.getId_categoria()
            });
        }

        aviso.setText(
                "Total de productos y servicios: "
                + modelo.getRowCount());

    } catch (SQLException ex) {

        aviso.setText("Error al cargar los registros.");

        JOptionPane.showMessageDialog(null,
                "Error de MySQL: " + ex.getMessage());
    }
}

private static void mostrarDatosProductoServicio(
        Producto_Servicio producto,
        Map<String, JTextField> entradas) {

    entradas.get("ID producto").setText(
            String.valueOf(producto.getIdProducto()));

    entradas.get("Nombre").setText(
            producto.getNombre());

    entradas.get("Tipo").setText(
            producto.getTipo());

    entradas.get("Precio").setText(
            String.valueOf(producto.getPrecio()));

    entradas.get("Descripción").setText(
            producto.getDescripcion() == null
                    ? ""
                    : producto.getDescripcion());

    // Los servicios no manejan stock
    if (producto.getTipo().equals("Servicio")) {

        entradas.get("Stock").setText("");

    } else {

        entradas.get("Stock").setText(
                String.valueOf(producto.getStock()));
    }

    entradas.get("ID categoría").setText(
            String.valueOf(producto.getId_categoria()));
    }
    
private static void configurarVentas(
        Map<String, JTextField> entradas,
        JButton guardar,
        JButton buscar,
        JTable tabla,
        DefaultTableModel modelo,
        JTable tablaDetalle,
        DefaultTableModel modeloDetalle,
        JLabel aviso) {

    JTextField txtId = entradas.get("ID venta");
    JTextField txtDueno = entradas.get("ID dueño");
    JTextField txtFecha = entradas.get("Fecha (AAAA-MM-DD)");
    JTextField txtTotal = entradas.get("Total");

    // MySQL genera el ID y la fecha de registro
    txtId.setEditable(false);
    txtFecha.setEditable(false);
    txtTotal.setEditable(false);

    // Cargar las ventas existentes
    cargarVentas(modelo, aviso);

    // BOTON GUARDAR
    guardar.addActionListener(e -> {

        try {
            int idDueno = Integer.parseInt(
                    txtDueno.getText().trim());

            if (idDueno <= 0) {
                JOptionPane.showMessageDialog(tabla,
                        "Ingrese un ID de dueño válido.");
                return;
            }

            if (modeloDetalle.getRowCount() == 0) {
                JOptionPane.showMessageDialog(tabla,
                        "Agregue al menos un producto.");
                return;
            }

            List<Detalle_Venta> detalles = new ArrayList<>();
            double total = 0;

            // Convertir las filas del JTable a objetos
            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {

                int idProducto = ((Number)
                        modeloDetalle.getValueAt(i, 0)).intValue();

                int cantidad = ((Number)
                        modeloDetalle.getValueAt(i, 2)).intValue();

                double precio = ((Number)
                        modeloDetalle.getValueAt(i, 3)).doubleValue();

                if (idProducto <= 0 || cantidad <= 0
                        || !Double.isFinite(precio) || precio < 0) {
                    throw new IllegalArgumentException(
                            "Hay un detalle con datos inválidos.");
                }

                Detalle_Venta detalle = new Detalle_Venta(
                        0,
                        0,
                        idProducto,
                        cantidad,
                        precio,
                        0
                );

                detalle.calcularSubtotal();
                detalles.add(detalle);

                total += detalle.getSubtotal();
            }

            if (!Double.isFinite(total)) {
                throw new IllegalArgumentException(
                        "El total calculado no es válido.");
            }

            // Crear la venta con tu constructor
            Venta venta = new Venta(
                    0,
                    idDueno,
                    null,
                    total
            );

            // Registrar venta y detalles con el DAO compartido
            venta.registrarVenta(detalles);

            JOptionPane.showMessageDialog(tabla,
                    "Venta registrada correctamente. ID: "
                    + venta.getID_venta());

            // Limpiar los datos después del guardado
            entradas.values().forEach(c -> c.setText(""));
            modeloDetalle.setRowCount(0);
            tabla.clearSelection();

            // Actualizar las ventas registradas
            cargarVentas(modelo, aviso);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID de dueño numérico válido.");

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(tabla,
                    ex.getMessage());

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al guardar la venta: "
                    + ex.getMessage());
        }
    });

    // BOTON BUSCAR
    buscar.addActionListener(e -> {

        String valor = JOptionPane.showInputDialog(
                tabla, "Ingrese el ID de la venta:");

        if (valor == null) {
            return;
        }

        try {
            int id = Integer.parseInt(valor.trim());

            Venta venta = Venta.buscarVenta(id);

            if (venta == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se encontró la venta.");
                return;
            }

            mostrarDatosVenta(
                    venta, entradas, modeloDetalle);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID numérico válido.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al buscar la venta: "
                    + ex.getMessage());
        }
    });

    // SELECCIONAR UNA VENTA DESDE EL JTABLE
    tabla.getSelectionModel().addListSelectionListener(e -> {

        if (e.getValueIsAdjusting()
                || tabla.getSelectedRow() < 0) {
            return;
        }

        try {
            int fila = tabla.convertRowIndexToModel(
                    tabla.getSelectedRow());

            int idVenta = ((Number)
                    modelo.getValueAt(fila, 0)).intValue();

            Venta venta = Venta.buscarVenta(idVenta);

            if (venta != null) {
                mostrarDatosVenta(
                        venta, entradas, modeloDetalle);
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al cargar detalles: "
                    + ex.getMessage());
        }
    });
    }

private static void cargarVentas(
        DefaultTableModel modelo,
        JLabel aviso) {

    try {
        modelo.setRowCount(0);

        for (Venta venta : Venta.listarVentas()) {

            modelo.addRow(new Object[]{
                venta.getID_venta(),
                venta.getID_dueno(),
                venta.getFecha(),
                venta.getTotal()
            });
        }

        aviso.setText(
                "Total de ventas registradas: "
                + modelo.getRowCount());

    } catch (SQLException ex) {

        aviso.setText("Error al cargar ventas.");

        JOptionPane.showMessageDialog(null,
                "Error de MySQL: "
                + ex.getMessage());
    }
}

private static void mostrarDatosVenta(
        Venta venta,
        Map<String, JTextField> entradas,
        DefaultTableModel modeloDetalle)
        throws SQLException {

    // Consultar primero para evitar mostrar datos parciales
    List<Detalle_Venta> detalles = venta.consultarDetalles();

    entradas.get("ID venta").setText(
            String.valueOf(venta.getID_venta()));

    entradas.get("ID dueño").setText(
            String.valueOf(venta.getID_dueno()));

    entradas.get("Fecha (AAAA-MM-DD)").setText(
            venta.getFecha() == null ? "" : venta.getFecha());

    entradas.get("Total").setText(
            String.format(java.util.Locale.US,
                    "%.2f", venta.getTotal()));

    modeloDetalle.setRowCount(0);

    for (Detalle_Venta detalle : detalles) {

        Producto_Servicio producto =
                Producto_Servicio.buscarProducto(
                        detalle.getID_producto());

        String nombre = producto == null
                ? "Producto no disponible"
                : producto.getNombre();

        modeloDetalle.addRow(new Object[]{
            detalle.getID_producto(),
            nombre,
            detalle.getCantidad(),
            detalle.getPrecio(),
            detalle.getSubtotal()
        });
    }
}

private static JPanel crearPanelDetalleConsulta(
        JTable tablaDetalle,
        DefaultTableModel modeloDetalle) {

    JPanel panel = new JPanel(new BorderLayout(10, 15));
    panel.setBackground(new Color(29, 37, 64));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel titulo = new JLabel("Detalle de consulta");
    titulo.setFont(new Font("Arial", Font.BOLD, 20));
    titulo.setForeground(Color.WHITE);
    panel.add(titulo, BorderLayout.NORTH);

    JPanel campos = new JPanel(new GridLayout(2, 5, 10, 8));
    campos.setOpaque(false);

    String[] etiquetas = {
        "Numero detalle",
        "Motivo detalle",
        "Tratamiento",
        "Dosis",
        "Tiempo dosis"
    };

    JTextField txtNumero = new JTextField();
    JTextField txtMotivo = new JTextField();
    JTextField txtTratamiento = new JTextField();
    JTextField txtDosis = new JTextField();
    JTextField txtTiempo = new JTextField();

    JTextField[] controles = {
        txtNumero, txtMotivo, txtTratamiento,
        txtDosis, txtTiempo
    };

    for (String etiqueta : etiquetas) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setForeground(Color.WHITE);
        campos.add(lbl);
    }

    for (JTextField control : controles) {
        control.setBackground(new Color(21, 28, 48));
        control.setForeground(Color.WHITE);
        control.setCaretColor(Color.WHITE);
        control.setBorder(BorderFactory.createLineBorder(
                new Color(70, 82, 118)));
        campos.add(control);
    }

    JButton btnAgregar = new JButton("Agregar detalle");
    JButton btnQuitar = new JButton("Quitar detalle");

    btnAgregar.setBackground(new Color(40, 108, 161));
    btnAgregar.setForeground(Color.WHITE);

    btnQuitar.setBackground(new Color(130, 70, 210));
    btnQuitar.setForeground(Color.WHITE);

    JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
    botones.setOpaque(false);
    botones.add(btnAgregar);
    botones.add(btnQuitar);

    JPanel superior = new JPanel(new BorderLayout(0, 10));
    superior.setOpaque(false);
    superior.add(campos, BorderLayout.CENTER);
    superior.add(botones, BorderLayout.SOUTH);

    tablaDetalle.setRowHeight(28);
    tablaDetalle.setBackground(new Color(21, 28, 48));
    tablaDetalle.setForeground(Color.WHITE);
    tablaDetalle.setGridColor(new Color(70, 82, 118));

    tablaDetalle.getTableHeader().setBackground(
            new Color(90, 65, 150));
    tablaDetalle.getTableHeader().setForeground(Color.WHITE);

    JScrollPane scroll = new JScrollPane(tablaDetalle);
    scroll.setPreferredSize(new Dimension(700, 180));

    JPanel contenido = new JPanel(new BorderLayout(0, 10));
    contenido.setOpaque(false);
    contenido.add(superior, BorderLayout.NORTH);
    contenido.add(scroll, BorderLayout.CENTER);

    panel.add(contenido, BorderLayout.CENTER);

    // AGREGAR DETALLE
    btnAgregar.addActionListener(e -> {
        try {
            int numero = Integer.parseInt(
                    txtNumero.getText().trim());

            String motivo = txtMotivo.getText().trim();
            String tratamiento = txtTratamiento.getText().trim();
            String dosis = txtDosis.getText().trim();
            String tiempo = txtTiempo.getText().trim();

            if (numero <= 0 || motivo.isEmpty()
                    || tratamiento.isEmpty()
                    || dosis.isEmpty() || tiempo.isEmpty()) {

                JOptionPane.showMessageDialog(panel,
                        "Complete todos los campos del detalle.");
                return;
            }

            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {
                int existente = ((Number)
                        modeloDetalle.getValueAt(i, 0)).intValue();

                if (existente == numero) {
                    JOptionPane.showMessageDialog(panel,
                            "El numero de detalle ya existe.");
                    return;
                }
            }

            modeloDetalle.addRow(new Object[]{
                numero, motivo, tratamiento, dosis, tiempo
            });

            for (JTextField campo : controles) {
                campo.setText("");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel,
                    "El numero de detalle debe ser numerico.");
        }
    });

    // QUITAR DETALLE
    btnQuitar.addActionListener(e -> {
        int fila = tablaDetalle.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(panel,
                    "Seleccione un detalle.");
            return;
        }

        int indice = tablaDetalle.convertRowIndexToModel(fila);
        modeloDetalle.removeRow(indice);
    });

    return panel;
}

private static void configurarConsultas(
        Map<String, JTextField> entradas,
        JButton guardar,
        JButton buscar,
        JTable tabla,
        DefaultTableModel modelo,
        JTable tablaDetalle,
        DefaultTableModel modeloDetalle,
        JLabel aviso) {

    JTextField txtId = entradas.get("ID consulta");
    JTextField txtNumero = entradas.get("Número de consulta");
    JTextField txtCita = entradas.get("ID cita");
    JTextField txtFecha = entradas.get("Fecha (AAAA-MM-DD)");
    JTextField txtEstado = entradas.get("Estado");
    JTextField txtMotivo = entradas.get("Motivo");

    txtId.setEditable(false);

    // Cargar registros
    cargarConsultas(modelo, aviso);

    // GUARDAR CONSULTA
    guardar.addActionListener(e -> {

        try {
            int numero = Integer.parseInt(
                    txtNumero.getText().trim());

            int idCita = Integer.parseInt(
                    txtCita.getText().trim());

            String fecha = txtFecha.getText().trim();
            String estado = txtEstado.getText().trim();
            String motivo = txtMotivo.getText().trim();

            if (numero <= 0 || idCita <= 0
                    || fecha.isEmpty() || estado.isEmpty()) {

                JOptionPane.showMessageDialog(tabla,
                        "Complete los datos de la consulta.");
                return;
            }

            // Comprobar formato de fecha
            java.time.LocalDate.parse(fecha);

            if (modeloDetalle.getRowCount() == 0) {
                JOptionPane.showMessageDialog(tabla,
                        "Agregue al menos un detalle.");
                return;
            }

            List<Detalle_Consulta> detalles = new ArrayList<>();

            for (int i = 0; i < modeloDetalle.getRowCount(); i++) {

                Detalle_Consulta detalle = new Detalle_Consulta(
                        0,
                        0,
                        ((Number) modeloDetalle.getValueAt(i, 0))
                                .intValue(),
                        String.valueOf(modeloDetalle.getValueAt(i, 1)),
                        String.valueOf(modeloDetalle.getValueAt(i, 2)),
                        String.valueOf(modeloDetalle.getValueAt(i, 3)),
                        String.valueOf(modeloDetalle.getValueAt(i, 4))
                );

                detalles.add(detalle);
            }

            Consulta consulta = new Consulta(
                    0,
                    numero,
                    idCita,
                    fecha,
                    "",
                    motivo,
                    "",
                    estado
            );

            // Guardar consulta y detalles
            consulta.registrarConsulta(detalles);

            JOptionPane.showMessageDialog(tabla,
                    "Consulta guardada. ID: "
                    + consulta.getId_consulta());

            entradas.values().forEach(c -> c.setText(""));
            modeloDetalle.setRowCount(0);
            tabla.clearSelection();

            cargarConsultas(modelo, aviso);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Numero de consulta e ID cita deben ser numericos.");

        } catch (java.time.format.DateTimeParseException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "La fecha debe tener formato AAAA-MM-DD.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al guardar consulta: "
                    + ex.getMessage());
        }
    });

    // BUSCAR CONSULTA
    buscar.addActionListener(e -> {

        String valor = JOptionPane.showInputDialog(
                tabla, "Ingrese el ID de consulta:");

        if (valor == null) {
            return;
        }

        try {
            int id = Integer.parseInt(valor.trim());

            Consulta consulta = Consulta.buscarConsulta(id);

            if (consulta == null) {
                JOptionPane.showMessageDialog(tabla,
                        "No se encontro la consulta.");
                return;
            }

            mostrarDatosConsulta(
                    consulta, entradas, modeloDetalle);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Ingrese un ID numerico valido.");

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al buscar consulta: "
                    + ex.getMessage());
        }
    });

    // SELECCIONAR FILA
    tabla.getSelectionModel().addListSelectionListener(e -> {

        if (e.getValueIsAdjusting()
                || tabla.getSelectedRow() < 0) {
            return;
        }

        try {
            int fila = tabla.convertRowIndexToModel(
                    tabla.getSelectedRow());

            int idConsulta = ((Number)
                    modelo.getValueAt(fila, 0)).intValue();

            Consulta consulta = Consulta.buscarConsulta(idConsulta);

            if (consulta != null) {
                mostrarDatosConsulta(
                        consulta, entradas, modeloDetalle);
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(tabla,
                    "Error al cargar detalles: "
                    + ex.getMessage());
        }
    });
}
    

private static void cargarConsultas(
        DefaultTableModel modelo,
        JLabel aviso) {

    try {
        modelo.setRowCount(0);

        for (Consulta consulta : Consulta.listarConsultas()) {

            List<Detalle_Consulta> detalles =
                    consulta.consultarDetalles();

            StringBuilder motivos = new StringBuilder();
            StringBuilder tratamientos = new StringBuilder();
            StringBuilder dosis = new StringBuilder();

            for (Detalle_Consulta detalle : detalles) {

                String motivo = detalle.getMotivo_detalle();
                String tratamiento = detalle.getTratamiento();
                String dosisDetalle = detalle.getDosis();

                if (motivo != null && !motivo.trim().isEmpty()) {
                    if (motivos.length() > 0) {
                        motivos.append("; ");
                    }
                    motivos.append(motivo);
                }

                if (tratamiento != null &&
                        !tratamiento.trim().isEmpty()) {
                    if (tratamientos.length() > 0) {
                        tratamientos.append("; ");
                    }
                    tratamientos.append(tratamiento);
                }

                if (dosisDetalle != null &&
                        !dosisDetalle.trim().isEmpty()) {
                    if (dosis.length() > 0) {
                        dosis.append("; ");
                    }
                    dosis.append(dosisDetalle);
                }
            }

            modelo.addRow(new Object[]{
                consulta.getId_consulta(),
                consulta.getNumero_consulta(),
                consulta.getID_cita(),
                consulta.getFecha(),
                consulta.getEstado_consulta(),
                motivos.toString(),
                tratamientos.toString(),
                dosis.toString()
            });
        }

        aviso.setText(
                "Total de consultas: " + modelo.getRowCount());

    } catch (SQLException ex) {

        aviso.setText("Error al cargar consultas.");

        JOptionPane.showMessageDialog(null,
                "Error de MySQL: " + ex.getMessage());
    }
}


private static void mostrarDatosConsulta(
        Consulta consulta,
        Map<String, JTextField> entradas,
        DefaultTableModel modeloDetalle)
        throws SQLException {

    List<Detalle_Consulta> detalles =
            consulta.consultarDetalles();

    entradas.get("ID consulta").setText(
            String.valueOf(consulta.getId_consulta()));

    entradas.get("Número de consulta").setText(
            String.valueOf(consulta.getNumero_consulta()));

    entradas.get("ID cita").setText(
            String.valueOf(consulta.getID_cita()));

    entradas.get("Fecha (AAAA-MM-DD)").setText(
            consulta.getFecha());

    entradas.get("Estado").setText(
            consulta.getEstado_consulta());
    

    // Mostrar los motivos registrados
    StringBuilder motivos = new StringBuilder();
    StringBuilder tratamientos = new StringBuilder();
    StringBuilder dosis = new StringBuilder();
    for (Detalle_Consulta detalle : detalles) {

    if (detalle.getMotivo_detalle() != null) {

        if (motivos.length() > 0) {
            motivos.append("; ");
        }

        motivos.append(detalle.getMotivo_detalle());
    }
    if (detalle.getTratamiento() != null) {
        if (tratamientos.length() > 0) tratamientos.append("; ");
        tratamientos.append(detalle.getTratamiento());
    }

    if (detalle.getDosis() != null) {
        if (dosis.length() > 0) dosis.append("; ");
        dosis.append(detalle.getDosis());
    }
    }

    entradas.get("Motivo").setText(motivos.toString());
    entradas.get("Tratamiento").setText(tratamientos.toString());
    entradas.get("Dosis").setText(dosis.toString());

    modeloDetalle.setRowCount(0);

    for (Detalle_Consulta detalle : detalles) {

        modeloDetalle.addRow(new Object[]{
            detalle.getNumero_detalle(),
            detalle.getMotivo_detalle(),
            detalle.getTratamiento(),
            detalle.getDosis(),
            detalle.getTiempo_dosis()
        });
    }
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
        if (!seccion.equals("Ventas")
        && !seccion.equals("Consultas")) {

        contenedor.add(listado, BorderLayout.CENTER);
        }
        if (seccion.equals("Citas")) {

        configurarCitas(
        entradas, guardar, buscar,
        tabla, modelo, aviso
        );

        } else if (seccion.equals("Mascotas")) {

        configurarMascotas(
            entradas, guardar, buscar,
            tabla, modelo, aviso
        );

        } else if (seccion.equals("Dueños")) {

    configurarDuenos(
        entradas, guardar, buscar,
        tabla, modelo, aviso
    );
    } else if (seccion.equals("Veterinarios")) {

    configurarVeterinarios(
        entradas, guardar, buscar,
        tabla, modelo, aviso
    );
    } else if (seccion.equals("Productos / Servicios")) {

    configurarProductosServicios(
        entradas, guardar, buscar,
        tabla, modelo, aviso
    );
    } else if (seccion.equals("Ventas")) {
        
        DefaultTableModel modeloDetalle =
            new DefaultTableModel(
                new Object[]{
                    "ID producto",
                    "Producto",
                    "Cantidad",
                    "Precio unitario",
                    "Subtotal"
                }, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    JTable tablaDetalle = new JTable(modeloDetalle);

    JTextField txtTotal = entradas.get("Total");
    txtTotal.setEditable(false);

    JPanel panelDetalle = crearPanelDetalleVenta(
            txtTotal,
            tablaDetalle,
            modeloDetalle
    );
    // Panel que organizará las secciones de ventas
    JPanel contenidoVentas = new JPanel();

    contenidoVentas.setLayout(
        new BoxLayout(contenidoVentas, BoxLayout.Y_AXIS)
    );

    contenidoVentas.setOpaque(false);

    // Agregar el detalle de venta
    panelDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
    contenidoVentas.add(panelDetalle);

    contenidoVentas.add(Box.createVerticalStrut(15));

    // Agregar los registros de ventas
    listado.setAlignmentX(Component.LEFT_ALIGNMENT);
    listado.setPreferredSize(new Dimension(800, 250));
    contenidoVentas.add(listado);

    // Incorporar las secciones al formulario
    contenedor.add(
        new JScrollPane(contenidoVentas),
        BorderLayout.CENTER
    );
    configurarVentas(
        entradas, guardar, buscar, tabla, modelo, tablaDetalle, modeloDetalle, aviso
        );
    } else if (seccion.equals("Consultas")) {

    // 1. Crear modelo de detalles
    DefaultTableModel modeloDetalle =
            new DefaultTableModel(
                new Object[]{
                    "Numero detalle",
                    "Motivo detalle",
                    "Tratamiento",
                    "Dosis",
                    "Tiempo dosis"
                }, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    // 2. Crear tabla de detalles
    JTable tablaDetalle = new JTable(modeloDetalle);

    // 3. Crear panel
    JPanel panelDetalle = crearPanelDetalleConsulta(
            tablaDetalle, modeloDetalle);

    // 4. Organizar paneles
    JPanel contenidoConsultas = new JPanel();

    contenidoConsultas.setLayout(
            new BoxLayout(contenidoConsultas, BoxLayout.Y_AXIS)
    );

    contenidoConsultas.setOpaque(false);

    panelDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
    contenidoConsultas.add(panelDetalle);

    contenidoConsultas.add(Box.createVerticalStrut(15));

    listado.setAlignmentX(Component.LEFT_ALIGNMENT);
    listado.setPreferredSize(new Dimension(800, 250));
    contenidoConsultas.add(listado);

    contenedor.add(
            new JScrollPane(contenidoConsultas),
            BorderLayout.CENTER
    );

    // 5. Conectar con MySQL
    configurarConsultas(
            entradas, guardar, buscar, tabla, modelo,
            tablaDetalle, modeloDetalle, aviso
    );

    } else {

    guardar.addActionListener(e ->
        JOptionPane.showMessageDialog(
            formulario, "Integración pendiente para " + seccion));

    buscar.addActionListener(e ->
        JOptionPane.showMessageDialog(
            formulario, "Búsqueda pendiente para " + seccion));
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

private static void cargarReportes(
        JLabel lblMascotas,
        JLabel lblCitas,
        JLabel lblConsultas,
        JLabel lblVentas,
        DefaultTableModel modelo) {

    try {
        // Obtener cantidades desde las clases existentes
        int totalMascotas = Mascota.listarMascotas().size();
        int totalCitas = Cita.listarCitas().size();
        int totalConsultas = Consulta.listarConsultas().size();
        int totalVentas = Venta.listarVentas().size();

        // Actualizar indicadores
        lblMascotas.setText(String.valueOf(totalMascotas));
        lblCitas.setText(String.valueOf(totalCitas));
        lblConsultas.setText(String.valueOf(totalConsultas));
        lblVentas.setText(String.valueOf(totalVentas));

        // Actualizar tabla de resumen
        modelo.setRowCount(0);

        modelo.addRow(new Object[]{
            "Mascotas",
            "Registros de mascotas",
            totalMascotas
        });

        modelo.addRow(new Object[]{
            "Citas",
            "Citas veterinarias",
            totalCitas
        });

        modelo.addRow(new Object[]{
            "Consultas",
            "Atenciones realizadas",
            totalConsultas
        });

        modelo.addRow(new Object[]{
            "Ventas",
            "Operaciones registradas",
            totalVentas
        });

    } catch (SQLException ex) {

        JOptionPane.showMessageDialog(
                null,
                "Error al cargar reportes: " + ex.getMessage(),
                "Error de MySQL",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

private static JPanel crearTarjetaReporte(
        String titulo,
        JLabel lblValor) {

    JPanel panel = new JPanel(new BorderLayout(0, 15));

    panel.setBackground(new Color(29, 37, 64));
    panel.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
    );

    JLabel lblTitulo = new JLabel(titulo);
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setFont(
            new Font("Arial", Font.PLAIN, 18)
    );

    lblValor.setText("--");
    lblValor.setForeground(new Color(178, 119, 255));
    lblValor.setFont(
            new Font("Arial", Font.BOLD, 30)
    );

    panel.add(lblTitulo, BorderLayout.NORTH);
    panel.add(lblValor, BorderLayout.CENTER);

    return panel;
}

private static JPanel crearPanelReportes() {

    JPanel principal = new JPanel(new BorderLayout(15, 20));
    principal.setBackground(new Color(13, 17, 34));

    principal.setBorder(
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
    );

    // Titulo principal
    JLabel titulo = new JLabel("Reportes");
    titulo.setForeground(Color.WHITE);
    titulo.setFont(new Font("Arial", Font.BOLD, 30));

    principal.add(titulo, BorderLayout.NORTH);

    // Indicadores
    JLabel lblMascotas = new JLabel("--");
    JLabel lblCitas = new JLabel("--");
    JLabel lblConsultas = new JLabel("--");
    JLabel lblVentas = new JLabel("--");

    JPanel tarjetas = new JPanel(new GridLayout(2, 2, 20, 20));
    tarjetas.setOpaque(false);

    tarjetas.add(crearTarjetaReporte(
            "Mascotas registradas", lblMascotas));

    tarjetas.add(crearTarjetaReporte(
            "Citas programadas", lblCitas));

    tarjetas.add(crearTarjetaReporte(
            "Consultas realizadas", lblConsultas));

    tarjetas.add(crearTarjetaReporte(
            "Ventas registradas", lblVentas));

    // Tabla de resumen
    DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{
                "Categoria", "Descripcion", "Total"
            }, 0) {

        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    JTable tabla = new JTable(modelo);

    tabla.setRowHeight(38);
    tabla.setBackground(new Color(29, 37, 64));
    tabla.setForeground(Color.WHITE);
    tabla.setGridColor(new Color(70, 82, 118));

    tabla.getTableHeader().setBackground(
            new Color(90, 65, 150));

    tabla.getTableHeader().setForeground(Color.WHITE);

    JPanel panelTabla = new JPanel(new BorderLayout(0, 15));

    panelTabla.setBackground(new Color(29, 37, 64));

    panelTabla.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
    );

    JLabel tituloTabla =
            new JLabel("Resumen general del sistema");

    tituloTabla.setForeground(Color.WHITE);
    tituloTabla.setFont(
            new Font("Arial", Font.BOLD, 22)
    );

    panelTabla.add(tituloTabla, BorderLayout.NORTH);
    panelTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

    // Organizar contenido
    JPanel contenido = new JPanel(new GridLayout(2, 1, 0, 20));
    contenido.setOpaque(false);

    contenido.add(tarjetas);
    contenido.add(panelTabla);

    principal.add(contenido, BorderLayout.CENTER);

    // Consultar datos de MySQL
    cargarReportes(
            lblMascotas,
            lblCitas,
            lblConsultas,
            lblVentas,
            modelo
    );

    return principal;
}


}
