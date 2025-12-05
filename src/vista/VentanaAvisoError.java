package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaAvisoError extends JDialog {

    public VentanaAvisoError(JFrame parent, String mensaje) {
        super(parent, "Alerta de Disponibilidad", true);

        Color colorFondo = new Color(41, 49, 51);
        Color colorTexto = Color.WHITE;
        Color colorPanelBotones = new Color(56, 65, 69);
        Color colorBotones = new Color(34, 39, 41);

        setLayout(new BorderLayout(10, 10));
        setSize(400, 150);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(colorFondo);

        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            setIconImage(Logo.getImage());
        } catch (Exception e) {}

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(colorPanelBotones);

        JButton btnCerrar = new JButton("Aceptar");
        btnCerrar.setBackground(colorBotones);
        btnCerrar.setForeground(colorTexto);

        panelBoton.add(btnCerrar);
        btnCerrar.addActionListener(e -> dispose());

        add(panelBoton, BorderLayout.SOUTH);
    }

    public void mostrar() {
        setVisible(true);
    }
}
