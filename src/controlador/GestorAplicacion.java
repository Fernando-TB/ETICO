
package controlador;

import java.io.*;
import java.time.DateTimeException;
import java.util.*;

import modelo.*;
import vista.*;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.YearMonth;

import modelo.Registrar;
import controlador.ConversorCSV;

public class GestorAplicacion implements IControladorAgendamiento, IControladorAutenticacion, IControladorNavegacion, IControladorCitas, IControladorEquipos {

    private final Registrar registroUsuarios;
    private final Logueo logueo;
    private final Logica logica;
    private final ConversorCSV conversorCSV;


    public boolean agendarCita(String emailUsuario, String fecha, String horaInicio, String horaFin, String titulo) {
        return logica.agendarCita(emailUsuario, fecha, horaInicio, horaFin, titulo);
    }

    public Map<LocalDate, String> obtenerCitasEntreFechas(LocalDate inicio, LocalDate fin, String usuario) throws IOException {

        Map<LocalDate, String> citasSemana = new HashMap<>();

        String archivo = String.format("eventos_%s.txt", usuario);

        TXTtoCalendario txttocalendario = new TXTtoCalendario();

        Calendario calendario = txttocalendario.TXTaCalendario(archivo);

        List<EventoCalendario> eventos = calendario.getCalendario();

        for (EventoCalendario evento : eventos) {

            try {

                String dia = evento.getDia().trim();

                if (dia.length() > 10) {
                    dia = dia.substring(0, 10);
                }

                LocalDate fechaCita = LocalDate.parse(dia);

                if (!fechaCita.isBefore(inicio) && !fechaCita.isAfter(fin)) {

                    String horaInicio = evento.getHoraMinInicio();
                    String horaFin = evento.getHoraMinFin();

                    String descripcion = evento.getNombreEvento() + ", " + horaInicio + " - " + horaFin;

                    String citasPrevias = citasSemana.getOrDefault(fechaCita, "");
                    if (!citasPrevias.isEmpty()) citasPrevias += "<br>";

                    citasSemana.put(fechaCita, citasPrevias + descripcion);
                }

            } catch (Exception e) {
                System.err.println("Error procesando cita desde CSV: " + evento.getNombreEvento() + " - " + e.getMessage());
            }
        }

        return citasSemana;
    }

    private List<String> readEventDataFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            String line;
            if ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    lines.add(parts[0].trim());
                    lines.add(parts[1].trim());
                    lines.add(parts[2].trim());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ADVERTENCIA: Archivo de datos de evento '" + filePath + "' no encontrado. Saltando la creación del evento.");
            return Collections.emptyList();
        }
        return lines;
    }

    public void navegarAAgendarReunion(String usuario, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);
        SwingUtilities.invokeLater(() -> {
            new CalendarioAgendar(this, this, usuario, contrasena, rol).mostrar();
        });
    }

    public GestorAplicacion() {
        this.registroUsuarios = new Registrar();
        this.logueo = new Logueo(this.registroUsuarios);

        ManejadorConsola manejadorConsola = new ManejadorConsola();

        this.conversorCSV = new ConversorCSV();
        this.logica = new Logica(manejadorConsola, conversorCSV);
    }

    public String intentarLogin(String correo, String contrasena) {
        return logueo.obtenerRol(correo, contrasena);
    }

    public boolean registrarNuevoUsuario(String correo, String contrasena, String rol) {
        return registroUsuarios.guardarUsuario(correo, contrasena, rol);
    }

    public void navegarALogin() {
        SwingUtilities.invokeLater(() -> new VentanaLogin(this, this).mostrar());
    }

    public void navegarARegistro() {
        SwingUtilities.invokeLater(() -> new VentanaRegistro(this, this).mostrar());
    }

    public void navegarACalendarioVista(String usuario, String contrasena, String rol, JFrame ventanaActual) {

        cerrarVentanaActual(ventanaActual);

        CalendarioVista calendario = new CalendarioVista(usuario, contrasena, rol, this, this, this);
        calendario.mostrar();
    }

    public void navegarAVentanaJefe(String usuario, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);
        SwingUtilities.invokeLater(() -> {
            new VentanaJefe(this, this, usuario, contrasena, this).mostrar();
        });


    }

    public void navegarAVentanaTrabajador(String usuario, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);
        SwingUtilities.invokeLater(() -> {
            new VentanaTrabajador(this, this, usuario, contrasena).mostrar();
        });

    }

    public void navegarAAgregarEquipo(String correo, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);
        SwingUtilities.invokeLater(() -> {
            new VentanaAgregarEquipo(this, registroUsuarios, correo, contrasena, rol, this).setVisible(true);
        });
    }

    public boolean agregarPersonaAEquipo(String correoJefe, String correoIntegrante) {
        String ruta = "Equipos.csv"; // mismo directorio del proyecto

        try (FileWriter fw = new FileWriter(ruta, true)) {
            fw.write(correoJefe + ";" + correoIntegrante + "\n");
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar equipo: " + e.getMessage());
            return false;
        }
    }

    public void cerrarVentanaActual(JFrame frameActual) {
        if (frameActual != null) {
            SwingUtilities.invokeLater(() -> frameActual.dispose());
        }

    }

    public List<String> obtenerEquipo(String correoJefe) {
        List<String> resultado = new ArrayList<>();
        String ruta = "Equipos.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length == 2 && datos[0].equalsIgnoreCase(correoJefe)) {
                    resultado.add(datos[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo Equipos.csv: " + e.getMessage());
        }

        return resultado;
    }

    public void navegarAIniciarEvento(String usuario, String contrasena, String rol, JFrame ventanaActual) {
        cerrarVentanaActual(ventanaActual);

        SwingUtilities.invokeLater(() -> {
            new VentanaEvento(this, this,this, usuario, contrasena, rol);
        });
    }

    public int convertirDuracionAMinutos(String duracion) {
        String[] partes = duracion.split(":");

        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);

        return horas * 60 + minutos;
    }

}
