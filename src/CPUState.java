
    /**
 * Enum que representa los estados del ciclo de instrucción.
 */
public enum CPUState {
    FETCH_REQUEST,    // CPU solicita instrucción
    MEMORY_ACCESS,    // Esperando respuesta de la RAM (ocurre el cuello de botella)
    FETCH_RESPONSE,   // Dato viajando de vuelta a la CPU
    DECODE_EXECUTE,   // CPU procesando internamente
    IDLE              // Sistema detenido
}