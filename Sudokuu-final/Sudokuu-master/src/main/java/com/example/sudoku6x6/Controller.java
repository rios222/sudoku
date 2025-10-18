package com.example.sudoku6x6;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
 * Controller principal del Sudoku 6x6.
 * Maneja la interfaz, eventos y conexión con el Model.
 */
public class Controller {

    @FXML
    private GridPane gridSudoku;

    @FXML
    private Button btnNuevoJuego;

    @FXML
    private Button btnAyuda;

    @FXML
    private Label lblEstado;

    private Model model;
    private Label[][] celdas; // Representa las etiquetas visuales de las celdas
    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;

    // Lista con coordenadas (fila,col) de celdas colocadas por la ayuda, para colorearlas distinto
    private final List<int[]> celdasDeAyuda = new ArrayList<>();

    /**
     * Inicializa el controlador.
     */
    @FXML
    public void initialize() {
        model = new Model();
        // Si tu Model soporta getSize() dinámico puedes usarlo, aquí asumimos 6x6.
        celdas = new Label[6][6];
        crearTablero();
        configurarEventos();

        // Generar y mostrar un tablero al iniciar la aplicación
        model.generarTableroInicial();
        actualizarVista();
    }

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

                // Copiar índices en variables efectivamente finales para la lambda
                final int f = fila;
                final int c = col;
                celda.setOnMouseClicked(e -> seleccionarCelda(f, c));

