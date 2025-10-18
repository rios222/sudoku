package com.example.sudoku6x6;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class View {
	private GridPane boardGrid;
	private Text title;
	
	public View(GridPane boardGrid, Text title) {
		this.boardGrid = boardGrid;
		this.title = title;
		
	}
		public Button getVerificarButton() {
			return new Button("Verificar");
		}

	// Metodo para obtener el valor de una celda en la vista
	public String getCellValue(int fila, int col) {
		TextField cell = getCell(fila, col);  // Obtén el TextField correspondiente
		return cell != null ? cell.getText() : "";  // Devuelve el texto o una cadena vacía si no se encuentra
	}
	
	// Metodo auxiliar privado para obtener la celda desde el GridPane
	private TextField getCell(int row, int col) {
		String cellId = "cell" + row + col;
		return (TextField) boardGrid.lookup("#" + cellId);
	}
	
	// Metodo para verificar si el Sudoku es válido
	public void verificarVictoria() {
		if (esSudokuValido()) {
			mostrarMensajeVictoria();  // Mostrar el mensaje de victoria
		} else {
			mostrarMensajeError();  // Mostrar un mensaje de error si el Sudoku no es válido
		}
	}
	
	//verifica si el tablero esta completo
	public boolean isBoardComplete() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (getCell(i, j).getText().isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}
	
	// Mostrar mensaje de victoria
	private void mostrarMensajeVictoria() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Victoria");
		alert.setHeaderText(null);
		alert.setContentText("¡Felicidades, has completado el Sudoku correctamente!");
		alert.showAndWait();
	}
	
	// Mostrar mensaje de error si el Sudoku no es válido
	private void mostrarMensajeError() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText("El Sudoku no es válido. Intenta nuevamente.");
		alert.showAndWait();
	}
	
	
	// Metodo para la validación de celdas
	public void setupCellValidation() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				TextField cell = getCell(i, j);
				if (cell != null) {
					cell.textProperty().addListener((observable, oldValue, newValue) -> {
						if (!newValue.isEmpty()) {  // Solo validar si hay contenido
							try {
								int num = Integer.parseInt(newValue);
								if (num < 1 || num > 6) {
									cell.setText(oldValue); // Revertir al valor anterior
									showErrorAlert("Error", "El número digitado no está entre 1 y 6");
								}
							} catch (NumberFormatException e) {
								cell.setText(oldValue); // revertir al valor anterior
								showErrorAlert("Entrada inválida", "Solo se permiten números del 1 al 6");
							}
						}
					});
				}
			}
		}
	}
	
	// Metodo para mostrar alertas de error
	private void showErrorAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	// Métodos para manipular celdas desde la view
	public void updateCell(int row, int col, String value) {
		TextField cell = getCell(row, col);
		if (cell != null) {
			cell.setText(value);
		}
	}
	
	public void setCellStyle(int row, int col, String style) {
		TextField cell = getCell(row, col);
		if (cell != null) {
			cell.setStyle(style);
		}
	}
	
	public void setCellEditable(int row, int col, boolean editable) {
		TextField cell = getCell(row, col);
		if (cell != null) {
			cell.setEditable(editable);
		}
	}
	
	// Métodos para el tablero completo
	public void clearBoard() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				updateCell(i, j, "");
				setCellStyle(i, j, "-fx-border-color: black;");
				setCellEditable(i, j, true);
			}
		}
	}
	
	public void setGenTablero(String[][] clues) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (clues[i][j] != null && !clues[i][j].isEmpty()) {
					updateCell(i, j, clues[i][j]);
					setCellStyle(i, j, "-fx-border-color: black; -fx-background-color: #f0f0f0;");
					setCellEditable(i, j, false);
				}
			}
		}
	}
	
	// Métodos para el título
	public void setTitle(String text) {
		if (title != null) {
			title.setText(text);
		}
	}
	
	// Metodo auxiliar para validar el Sudoku
	private boolean esSudokuValido() {
		// Implementa la lógica para validar si el Sudoku es válido
		return true;  // Retorna true si el Sudoku es válido, de lo contrario false
	}
}
