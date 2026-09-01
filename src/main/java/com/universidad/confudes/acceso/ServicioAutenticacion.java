package com.universidad.confudes.acceso;

import org.springframework.stereotype.Service;

// Simula la verificación de rol de un usuario autenticado.
@Service
public class ServicioAutenticacion {
    public boolean tienePermiso(String participanteId, String rolRequerido) {
        // Simulación: los IDs que empiezan con "ADMIN-" tienen
        // permiso de "STAFF"; el resto no.
        if (rolRequerido.equals("STAFF")) {
            return participanteId.startsWith("ADMIN-");
        }
        return true;
    }
}