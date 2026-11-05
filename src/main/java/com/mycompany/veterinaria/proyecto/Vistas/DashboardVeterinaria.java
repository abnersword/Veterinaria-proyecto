package com.mycompany.veterinaria.proyecto.Vistas;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DashboardVeterinaria extends JFrame {
    private static final Color FONDO = new Color(12, 17, 35);
    private static final Color PANEL = new Color(28, 35, 61);
    private static final Color MORADO = new Color(135, 83, 218);
    private static final Color TEXTO = new Color(242, 239, 255);
    private final JPanel contenido = new JPanel(new BorderLayout());

    public DashboardVeterinaria() {
        setTitle("Veterinaria - Sistema de Gestión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        setContentPane(raiz);

        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBackground(new Color(15, 19, 40));
        lateral.setBorder(new EmptyBorder(26, 15, 20, 15));
        lateral.setPreferredSize(new Dimension(235, 0));
        JLabel logo = new JLabel("🐾 VETERINARIA", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        logo.setForeground(new Color(188, 148, 255));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lateral.add(logo);
        lateral.add(Box.createVerticalStrut(35));
        String[] secciones = {"Inicio", "Citas", "Mascotas", "Dueños", "Veterinarios", "Productos / Servicios", "Ventas", "Consultas", "Reportes", "Configuración"};
        for (String seccion : secciones) {
            JButton boton = botonMenu(seccion);
            lateral.add(boton);
            lateral.add(Box.createVerticalStrut(7));
        }
        lateral.add(Box.createVerticalGlue());
        JButton salir = botonMenu("Cerrar sesión");
        salir.addActionListener(e -> dispose());
        lateral.add(salir);
        raiz.add(lateral, BorderLayout.WEST);

        contenido.setBackground(FONDO);
        raiz.add(contenido, BorderLayout.CENTER);
        mostrarInicio();
    }

    
private JButton botonMenu(String nombre) {
    JButton boton = new JButton(nombre);

    Color colorNormal = new Color(15, 19, 40);
    Color colorHover = new Color(65, 48, 110);

    boton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
    boton.setForeground(TEXTO);
    boton.setBackground(
        nombre.equals("Inicio") ? MORADO : colorNormal
    );

    boton.setBorder(new EmptyBorder(11, 18, 11, 10));
    boton.setHorizontalAlignment(SwingConstants.LEFT);
    boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
    boton.setFocusPainted(false);
    boton.setOpaque(true);
    boton.setContentAreaFilled(true);
    boton.setCursor(
        Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
    );

    boton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            boton.setBackground(colorHover);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            boton.setBackground(
                nombre.equals("Inicio") ? MORADO : colorNormal
            );
        }
    });

    boton.addActionListener(e -> {
        if (nombre.equals("Inicio")) {
            mostrarInicio();
        } else {
            mostrarSeccion(nombre);
        }
    });

    return boton;
}


    private JPanel tarjeta(String titulo, String valor, Color acento) {
        JPanel panel = new JPanel(new BorderLayout(12, 4));
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 83, 123), 1, true),
                new EmptyBorder(15, 18, 15, 18)));
        JLabel numero = new JLabel(valor);
        numero.setFont(new Font("Segoe UI", Font.BOLD, 33));
        numero.setForeground(acento);
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setForeground(TEXTO);
        etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(numero, BorderLayout.CENTER);
        panel.add(etiqueta, BorderLayout.SOUTH);
        return panel;
    }

    private JLabel titulo(String texto, int tamano) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, tamano));
        label.setForeground(TEXTO);
        return label;
    }

    private void mostrarInicio() {
        contenido.removeAll();
        JPanel principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(FONDO);
        principal.setBorder(new EmptyBorder(26, 25, 25, 25));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(titulo("Bienvenido al Sistema de la Veterinaria", 26), BorderLayout.WEST);
        JLabel fecha = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("es", "GT"))));
        fecha.setForeground(new Color(194, 164, 255));
        cabecera.add(fecha, BorderLayout.EAST);
        principal.add(cabecera);
        principal.add(Box.createVerticalStrut(8));
        JLabel subtitulo = new JLabel("Gestiona citas, mascotas, consultas, ventas y más de forma rápida y segura.");
        subtitulo.setForeground(new Color(190, 194, 216));
        principal.add(subtitulo);
        principal.add(Box.createVerticalStrut(24));

        JPanel estadisticas = new JPanel(new GridLayout(1, 4, 12, 0));
        estadisticas.setOpaque(false);
        estadisticas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 105));
        estadisticas.add(tarjeta("Mascotas registradas", "12", new Color(95, 169, 255)));
        estadisticas.add(tarjeta("Citas hoy", "5", new Color(186, 129, 255)));
        estadisticas.add(tarjeta("Consultas activas", "3", new Color(95, 169, 255)));
        estadisticas.add(tarjeta("Productos en stock", "8", new Color(255, 139, 198)));
        principal.add(estadisticas);
        principal.add(Box.createVerticalStrut(22));


