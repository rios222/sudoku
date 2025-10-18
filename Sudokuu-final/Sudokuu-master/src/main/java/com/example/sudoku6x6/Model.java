package com.example.sudoku6x6;

import java.util.*;

/**
 * Model del Sudoku 6x6 reescrito:
 * - genera tableros resolubles con exactamente 2 números por bloque 2x3
 * - comprueba unicidad de solución
 * - almacena la solución completa para usarla en la ayuda
 * - permite ingresar números (1-6) y devuelve si la entrada es válida
 */
public class Model {

    private final int SIZE = 6;
    private final int BLOCK_ROWS = 2;
    private final int BLOCK_COLS = 3;

    private int[][] board;             // Tablero actual (0 = vacío)
    private boolean[][] fixed;         // Celdas iniciales (no editables)
    private int[][] solucionCompleta;  // Solución completa generada (para ayuda)

    public Model() {
        board = new int[SIZE][SIZE];
        fixed = new boolean[SIZE][SIZE];
        solucionCompleta = null;
    }

    // ----------------------------
    // GENERACIÓN: solución y puzzle
    // ----------------------------

    /**
     * Genera un tablero Sudoku 6x6 que cumple:
     * - tiene exactamente 2 números por cada bloque 2x3
     * - el puzzle resultante tiene única solución
     *
     * Intenta varias veces (limite) si no encuentra unicidad.
     */
    public void generarTableroInicial() {
        final int MAX_INTENTOS = 2000;
        int intentos = 0;
        Random rnd = new Random();

        while (intentos < MAX_INTENTOS) {
            intentoGeneracionBasico();

            // Ahora board contiene la solución completa (resolverTableroCompleto puso números)
            // Guardamos la solución
            int[][] solucionGuard = copiarMatriz(board);

            // Construimos un puzzle que deje EXACTAMENTE 2 números por bloque 2x3
            int[][] puzzle = new int[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) Arrays.fill(puzzle[i], 0);

            // Para cada bloque, elegimos 2 posiciones para mantener
            for (int br = 0; br < SIZE; br += BLOCK_ROWS) {
                for (int bc = 0; bc < SIZE; bc += BLOCK_COLS) {
                    // Lista de posiciones dentro del bloque
                    List<int[]> posiciones = new ArrayList<>();
                    for (int r = br; r < br + BLOCK_ROWS; r++) {
                        for (int c = bc; c < bc + BLOCK_COLS; c++) {
                            posiciones.add(new int[]{r, c});
                        }
                    }
                    Collections.shuffle(posiciones, rnd);
                    // Tomamos las dos primeras
                    for (int k = 0; k < 2; k++) {
                        int[] p = posiciones.get(k);
                        puzzle[p[0]][p[1]] = solucionGuard[p[0]][p[1]];
                    }
                }
            }

            // Comprobar que puzzle tiene al menos 1 solución (sí tendrá) y preferimos única solución
            int soluciones = contarSoluciones(puzzle, 2); // contamos hasta 2
            if (soluciones == 1) {
                // Aceptamos este puzzle
                board = puzzle;
                solucionCompleta = solucionGuard;
                // Marcar fijas
                for (int r = 0; r < SIZE; r++) {
                    for (int c = 0; c < SIZE; c++) {
                        fixed[r][c] = (board[r][c] != 0);
                    }
                }
                return;
            }

            // Si no es único, intentamos otra vez
            intentos++;
        }

        // Si se agotaron intentos, caemos a un método mas simple
        // Generar solución completa y eliminar celdas aleatoriamente
        limpiarTablero();
        resolverTableroCompleto();
        solucionCompleta = copiarMatriz(board);
        eliminarCeldas(16 + new Random().nextInt(6)); // fallback
        for (int r = 0; r < SIZE; r++) for (int c = 0; c < SIZE; c++) fixed[r][c] = (board[r][c] != 0);
    }

    /**
     * Genera solución completa en 'board' (backtracking).
     * Este método rellena board; se usa por la generación
     */
    private void intentoGeneracionBasico() {
        // inicializar vacío
        for (int i = 0; i < SIZE; i++) Arrays.fill(board[i], 0);
        // Backtracking para llenar la solución
        resolverTableroCompleto();
    }

