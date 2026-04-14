package com.agenda;

import com.agenda.ui.MainFrame;
import com.agenda.util.DatabaseUtil;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Usar o look and feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Mantém o look and feel padrão se falhar
        }

        // Inicializar banco de dados
        DatabaseUtil.inicializarBanco();

        // Abrir a janela principal na EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
