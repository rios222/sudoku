package com.example.sudoku6x6;

import java.util.*;

/**
 * Clase {@code Model} que gestiona toda la lógica del juego Sudoku 6x6.
 * <p>
 * Esta clase se encarga de:
 * <ul>
 *   <li>Generar tableros válidos y resolubles.</li>
 *   <li>Garantizar que cada tablero tenga una única solución.</li>
 *   <li>Validar los movimientos del jugador según las reglas del Sudoku.</li>
 *   <li>Proporcionar una ayuda automática basada en la solución completa.</li>
 *   <li>Detectar errores, verificar la finalización del juego y manejar celdas fijas.</li>
 * </ul>
 *
 * El Sudoku generado tiene bloques de 2x3 celdas (6x6 total) y se construye
 * con exactamente dos números por bloque.
 */
public class Model {

    /** Tamaño total del tablero (6x6). */
    private final int SIZE = 6;

    /** Cantidad de filas por bloque (2). */
    private final int BLOCK_ROWS = 2;

    /** Cantidad de columnas por bloque (3). */
    private final int BLOCK_COLS = 3;

    /** Representa el tablero actual del Sudoku (0 indica celda vacía). */
    private int[][] board;

    /** Indica qué celdas son fijas (no editables por el jugador). */
    private boolean[][] fixed;

    /** Solución completa generada, usada para validaciones y ayuda. */
    private int[][] solucionCompleta;

    /**
     * Constructor por defecto.
     * Inicializa el tablero, las celdas fijas y la solución vacía.
     */
    public Model() {
        board = new int[SIZE][SIZE];
        fixed = new boolean[SIZE][SIZE];
        solucionCompleta = null;
    }

    // ----------------------------------------------------------
    // GENERACIÓN DEL TABLERO
    // ----------------------------------------------------------

    /**
     * Genera un tablero Sudoku 6x6 válido con las siguientes condiciones:
     * <ul>
     *   <li>Cada bloque 2x3 contiene exactamente dos números visibles.</li>
     *   <li>El tablero tiene una única solución válida.</li>
     * </ul>
     * Si no logra generar un tablero único tras varios intentos, utiliza un método
     * alternativo de respaldo que garantiza un tablero resoluble (aunque no perfecto).
     */
    public void generarTableroInicial() {
        final int MAX_INTENTOS = 2000;
        int intentos = 0;
        Random rnd = new Random();

        while (intentos < MAX_INTENTOS) {
            intentoGeneracionBasico();

            // Guardar la solución completa generada
            int[][] solucionGuard = copiarMatriz(board);

            // Construir puzzle con 2 números por bloque 2x3
            int[][] puzzle = new int[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) Arrays.fill(puzzle[i], 0);

            for (int br = 0; br < SIZE; br += BLOCK_ROWS) {
                for (int bc = 0; bc < SIZE; bc += BLOCK_COLS) {
                    List<int[]> posiciones = new ArrayList<>();
                    for (int r = br; r < br + BLOCK_ROWS; r++) {
                        for (int c = bc; c < bc + BLOCK_COLS; c++) {
                            posiciones.add(new int[]{r, c});
                        }
                    }
                    Collections.shuffle(posiciones, rnd);
                    for (int k = 0; k < 2; k++) {
                        int[] p = posiciones.get(k);
                        puzzle[p[0]][p[1]] = solucionGuard[p[0]][p[1]];
                    }
                }
            }

            // Verificar unicidad de solución
            int soluciones = contarSoluciones(puzzle, 2);
            if (soluciones == 1) {
                board = puzzle;
                solucionCompleta = solucionGuard;
                for (int r = 0; r < SIZE; r++) {
                    for (int c = 0; c < SIZE; c++) {
                        fixed[r][c] = (board[r][c] != 0);
                    }
                }
                return;
            }
            intentos++;
        }

        // Método de respaldo si falla la generación única
        limpiarTablero();
        resolverTableroCompleto();
        solucionCompleta = copiarMatriz(board);
        eliminarCeldas(16 + new Random().nextInt(6));
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                fixed[r][c] = (board[r][c] != 0);
    }

    /**
     * Reinicia el tablero y genera una solución completa válida mediante backtracking.
     */
    private void intentoGeneracionBasico() {
        for (int i = 0; i < SIZE; i++) Arrays.fill(board[i], 0);
        resolverTableroCompleto();
    }