                gridSudoku.add(celda, col, fila);
                celdas[fila][col] = lbl;
            }
        }
    }

    /**
     * Configura los eventos globales (teclado y botones).
     */
    private void configurarEventos() {
        // KeyTyped para los caracteres (números). Se requiere que el grid tenga foco (requestFocus).
        gridSudoku.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            if (filaSeleccionada == -1 || colSeleccionada == -1) return;

            String ch = e.getCharacter();
            if (ch.matches("[1-6]")) {
                int num = Integer.parseInt(ch);

                // colocar número y obtener si es válido (Model.ingresarNumero coloca y devuelve si es válido)
                boolean esValidoAhora = model.ingresarNumero(filaSeleccionada, colSeleccionada, num);

                if (!esValidoAhora) {
                    lblEstado.setText("⚠ Número inválido en esta posición.");
                } else {
                    lblEstado.setText("");
                }

                // Si el usuario escribió algo en una celda que había sido marcada por ayuda, la dejamos como "usuario" (no cambiar celdasDeAyuda aquí)
                actualizarVista();
            }
            // KeyTyped no captura Backspace, por eso manejamos borrado en KEY_PRESSED (ver abajo).
        });

        // KeyPressed para borrar (BACK_SPACE, DELETE) y otras teclas funcionales.
        gridSudoku.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (filaSeleccionada == -1 || colSeleccionada == -1) return;

            if (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE) {
                boolean ok = model.eliminarNumero(filaSeleccionada, colSeleccionada);
                if (ok) {
                    lblEstado.setText("");
                    // Si la celda estaba entre las celdas de ayuda, la removemos de esa lista (el usuario borró)
                    celdasDeAyuda.removeIf(p -> p[0] == filaSeleccionada && p[1] == colSeleccionada);
                }
                actualizarVista();
            }
        });

        // Botón nuevo juego
        btnNuevoJuego.setOnAction(e -> nuevoJuego());

        // Botón ayuda
        btnAyuda.setOnAction(e -> mostrarAyuda());
    }

    /**
     * Selecciona visualmente una celda.
     */
    private void seleccionarCelda(int fila, int col) {
        filaSeleccionada = fila;
        colSeleccionada = col;
        lblEstado.setText("Celda seleccionada: (" + (fila + 1) + ", " + (col + 1) + ")");
        // Pedir foco al grid para que reciba eventos de teclado
        gridSudoku.requestFocus();

        // Actualizar vista para que la celda seleccionada se muestre con el borde
        actualizarVista();
    }

    /**
     * Inicia un nuevo juego (HU-2).
     */
    private void nuevoJuego() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setHeaderText("¿Iniciar un nuevo juego?");
        alerta.setContentText("Se borrará el tablero actual.");
        alerta.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {
                // limpiar marcas de ayuda previas
                celdasDeAyuda.clear();
                model.generarTableroInicial();
                // reset selección
                filaSeleccionada = -1;
                colSeleccionada = -1;
                actualizarVista();
                lblEstado.setText("Nuevo juego iniciado.");
            }
        });
    }

    /**
     * Usa la opción de ayuda para colocar automáticamente una sugerencia válida.
     * No permite usarla si solo queda una celda vacía (HU-5).
     *
     * Detecta qué celda fue colocada comparando antes/después y la marca en celdasDeAyuda.
     */
    private void mostrarAyuda() {
        // Copia del tablero antes de la ayuda (deep copy)
        int[][] antes = copiarTablero(model.getTablero());

        boolean usada = model.usarAyuda();

        if (!usada) {
            lblEstado.setText("⚠️ No puedes usar más ayuda: solo queda una celda vacía o no hay opciones válidas.");
            return;
        }

        // buscar la diferencia entre 'antes' y 'despues' para saber qué celda colocó la ayuda
        int[][] despues = model.getTablero();
        boolean encontrada = false;
        for (int r = 0; r < 6 && !encontrada; r++) {
            for (int c = 0; c < 6; c++) {
                if (antes[r][c] == 0 && despues[r][c] != 0) {
                    // registrar esta celda como "colocada por ayuda"
                    celdasDeAyuda.add(new int[]{r, c});
                    encontrada = true;
                    break;
                }
            }
        }

        lblEstado.setText("💡 Se ha colocado una sugerencia automática en una celda vacía.");
        actualizarVista();
    }

    /**
     * Refresca toda la interfaz según el estado del modelo.
     * Aplica estilos visuales para celdas fijas, errores, celdas de ayuda y la celda seleccionada.
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

                // Estilo base
                lbl.setStyle("-fx-alignment: center; -fx-font-size: 18px;");

                // Fondo y texto base (por si no es fija ni error)
                StackPane parent = (StackPane) lbl.getParent();
                parent.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");

                // Color para celdas fijas
                if (fijas[fila][col]) {
                    lbl.setTextFill(Color.BLACK);
                    lbl.setStyle(lbl.getStyle() + "-fx-font-weight: bold;");
                    parent.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: gray;");
                } else {
                    lbl.setTextFill(Color.DARKGREEN);
                    lbl.setStyle(lbl.getStyle() + "-fx-font-weight: normal;");
                }

                // Copiar fila y col en variables efectivamente finales
                final int f = fila;
                final int c = col;

               // Resaltar si es celda colocada por la ayuda
                boolean esAyuda = celdasDeAyuda.stream().anyMatch(p -> p[0] == f && p[1] == c);
                if (esAyuda) {
                    parent.setStyle("-fx-background-color: #dfefff; -fx-border-color: #90b7ff;"); // azul claro
                    lbl.setTextFill(Color.DARKBLUE);
                }


                // Resaltar errores (tos los marcados por el model)
                boolean esError = errores.stream().anyMatch(p -> p[0] == f && p[1] == c);
                if (esError) {
                    lbl.setTextFill(Color.RED);
                    parent.setStyle("-fx-background-color: #ffd6d6; -fx-border-color: red;");
                }

                // Resaltar celda seleccionada (borde azul más claro)
                if (fila == filaSeleccionada && col == colSeleccionada) {
                    parent.setStyle("-fx-background-color: #d9ebff; -fx-border-color: #0066cc; -fx-border-width: 2;");
                }
            }
        }

        if (model.tableroCompleto()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("¡Sudoku Completado!");
            alerta.setHeaderText("¡Felicidades!");
            alerta.setContentText("Has completado correctamente el Sudoku 6x6.");
            alerta.showAndWait();

            lblEstado.setText("¡Juego completado!");
        }

    }

    // -------------------------
    // Utils
    // -------------------------

    private int[][] copiarTablero(int[][] src) {
        int size = src.length;
        int[][] copia = new int[size][size];
        for (int i = 0; i < size; i++) copia[i] = Arrays.copyOf(src[i], size);
        return copia;
    }
}
