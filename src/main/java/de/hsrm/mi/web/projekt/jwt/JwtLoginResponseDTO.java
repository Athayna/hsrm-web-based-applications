package de.hsrm.mi.web.projekt.jwt;

/**
 * JwtLoginRequest - einfache Java-Datenklasse, um vom
 * Vue-Frontend (doLogin()) gelieferte JSON-Struktur
 * { 'username': '...', 'password': '...' }
 * aufzunehmen.
 */
public record JwtLoginResponseDTO(String username, String name, Long benutzerprofilid, String jwtToken) {};
