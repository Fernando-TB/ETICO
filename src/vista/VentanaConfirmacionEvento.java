package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaConfirmacionEvento extends JDialog {

    private boolean confirmado = false; // valor a devolver

    public VentanaConfirmacionEvento(JFrame parent, String mensaje) {
        super(parent, "Confirmación", true); // true = modal

        setLayout(new BorderLayout(10, 10));
        setSize(350, 140);
        setLocationRelativeTo(parent);

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnSi = new JButton("Sí");
        JButton btnNo = new JButton("No");

        panelBotones.add(btnSi);
        panelBotones.add(btnNo);
        add(panelBotones, BorderLayout.SOUTH);

        btnSi.addActionListener((ActionEvent e) -> {
            confirmado = true;
            dispose();
        });

        btnNo.addActionListener((ActionEvent e) -> {
            confirmado = false;
            dispose();
        });
    }

    public boolean mostrar() {
        setVisible(true);
        return confirmado;
    }
}
