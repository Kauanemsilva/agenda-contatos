package com.agenda.ui;

import com.agenda.model.Contato;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ContatoDialog extends JDialog {

    private final JTextField txtNome     = new JTextField(25);
    private final JTextField txtTelefone = new JTextField(25);
    private final JTextField txtEmail    = new JTextField(25);
    private final JComboBox<String> cmbGrupo = new JComboBox<>(
            new String[]{"Geral", "Família", "Amigos", "Trabalho", "Outros"});

    private boolean confirmado = false;
    private Contato contato;

    // ── Construtor: novo contato ──────────────────────────────────────────────
    public ContatoDialog(Frame parent) {
        this(parent, null);
    }

    // ── Construtor: editar contato ────────────────────────────────────────────
    public ContatoDialog(Frame parent, Contato contato) {
        super(parent, contato == null ? "Novo Contato" : "Editar Contato", true);
        this.contato = contato;

        if (contato != null) {
            txtNome.setText(contato.getNome());
            txtTelefone.setText(contato.getTelefone());
            txtEmail.setText(contato.getEmail());
            cmbGrupo.setSelectedItem(contato.getGrupo());
        }

        construirUI();
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void construirUI() {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBorder(new EmptyBorder(20, 24, 16, 24));

        // ── Formulário ────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nome *", "Telefone", "E-mail", "Grupo"};
        JComponent[] campos = {txtNome, txtTelefone, txtEmail, cmbGrupo};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 12f));
            form.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            form.add(campos[i], gbc);
        }

        // ── Botões ────────────────────────────────────────────────────────────
        JButton btnSalvar   = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.setPreferredSize(new Dimension(90, 32));
        btnCancelar.setPreferredSize(new Dimension(90, 32));
        btnSalvar.setBackground(new Color(37, 99, 235));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        getRootPane().setDefaultButton(btnSalvar);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.add(btnCancelar);
        botoes.add(btnSalvar);

        painel.add(form, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);
        setContentPane(painel);
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O campo Nome é obrigatório.", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return;
        }

        if (contato == null) {
            contato = new Contato();
        }
        contato.setNome(nome);
        contato.setTelefone(txtTelefone.getText().trim());
        contato.setEmail(txtEmail.getText().trim());
        contato.setGrupo((String) cmbGrupo.getSelectedItem());

        confirmado = true;
        dispose();
    }

    public boolean isConfirmado() { return confirmado; }
    public Contato getContato()   { return contato; }
}
