package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaAvisoError extends JDialog {

    public VentanaAvisoError(JFrame parent, String mensaje) {
        super(parent, "Alerta de Disponibilidad", true);

        setLayout(new BorderLayout(10, 10));
        setSize(400, 150);
        setLocationRelativeTo(parent);

        try {
            ImageIcon Logo = new ImageIcon(getClass().getResource("/LOGO.png"));
            setIconImage(Logo.getImage());
        } catch (Exception e) {
        }

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 12));

        add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        JButton btnCerrar = new JButton("Aceptar");
        panelBoton.add(btnCerrar);

        btnCerrar.addActionListener(e -> dispose());

        add(panelBoton, BorderLayout.SOUTH);
    }

    public void mostrar() {
        setVisible(true);
    }
}