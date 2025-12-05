package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaConfirmacionEvento extends JDialog {

    private boolean confirmado = false;

    public VentanaConfirmacionEvento(JFrame parent, String mensaje) {
        super(parent, "Confirmación", true);

        Color colorFondo = new Color(41, 49, 51);
        Color colorTexto = Color.WHITE;
        Color colorPanelBotones = new Color(56, 65, 69);
        Color colorBotones = new Color(34, 39, 41);

        setLayout(new BorderLayout(10, 10));
        setSize(350, 140);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(colorFondo);

        JLabel lblMensaje = new JLabel(mensaje, SwingConstants.CENTER);
        lblMensaje.setForeground(colorTexto);
        add(lblMensaje, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(colorPanelBotones);

        JButton btnSi = new JButton("Sí");
        JButton btnNo = new JButton("No");

        btnSi.setBackground(colorBotones);
        btnNo.setBackground(colorBotones);
        btnSi.setForeground(colorTexto);
        btnNo.setForeground(colorTexto);

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
