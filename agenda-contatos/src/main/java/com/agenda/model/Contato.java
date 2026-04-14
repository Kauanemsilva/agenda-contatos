package com.agenda.model;

public class Contato {

    private int id;
    private String nome;
    private String telefone;
    private String email;
    private String grupo;

    public Contato() {}

    public Contato(String nome, String telefone, String email, String grupo) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.grupo = grupo;
    }

    public Contato(int id, String nome, String telefone, String email, String grupo) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.grupo = grupo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    @Override
    public String toString() {
        return nome;
    }
}