    /**
     * Rellena completamente un tablero válido usando backtracking.
     */
    private boolean resolverTableroCompleto() {
        for (int fila = 0; fila < SIZE; fila++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[fila][col] == 0) {
                    List<Integer> numeros = Arrays.asList(1,2,3,4,5,6);
                    Collections.shuffle(numeros);
                    for (int num : numeros) {
                        if (esValidoEn(tableroTemp(board), fila, col, num)) {
                            board[fila][col] = num;
                            if (resolverTableroCompleto()) return true;
                            board[fila][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // ----------------------------
    // VALIDACIÓN / HELPER de generación
    // ----------------------------

    /**
     * Comprueba si un número es válido en una posición para un tablero dado.
     * Utilidad interna para usar con copias temporales.
     */
    private boolean esValidoEn(int[][] tablero, int fila, int col, int num) {
        // Fila
        for (int c = 0; c < SIZE; c++) {
            if (tablero[fila][c] == num && c != col) return false;
        }
        // Columna
        for (int r = 0; r < SIZE; r++) {
            if (tablero[r][col] == num && r != fila) return false;
        }
        // Bloque
        int startRow = (fila / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                if (tablero[r][c] == num && (r != fila || c != col)) return false;
            }
        }
        return true;
    }

    /**
     * Copia profunda de un tablero (útil para pruebas).
     */
    private int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) copia[i] = Arrays.copyOf(original[i], SIZE);
        return copia;
    }

    /**
     * Construye y devuelve una copia del board (para pasar a comprobaciones sin tocar el original).
     */
    private int[][] tableroTemp(int[][] base) {
        return copiarMatriz(base);
    }

    /**
     * Elimina celdas aleatoriamente del tablero actual. (fallback)
     */
    private void eliminarCeldas(int cantidad) {
        Random random = new Random();
        int eliminadas = 0;
        while (eliminadas < cantidad) {
            int fila = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (board[fila][col] != 0) {
                board[fila][col] = 0;
                eliminadas++;
            }
        }
    }

    // ----------------------------
    // CONTADOR DE SOLUCIONES (unicidad)
    // ----------------------------

    /**
     * Cuenta soluciones del tablero dado hasta el límite maxCount (p. ej. 2).
     * Devuelve el número de soluciones encontradas (si encuentra >= maxCount corta y devuelve >= maxCount).
     */
    private int contarSoluciones(int[][] tableroInicial, int maxCount) {
        int[][] t = copiarMatriz(tableroInicial);
        return contarSolucionesRec(t, maxCount, 0);
    }

    private int contarSolucionesRec(int[][] tablero, int maxCount, int contadorActual) {
        if (contadorActual >= maxCount) return contadorActual;

        // buscar celda vacía
        int fila = -1, col = -1;
        outer:
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (tablero[r][c] == 0) {
                    fila = r; col = c;
                    break outer;
                }
            }
        }

        // si no hay vacías, encontramos una solución
        if (fila == -1) return contadorActual + 1;

        // intentar números
        for (int num = 1; num <= 6; num++) {
            if (esValidoEn(tablero, fila, col, num)) {
                tablero[fila][col] = num;
                contadorActual = contarSolucionesRec(tablero, maxCount, contadorActual);
                tablero[fila][col] = 0;
                if (contadorActual >= maxCount) return contadorActual;
            }
        }
        return contadorActual;
    }

    // ----------------------------
    // VALIDACIONES Y OPERACIONES PÚBLICAS
    // ----------------------------

    /**
     * Verifica si un número es válido en la posición actual del board (usa el board actual).
     * Pública: usada por la vista para validar en tiempo real.
     */
    public boolean esValido(int fila, int col, int num) {
        // Fila
        for (int c = 0; c < SIZE; c++) {
            if (board[fila][c] == num && c != col) return false;
        }
        // Columna
        for (int r = 0; r < SIZE; r++) {
            if (board[r][col] == num && r != fila) return false;
        }
        // Bloque
        int startRow = (fila / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                if (board[r][c] == num && (r != fila || c != col)) return false;
            }
        }
        return true;
    }

    /**
     * Intenta colocar un número en la celda (permite que el usuario escriba 1-6).
     * Devuelve true si la entrada es válida según reglas en ese momento, false si produce conflicto.
     * No modifica celdas fijas.
     */
    public boolean ingresarNumero(int fila, int col, int num) {
        if (fixed[fila][col]) return false;
        if (num < 1 || num > 6) return false;

        // Siempre colocamos el número (para que el usuario vea su entrada)
        board[fila][col] = num;
        // Retornamos si en este momento es válido
        return esValido(fila, col, num);
    }

    /**
     * Borra el número de la celda (si no es fija).
     */
    public boolean eliminarNumero(int fila, int col) {
        if (fixed[fila][col]) return false;
        board[fila][col] = 0;
        return true;
    }

    /**
     * Devuelve la lista de celdas que actualmente están en conflicto.
     * Cada elemento es un int[]{fila, col}
     */
    public List<int[]> obtenerCeldasErroneas() {
        List<int[]> errores = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int num = board[r][c];
                if (num != 0 && !esValido(r, c, num)) errores.add(new int[]{r, c});
            }
        }
        return errores;
    }

    /**
     * Devuelve true si el tablero actual está completo y válido.
     */
    public boolean tableroCompleto() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int num = board[r][c];
                if (num == 0 || !esValido(r, c, num)) return false;
            }
        }
        return true;
    }

    // ----------------------------
    // AYUDA (usando la solución completa guardada)
    // ----------------------------

    /**
     * Coloca automáticamente una sugerencia correcta en el tablero.
     * Sólo se permite si quedan más de una celda vacía.
     * Retorna true si colocó la ayuda, false si no pudo (p. ej. queda 1 o 0 vacías o no hay solución guardada).
     */
    public boolean usarAyuda() {
        // Debe existir una solución completa guardada
        if (solucionCompleta == null) return false;

        int vacias = 0;
        for (int r = 0; r < SIZE; r++) for (int c = 0; c < SIZE; c++) if (board[r][c] == 0) vacias++;

        if (vacias <= 1) return false; // no permitir ayuda si sólo queda 1

        // Elegir una celda vacía aleatoria y colocar la solución correspondiente
        List<int[]> vacantes = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) for (int c = 0; c < SIZE; c++) if (board[r][c] == 0) vacantes.add(new int[]{r, c});

        if (vacantes.isEmpty()) return false;

        Collections.shuffle(vacantes);
        for (int[] cel : vacantes) {
            int r = cel[0], c = cel[1];
            int valorCorrecto = solucionCompleta[r][c];
            // Colocar el valor correcto
            board[r][c] = valorCorrecto;
            // No lo marcamos fijo; el usuario podría borrarlo si lo desea
            return true;
        }
        return false;
    }

    // ----------------------------
    // UTILIDADES
    // ----------------------------

    /**
     * Limpia tablero y fijas.
     */
    public void limpiarTablero() {
        board = new int[SIZE][SIZE];
        fixed = new boolean[SIZE][SIZE];
        solucionCompleta = null;
    }

    public int[][] getTablero() { return board; }
    public boolean[][] getFijas() { return fixed; }
    public int getSize() { return SIZE; }
}
