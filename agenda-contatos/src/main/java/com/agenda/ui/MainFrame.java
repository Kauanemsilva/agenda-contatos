package com.agenda.ui;

import com.agenda.dao.ContatoDAO;
import com.agenda.model.Contato;
import com.agenda.util.DatabaseUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private final ContatoDAO dao = new ContatoDAO();
    private final ContatoTableModel tableModel = new ContatoTableModel();
    private final JTable tabela = new JTable(tableModel);
    private final JTextField txtBusca = new JTextField(20);
    private final JComboBox<String> cmbFiltroGrupo = new JComboBox<>(
            new String[]{"Todos", "Geral", "Família", "Amigos", "Trabalho", "Outros"});
    private final JLabel lblStatus = new JLabel("Carregando...");
    private TableRowSorter<ContatoTableModel> sorter;

    public MainFrame() {
        super("📒 Agenda de Contatos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 560);
        setMinimumSize(new Dimension(640, 420));
        setLocationRelativeTo(null);

        construirUI();
        configurarTabela();
        carregarContatos();

        // Fechar conexão ao sair
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                DatabaseUtil.fecharConexao();
            }
        });
    }

    // ── Layout principal ──────────────────────────────────────────────────────
    private void construirUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        setContentPane(root);

        root.add(criarPainelTopo(), BorderLayout.NORTH);
        root.add(criarPainelCentro(), BorderLayout.CENTER);
        root.add(criarPainelRodape(), BorderLayout.SOUTH);
    }

    // ── Painel topo: título + busca + filtro + botões ─────────────────────────
    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new BorderLayout(12, 8));
        painel.setBorder(new EmptyBorder(14, 16, 10, 16));
        painel.setBackground(new Color(37, 99, 235));

        // Título
        JLabel titulo = new JLabel("Agenda de Contatos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        titulo.setForeground(Color.WHITE);

        // Barra de busca + filtro
        JPanel barraFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barraFiltro.setOpaque(false);

        JLabel lblBusca = new JLabel("Buscar:");
        lblBusca.setForeground(new Color(220, 230, 255));

        txtBusca.setPreferredSize(new Dimension(200, 28));
        txtBusca.putClientProperty("JTextField.placeholderText", "Digite o nome...");

        JLabel lblGrupo = new JLabel("Grupo:");
        lblGrupo.setForeground(new Color(220, 230, 255));

        cmbFiltroGrupo.setPreferredSize(new Dimension(120, 28));

        barraFiltro.add(lblBusca);
        barraFiltro.add(txtBusca);
        barraFiltro.add(Box.createHorizontalStrut(8));
        barraFiltro.add(lblGrupo);
        barraFiltro.add(cmbFiltroGrupo);

        // Botões de ação
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botoes.setOpaque(false);

        JButton btnNovo    = criarBotao("+ Novo",   Color.WHITE, new Color(34, 197, 94));    // Verde
        JButton btnEditar  = criarBotao("Editar",   Color.WHITE, new Color(59, 130, 246));  // Azul
        JButton btnExcluir = criarBotao("Excluir",  Color.WHITE, new Color(239, 68, 68));   // Vermelho

        btnNovo.addActionListener(e -> abrirFormularioNovo());
        btnEditar.addActionListener(e -> abrirFormularioEditar());
        btnExcluir.addActionListener(e -> excluirContato());

        botoes.add(btnNovo);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);

        JPanel centro = new JPanel(new BorderLayout(8, 4));
        centro.setOpaque(false);
        centro.add(barraFiltro, BorderLayout.WEST);
        centro.add(botoes, BorderLayout.EAST);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(centro, BorderLayout.SOUTH);

        // Listeners de busca e filtro
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filtrar(); }
            public void removeUpdate(DocumentEvent e)  { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
        cmbFiltroGrupo.addActionListener(e -> carregarContatos());

        return painel;
    }

    // ── Painel central: tabela ────────────────────────────────────────────────
    private JPanel criarPainelCentro() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(new EmptyBorder(0, 0, 0, 0));
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    // ── Painel rodapé: status ─────────────────────────────────────────────────
    private JPanel criarPainelRodape() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        painel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));
        lblStatus.setFont(lblStatus.getFont().deriveFont(11f));
        lblStatus.setForeground(Color.GRAY);
        painel.add(lblStatus);
        return painel;
    }

    // ── Configuração da tabela ────────────────────────────────────────────────
    private void configurarTabela() {
        tabela.setRowHeight(26);
        tabela.setShowGrid(true);
        tabela.setGridColor(new Color(235, 235, 235));
        tabela.setSelectionBackground(new Color(219, 234, 254));
        tabela.setSelectionForeground(Color.BLACK);
        tabela.setFont(tabela.getFont().deriveFont(13f));
        tabela.getTableHeader().setFont(tabela.getFont().deriveFont(Font.BOLD, 13f));
        tabela.getTableHeader().setBackground(new Color(243, 244, 246));
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Larguras das colunas
        int[] larguras = {40, 200, 140, 220, 100};
        for (int i = 0; i < larguras.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }

        // Sorter
        sorter = new TableRowSorter<>(tableModel);
        tabela.setRowSorter(sorter);

        // Duplo clique para editar
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) abrirFormularioEditar();
            }
        });
    }

    // ── Carregar contatos do banco ────────────────────────────────────────────
    private void carregarContatos() {
        try {
            String grupo = (String) cmbFiltroGrupo.getSelectedItem();
            List<Contato> lista = dao.buscarPorGrupo(grupo);
            tableModel.setContatos(lista);
            atualizarStatus(lista.size());
            txtBusca.setText("");
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar contatos: " + e.getMessage());
        }
    }

    // ── Filtrar por nome (no sorter, sem ir ao banco) ─────────────────────────
    private void filtrar() {
        String texto = txtBusca.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1));
        }
    }

    // ── Novo contato ──────────────────────────────────────────────────────────
    private void abrirFormularioNovo() {
        ContatoDialog dialog = new ContatoDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try {
                dao.inserir(dialog.getContato());
                carregarContatos();
            } catch (SQLException e) {
                mostrarErro("Erro ao salvar contato: " + e.getMessage());
            }
        }
    }

    // ── Editar contato selecionado ────────────────────────────────────────────
    private void abrirFormularioEditar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um contato para editar.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int linhaModelo = tabela.convertRowIndexToModel(linha);
        Contato contato = tableModel.getContato(linhaModelo);

        ContatoDialog dialog = new ContatoDialog(this, contato);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            try {
                dao.atualizar(dialog.getContato());
                carregarContatos();
            } catch (SQLException e) {
                mostrarErro("Erro ao atualizar contato: " + e.getMessage());
            }
        }
    }

    // ── Excluir contato selecionado ───────────────────────────────────────────
    private void excluirContato() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um contato para excluir.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int linhaModelo = tabela.convertRowIndexToModel(linha);
        Contato contato = tableModel.getContato(linhaModelo);

        int resposta = JOptionPane.showConfirmDialog(this,
                "Excluir o contato \"" + contato.getNome() + "\"?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                dao.excluir(contato.getId());
                carregarContatos();
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir contato: " + e.getMessage());
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void atualizarStatus(int total) {
        lblStatus.setText(total + " contato" + (total != 1 ? "s" : "") + " encontrado" + (total != 1 ? "s" : ""));
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private JButton criarBotao(String texto, Color fg, Color bg) {
        JButton btn = new JButton(texto);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(85, 28));
        btn.setFont(btn.getFont().deriveFont(12f));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover (deixa um pouco mais claro)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(aplicarBrilho(bg, 1.15f));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }
    
    private Color aplicarBrilho(Color cor, float fator) {
        return new Color(
            Math.min(255, (int)(cor.getRed() * fator)),
            Math.min(255, (int)(cor.getGreen() * fator)),
            Math.min(255, (int)(cor.getBlue() * fator))
        );
    }
}
