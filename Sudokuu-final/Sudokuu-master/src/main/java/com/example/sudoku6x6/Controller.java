package com.example.sudoku6x6;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controlador principal del juego Sudoku 6x6.
 *
 * Administra la interfaz grafica, los eventos del usuario y la comunicacion con la clase Model.
 * Se encarga de crear el tablero, manejar las entradas del teclado, mostrar ayudas,
 * y actualizar la vista segun el estado del juego.
 */
public class Controller {

    /** Contenedor grafico que representa el tablero del Sudoku. */
    @FXML
    private GridPane gridSudoku;

    /** Boton para iniciar un nuevo juego. */
    @FXML
    private Button btnNuevoJuego;

    /** Boton que muestra una ayuda automatica. */
    @FXML
    private Button btnAyuda;

    /** Etiqueta que muestra mensajes o el estado del juego. */
    @FXML
    private Label lblEstado;

    /** Instancia del modelo logico del Sudoku. */
    private Model model;

    /** Matriz de etiquetas que representan visualmente cada celda del tablero. */
    private Label[][] celdas;

    /** Fila actualmente seleccionada por el usuario. */
    private int filaSeleccionada = -1;

    /** Columna actualmente seleccionada por el usuario. */
    private int colSeleccionada = -1;

    /** Lista de coordenadas de las celdas completadas mediante la ayuda automatica. */
    private final List<int[]> celdasDeAyuda = new ArrayList<>();

    /**
     * Inicializa el controlador y la interfaz grafica.
     * Crea el tablero, configura los eventos y genera un tablero inicial al iniciar la aplicacion.
     */
    @FXML
    public void initialize() {
        model = new Model();
        celdas = new Label[6][6];
        crearTablero();
        configurarEventos();
        model.generarTableroInicial();
        actualizarVista();
    }