    /**
     * Algoritmo de backtracking que genera una solución completa de Sudoku 6x6.
     *
     * @return {@code true} si se logró completar el tablero correctamente.
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

    // ----------------------------------------------------------
    // VALIDACIONES INTERNAS
    // ----------------------------------------------------------

    /**
     * Comprueba si un número puede colocarse en una posición determinada sin
     * violar las reglas del Sudoku (filas, columnas o bloques).
     *
     * @param tablero tablero sobre el cual validar.
     * @param fila    fila donde se intenta colocar el número.
     * @param col     columna donde se intenta colocar el número.
     * @param num     número a verificar (1-6).
     * @return {@code true} si el número puede colocarse, {@code false} si genera conflicto.
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
        // Bloque 2x3
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
     * Crea una copia profunda del tablero dado.
     *
     * @param original tablero original.
     * @return copia independiente del tablero.
     */
    private int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) copia[i] = Arrays.copyOf(original[i], SIZE);
        return copia;
    }

    /**
     * Devuelve una copia del tablero base (usado para pruebas temporales).
     *
     * @param base tablero base.
     * @return copia del tablero.
     */
    private int[][] tableroTemp(int[][] base) {
        return copiarMatriz(base);
    }

    /**
     * Elimina celdas aleatoriamente del tablero actual.
     * Se usa como método alternativo cuando falla la generación principal.
     *
     * @param cantidad número de celdas a eliminar.
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

    // ----------------------------------------------------------
    // CONTEO DE SOLUCIONES (unicidad)
    // ----------------------------------------------------------

    /**
     * Cuenta cuántas soluciones tiene un tablero parcial hasta un máximo dado.
     * Se usa para garantizar la unicidad del Sudoku generado.
     *
     * @param tableroInicial tablero parcial a evaluar.
     * @param maxCount número máximo de soluciones a contar antes de detenerse.
     * @return número de soluciones encontradas (cortará al llegar a {@code maxCount}).
     */
    private int contarSoluciones(int[][] tableroInicial, int maxCount) {
        int[][] t = copiarMatriz(tableroInicial);
        return contarSolucionesRec(t, maxCount, 0);
    }

    /**
     * Método recursivo que explora todas las posibles soluciones del tablero.
     *
     * @param tablero tablero actual.
     * @param maxCount máximo número de soluciones que se desea encontrar.
     * @param contadorActual contador de soluciones encontradas hasta el momento.
     * @return cantidad total de soluciones encontradas (hasta el máximo permitido).
     */
    private int contarSolucionesRec(int[][] tablero, int maxCount, int contadorActual) {
        if (contadorActual >= maxCount) return contadorActual;

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

        if (fila == -1) return contadorActual + 1;

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

    // ----------------------------------------------------------
    // VALIDACIONES PÚBLICAS / OPERACIONES DE JUEGO
    // ----------------------------------------------------------

    /**
     * Verifica si un número puede colocarse en la posición indicada del tablero actual.
     *
     * @param fila fila de la celda.
     * @param col columna de la celda.
     * @param num número a validar.
     * @return {@code true} si el número cumple las reglas del Sudoku.
     */
    public boolean esValido(int fila, int col, int num) {
        return esValidoEn(board, fila, col, num);
    }

    /**
     * Intenta ingresar un número en una celda del tablero.
     * Si la celda es fija o el número es inválido, no se modifica.
     *
     * @param fila fila de la celda.
     * @param col columna de la celda.
     * @param num número a colocar (1–6).
     * @return {@code true} si la jugada es válida, {@code false} si genera conflicto.
     */
    public boolean ingresarNumero(int fila, int col, int num) {
        if (fixed[fila][col]) return false;
        if (num < 1 || num > 6) return false;
        board[fila][col] = num;
        return esValido(fila, col, num);
    }

    /**
     * Elimina el número de una celda (si no es fija).
     *
     * @param fila fila de la celda.
     * @param col columna de la celda.
     * @return {@code true} si se eliminó correctamente, {@code false} si es fija.
     */
    public boolean eliminarNumero(int fila, int col) {
        if (fixed[fila][col]) return false;
        board[fila][col] = 0;
        return true;
    }

    /**
     * Devuelve una lista con las coordenadas de todas las celdas erróneas,
     * es decir, aquellas que violan las reglas del Sudoku actual.
     *
     * @return lista de celdas en conflicto (pares [fila, columna]).
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
     * Comprueba si el tablero está completamente lleno y cumple todas las reglas.
     *
     * @return {@code true} si el Sudoku está completo y válido.
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

    // ----------------------------------------------------------
    // SISTEMA DE AYUDA
    // ----------------------------------------------------------

    /**
     * Coloca automáticamente un número correcto en una celda vacía aleatoria,
     * usando la solución completa guardada.
     * <p>
     * Solo se permite si hay más de una celda vacía.
     *
     * @return {@code true} si se colocó una ayuda, {@code false} si no fue posible.
     */
    public boolean usarAyuda() {
        if (solucionCompleta == null) return false;

        int vacias = 0;
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (board[r][c] == 0) vacias++;

        if (vacias <= 1) return false;

        List<int[]> vacantes = new ArrayList<>();
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (board[r][c] == 0) vacantes.add(new int[]{r, c});

        if (vacantes.isEmpty()) return false;

        Collections.shuffle(vacantes);
        for (int[] cel : vacantes) {
            int r = cel[0], c = cel[1];
            int valorCorrecto = solucionCompleta[r][c];
            board[r][c] = valorCorrecto;
            return true;
        }
        return false;
    }

    // ----------------------------------------------------------
    // UTILIDADES Y GETTERS
    // ----------------------------------------------------------

    /**
     * Limpia completamente el tablero y las celdas fijas.
     * Deja el modelo listo para generar un nuevo Sudoku.
     */
    public void limpiarTablero() {
        board = new int[SIZE][SIZE];
        fixed = new boolean[SIZE][SIZE];
        solucionCompleta = null;
    }

    /** @return el tablero actual del Sudoku. */
    public int[][] getTablero() { return board; }

    /** @return matriz que indica las celdas fijas (true = fija, false = editable). */
    public boolean[][] getFijas() { return fixed; }

    /** @return tamaño del tablero (6). */
    public int getSize() { return SIZE; }
}
