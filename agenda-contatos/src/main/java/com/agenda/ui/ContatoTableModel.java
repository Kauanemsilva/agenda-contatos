package com.agenda.ui;

import com.agenda.model.Contato;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ContatoTableModel extends AbstractTableModel {

    private static final String[] COLUNAS = {"#", "Nome", "Telefone", "E-mail", "Grupo"};

    private List<Contato> contatos = new ArrayList<>();

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
        fireTableDataChanged();
    }

    public Contato getContato(int linha) {
        return contatos.get(linha);
    }

    @Override
    public int getRowCount() {
        return contatos.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLUNAS[col];
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Contato contato = contatos.get(linha);
        switch (coluna) {
            case 0:
                return contato.getId();
            case 1:
                return contato.getNome();
            case 2:
                return contato.getTelefone();
            case 3:
                return contato.getEmail();
            case 4:
                return contato.getGrupo();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        return col == 0 ? Integer.class : String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
