package com.agenda.dao;

import com.agenda.model.Contato;
import com.agenda.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {

    // ── Inserir ──────────────────────────────────────────────────────────────
    public void inserir(Contato contato) throws SQLException {
        String sql = "INSERT INTO contatos (nome, telefone, email, grupo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());
            stmt.setString(4, contato.getGrupo());
            stmt.executeUpdate();
        }
    }

    // ── Atualizar ─────────────────────────────────────────────────────────────
    public void atualizar(Contato contato) throws SQLException {
        String sql = "UPDATE contatos SET nome=?, telefone=?, email=?, grupo=? WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());
            stmt.setString(4, contato.getGrupo());
            stmt.setInt(5, contato.getId());
            stmt.executeUpdate();
        }
    }

    // ── Excluir ───────────────────────────────────────────────────────────────
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM contatos WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ── Listar todos ──────────────────────────────────────────────────────────
    public List<Contato> listarTodos() throws SQLException {
        String sql = "SELECT * FROM contatos ORDER BY nome COLLATE NOCASE";
        List<Contato> lista = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Buscar por nome ───────────────────────────────────────────────────────
    public List<Contato> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM contatos WHERE nome LIKE ? ORDER BY nome COLLATE NOCASE";
        List<Contato> lista = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Buscar por grupo ──────────────────────────────────────────────────────
    public List<Contato> buscarPorGrupo(String grupo) throws SQLException {
        if (grupo.equals("Todos")) return listarTodos();

        String sql = "SELECT * FROM contatos WHERE grupo=? ORDER BY nome COLLATE NOCASE";
        List<Contato> lista = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, grupo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Contar registros ──────────────────────────────────────────────────────
    public int contar() throws SQLException {
        String sql = "SELECT COUNT(*) FROM contatos";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ── Mapear ResultSet → Contato ────────────────────────────────────────────
    private Contato mapear(ResultSet rs) throws SQLException {
        return new Contato(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("grupo")
        );
    }
}