JPanel banner = new JPanel(new BorderLayout(20, 0));
banner.setBackground(new Color(66, 44, 113));
banner.setBorder(new EmptyBorder(15, 25, 15, 25));
banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
banner.setPreferredSize(new Dimension(900, 180));

// Textos del banner
JPanel textosBanner = new JPanel();
textosBanner.setLayout(new BoxLayout(textosBanner, BoxLayout.Y_AXIS));
textosBanner.setOpaque(false);

JLabel mensaje = titulo(
    "Cuidamos la salud y el bienestar de tu mascota", 20
);
textosBanner.add(Box.createVerticalGlue());
textosBanner.add(mensaje);
textosBanner.add(Box.createVerticalStrut(12));

JLabel servicios = new JLabel(
    "Consultas  •  Vacunación  •  Tratamientos"
);
servicios.setForeground(new Color(230, 211, 255));
textosBanner.add(servicios);
textosBanner.add(Box.createVerticalGlue());

banner.add(textosBanner, BorderLayout.CENTER);

// Imagen del perro y el gato
java.net.URL rutaImagen = getClass().getResource(
    "/imagenes/mascotas.png"
);
System.out.println("Ruta de imagen: " + rutaImagen);

if (rutaImagen != null) {
    ImageIcon icono = new ImageIcon(rutaImagen);

    Image imagenEscalada = icono.getImage().getScaledInstance(
        230, 145, Image.SCALE_SMOOTH
    );

    JLabel imagenMascotas = new JLabel(
        new ImageIcon(imagenEscalada)
    );

    imagenMascotas.setHorizontalAlignment(SwingConstants.CENTER);
    banner.add(imagenMascotas, BorderLayout.EAST);
} else {
    System.out.println("No se encontró mascotas.png");
}

principal.add(banner);

        principal.add(Box.createVerticalStrut(22));

        JPanel inferior = new JPanel(new GridLayout(1, 2, 18, 0));
        inferior.setOpaque(false);
        JPanel bloqueCitas = new JPanel(new BorderLayout(0, 12));
        bloqueCitas.setBackground(PANEL);
        bloqueCitas.setBorder(new EmptyBorder(18, 15, 18, 15));
        bloqueCitas.add(titulo("Próximas citas", 19), BorderLayout.NORTH);
        String[] columnas = {"N.º", "Fecha", "Hora", "Mascota", "Estado"};
        Object[][] filas = {
            {"0012", "03/11/2026", "10:00", "Max", "Programada"},
            {"0013", "03/11/2026", "11:30", "Luna", "Programada"},
            {"0014", "03/11/2026", "15:00", "Toby", "Pendiente"},
            {"0015", "04/11/2026", "09:00", "Kiara", "Programada"}
        };
        JTable tabla = new JTable(new DefaultTableModel(filas, columnas) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        });
        tabla.setRowHeight(38);
        tabla.setBackground(new Color(20, 27, 49));
        tabla.setForeground(TEXTO);
        tabla.setSelectionBackground(MORADO);
        tabla.setGridColor(new Color(55, 62, 91));
        tabla.getTableHeader().setBackground(new Color(88, 62, 150));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(PANEL);
        bloqueCitas.add(scroll, BorderLayout.CENTER);
        inferior.add(bloqueCitas);

        JPanel accesos = new JPanel(new BorderLayout(0, 14));
        accesos.setBackground(PANEL);
        accesos.setBorder(new EmptyBorder(18, 15, 18, 15));
        accesos.add(titulo("Accesos rápidos", 19), BorderLayout.NORTH);
        JPanel cuadricula = new JPanel(new GridLayout(2, 3, 10, 10));
        cuadricula.setOpaque(false);
        String[] nombres = {"Nueva cita", "Registrar mascota", "Nuevo dueño", "Registrar consulta", "Nueva venta", "Ver reportes"};
        String[] destinos = {"Citas", "Mascotas", "Dueños", "Consultas", "Ventas", "Reportes"};
        for (int i = 0; i < nombres.length; i++) {
            final String destino = destinos[i];
            JButton b = new JButton("<html><center>" + nombres[i] + "</center></html>");
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setForeground(Color.WHITE);
            b.setBackground(i % 2 == 0 ? new Color(42, 82, 144) : new Color(92, 54, 152));
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addActionListener(e -> mostrarSeccion(destino));
            cuadricula.add(b);
        }
        accesos.add(cuadricula, BorderLayout.CENTER);
        inferior.add(accesos);
        principal.add(inferior);

        JScrollPane scrollPrincipal = new JScrollPane(principal);
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);
        contenido.add(scrollPrincipal, BorderLayout.CENTER);
        contenido.revalidate();
        contenido.repaint();
    }

    
private void mostrarSeccion(String nombre) {
    contenido.removeAll();

    JPanel pagina = PantallasVeterinaria.crear(nombre);

    JScrollPane scroll = new JScrollPane(pagina);
    scroll.setBorder(null);
    scroll.getVerticalScrollBar().setUnitIncrement(16);

    contenido.add(scroll, BorderLayout.CENTER);

    contenido.revalidate();
    contenido.repaint();
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardVeterinaria().setVisible(true));
    }
}