    /**
     * Crea visualmente el tablero 6x6 dentro del GridPane.
     * Cada celda contiene un rectangulo de fondo y una etiqueta para mostrar el numero.
     */
    private void crearTablero() {
        gridSudoku.getChildren().clear();
        gridSudoku.setAlignment(Pos.CENTER);
        gridSudoku.setGridLinesVisible(true);

        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 6; col++) {
                StackPane celda = new StackPane();
                Rectangle fondo = new Rectangle(60, 60);
                fondo.setFill(Color.WHITE);
                fondo.setStroke(Color.GRAY);

                Label lbl = new Label("");
                lbl.setFont(new Font(18));

                celda.getChildren().addAll(fondo, lbl);

                final int f = fila;
                final int c = col;
                celda.setOnMouseClicked(e -> seleccionarCelda(f, c));

                gridSudoku.add(celda, col, fila);
                celdas[fila][col] = lbl;
            }
        }
    }

    /**
     * Configura los eventos del teclado y los botones.
     * Maneja la entrada de numeros, el borrado de celdas y las acciones de los botones.
     */
    private void configurarEventos() {
        // Evento para ingreso de numeros
        gridSudoku.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            if (filaSeleccionada == -1 || colSeleccionada == -1) return;

            String ch = e.getCharacter();
            if (ch.matches("[1-6]")) {
                int num = Integer.parseInt(ch);
                boolean esValidoAhora = model.ingresarNumero(filaSeleccionada, colSeleccionada, num);

                if (!esValidoAhora) {
                    lblEstado.setText(" Numero invalido en esta posicion.");
                } else {
                    lblEstado.setText("");
                }
                actualizarVista();
            }
        });

        // Evento para borrar numeros
        gridSudoku.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (filaSeleccionada == -1 || colSeleccionada == -1) return;

            if (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE) {
                boolean ok = model.eliminarNumero(filaSeleccionada, colSeleccionada);
                if (ok) {
                    lblEstado.setText("");
                    celdasDeAyuda.removeIf(p -> p[0] == filaSeleccionada && p[1] == colSeleccionada);
                }
                actualizarVista();
            }
        });

        btnNuevoJuego.setOnAction(e -> nuevoJuego());
        btnAyuda.setOnAction(e -> mostrarAyuda());
    }

    /**
     * Marca una celda como seleccionada para permitir la edicion.
     *
     * @param fila fila seleccionada por el usuario
     * @param col columna seleccionada por el usuario
     */
    private void seleccionarCelda(int fila, int col) {
        filaSeleccionada = fila;
        colSeleccionada = col;
        lblEstado.setText("Celda seleccionada: (" + (fila + 1) + ", " + (col + 1) + ")");
        gridSudoku.requestFocus();
        actualizarVista();
    }

    /**
     * Inicia un nuevo juego.
     * Limpia el tablero y genera un nuevo Sudoku aleatorio.
     */
    private void nuevoJuego() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setHeaderText("¿Iniciar un nuevo juego?");
        alerta.setContentText("Se borrara el tablero actual.");
        alerta.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                celdasDeAyuda.clear();
                model.generarTableroInicial();
                filaSeleccionada = -1;
                colSeleccionada = -1;
                actualizarVista();
                lblEstado.setText("Nuevo juego iniciado.");
            }
        });
    }

    /**
     * Coloca automaticamente una sugerencia valida en una celda vacia.
     * Si solo queda una celda vacia, la ayuda no se aplica.
     */
    private void mostrarAyuda() {
        int[][] antes = copiarTablero(model.getTablero());
        boolean usada = model.usarAyuda();

        if (!usada) {
            lblEstado.setText(" No puedes usar mas ayuda: solo queda una celda vacia o no hay opciones validas.");
            return;
        }

        int[][] despues = model.getTablero();
        boolean encontrada = false;
        for (int r = 0; r < 6 && !encontrada; r++) {
            for (int c = 0; c < 6; c++) {
                if (antes[r][c] == 0 && despues[r][c] != 0) {
                    celdasDeAyuda.add(new int[]{r, c});
                    encontrada = true;
                    break;
                }
            }
        }

        lblEstado.setText(" Se ha colocado una sugerencia automatica en una celda vacia.");
        actualizarVista();
    }

    /**
     * Actualiza visualmente el tablero segun el estado actual del modelo.
     * Aplica colores y estilos para celdas fijas, errores, ayudas y seleccionadas.
     */
    private void actualizarVista() {
        int[][] tablero = model.getTablero();
        boolean[][] fijas = model.getFijas();
        List<int[]> errores = model.obtenerCeldasErroneas();

        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 6; col++) {
                Label lbl = celdas[fila][col];
                int valor = tablero[fila][col];
                lbl.setText(valor == 0 ? "" : String.valueOf(valor));
                lbl.setStyle("-fx-alignment: center; -fx-font-size: 18px;");
                StackPane parent = (StackPane) lbl.getParent();
                parent.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");

                if (fijas[fila][col]) {
                    lbl.setTextFill(Color.BLACK);
                    lbl.setStyle(lbl.getStyle() + "-fx-font-weight: bold;");
                    parent.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: gray;");
                } else {
                    lbl.setTextFill(Color.DARKGREEN);
                    lbl.setStyle(lbl.getStyle() + "-fx-font-weight: normal;");
                }

                final int f = fila;
                final int c = col;

                boolean esAyuda = celdasDeAyuda.stream().anyMatch(p -> p[0] == f && p[1] == c);
                if (esAyuda) {
                    parent.setStyle("-fx-background-color: #dfefff; -fx-border-color: #90b7ff;");
                    lbl.setTextFill(Color.DARKBLUE);
                }

                boolean esError = errores.stream().anyMatch(p -> p[0] == f && p[1] == c);
                if (esError) {
                    lbl.setTextFill(Color.RED);
                    parent.setStyle("-fx-background-color: #ffd6d6; -fx-border-color: red;");
                }

                if (fila == filaSeleccionada && col == colSeleccionada) {
                    parent.setStyle("-fx-background-color: #d9ebff; -fx-border-color: #0066cc; -fx-border-width: 2;");
                }
            }
        }

        if (model.tableroCompleto()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sudoku Completado");
            alerta.setHeaderText("Felicidades");
            alerta.setContentText("Has completado correctamente el Sudoku 6x6.");
            alerta.showAndWait();
            lblEstado.setText("Juego completado.");
        }
    }

    /**
     * Crea una copia del tablero actual para comparar antes y despues de aplicar una ayuda.
     *
     * @param src tablero original
     * @return copia independiente del tablero original
     */
    private int[][] copiarTablero(int[][] src) {
        int size = src.length;
        int[][] copia = new int[size][size];
        for (int i = 0; i < size; i++) copia[i] = Arrays.copyOf(src[i], size);
        return copia;
    }
}
