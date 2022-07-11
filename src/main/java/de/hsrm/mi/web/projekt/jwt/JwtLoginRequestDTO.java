package de.hsrm.mi.web.projekt.jwt;

/**
 * JwtLoginRequest - einfache Java-Datenklasse, um vom
 * Vue-Frontend (doLogin()) gelieferte JSON-Struktur
 * { 'username': '...', 'password': '...' }
 * aufzunehmen.
 */
public record JwtLoginRequestDTO(String username, String password) {};
