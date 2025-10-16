package com.example.employees.exception;

import java.time.Instant;
import java.util.List;

/**
 * Representa una respuesta estándar de error para la API REST.
 * Incluye el código de estado HTTP, el mensaje de error,
 * una lista de detalles y la marca de tiempo del error.
 *
 * @param status    El código de estado HTTP (ej. 404, 500)
 * @param error     Breve descripción o nombre del error
 * @param details   Lista de mensajes de detalle adicionales
 * @param timestamp Momento exacto en que ocurrió el error
 */
public record ErrorResponse(
    int status,            // Código de estado HTTP (por ejemplo, 404, 500)
    String error,          // Descripción breve del error
    List<String> details,  // Lista de detalles o mensajes adicionales
    Instant timestamp      // Fecha y hora de ocurrencia del error
) {}


