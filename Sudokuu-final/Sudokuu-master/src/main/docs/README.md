# 🎮 Sudoku 6x6

Un juego de Sudoku interactivo con interfaz gráfica desarrollado en **Java** con **JavaFX**.

## 📋 Descripción

Este proyecto implementa un juego de Sudoku con tablero de 6x6 celdas organizadas en bloques de 2x3. Incluye generación automática de puzzles únicos, validación de reglas, sistema de ayuda y una interfaz gráfica intuitiva.

## ✨ Características

- ✅ Generación automática de Sudokus 6x6 con solución única
- ✅ Validación en tiempo real de las reglas del Sudoku
- ✅ Sistema de ayuda automática
- ✅ Detección de errores con resaltado en color
- ✅ Interfaz gráfica moderna con JavaFX
- ✅ Múltiples juegos (nuevo juego en cualquier momento)
- ✅ Celdas fijas no editables (precargas del puzzle)

## 🛠️ Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **JavaFX 17.0.6** - Framework para la interfaz gráfica
- **Maven 3.9+** - Gestión de dependencias
- **JUnit 5.10.2** - Framework de pruebas

## 📦 Requisitos Previos

- Java Development Kit (JDK) 17 o superior
- Maven 3.6 o superior
- Git (opcional, para clonar el repositorio)

## 🚀 Instalación y Ejecución

### 1. Clonar o descargar el proyecto

```bash
git clone https://github.com/rios222/sudoku.git
cd Sudokuu-final/Sudokuu-master
```

### 2. Compilar el proyecto

```bash
mvn clean compile
```

### 3. Ejecutar la aplicación

```bash
mvn javafx:run
```

O compilar y ejecutar directamente:

```bash
mvn clean javafx:run
```

## 🎮 Cómo Jugar

### Objetivo
Llenar todas las celdas vacías con números del 1 al 6, respetando las reglas del Sudoku.

### Reglas del Sudoku 6x6
- Cada **fila** debe contener los números 1-6 sin repetir
- Cada **columna** debe contener los números 1-6 sin repetir
- Cada **bloque 2x3** debe contener los números 1-6 sin repetir

### Controles
| Acción | Cómo | Efecto |
|--------|------|--------|
| Seleccionar celda | Clic del ratón | Marca la celda para editar |
| Ingresar número | Teclas 1-6 | Coloca el número en la celda seleccionada |
| Borrar número | BACKSPACE o DELETE | Elimina el número de la celda seleccionada |
| Nueva partida | Botón "Nuevo Juego" | Genera un nuevo Sudoku (requiere confirmación) |
| Obtener ayuda | Botón "Ayuda" | Coloca un número correcto en una celda vacía |

### Indicadores Visuales
- 🟩 **Gris**: Celdas iniciales (no editables)
- 🟩 **Verde oscuro**: Números ingresados por el jugador
- 🟥 **Rojo**: Número que viola las reglas
- 🟦 **Azul claro**: Número sugerido por la ayuda
- 🟦 **Azul oscuro**: Celda actualmente seleccionada

## 📁 Estructura del Proyecto

```
Sudokuu-final/
├── Sudokuu-master/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/sudoku6x6/
│   │   │   │   ├── App.java                 (Punto de entrada)
│   │   │   │   ├── Controller.java          (Lógica de interfaz)
│   │   │   │   ├── MenuController.java      (Controlador del menú)
│   │   │   │   └── Model.java               (Lógica del juego)
│   │   │   └── view/
│   │   │       ├── com/example/sudoku6x6/
│   │   │       │   ├── menu_main.fxml       (Menú principal)
│   │   │       │   └── sudoku_main.fxml     (Interfaz del juego)
│   │   │       └── images/
│   │   │           └── sudoku.gif           (Imagen decorativa)
│   │   └── test/
│   ├── pom.xml                              (Configuración Maven)
│   └── target/                              (Compilados)
├── docs/
│   ├── JAVADOC.md                          (Documentación técnica)
│   ├── INSTALACION.md                      (Guía detallada de instalación)
│   └── COMO_JUGAR.md                       (Instrucciones del juego)
└── README.md                                (Este archivo)
```

## 📚 Documentación

- 📖 **[Javadoc Completo](./docs/JAVADOC.md)** - Documentación técnica de todas las clases
- 🔧 **[Guía de Instalación](./docs/INSTALACION.md)** - Pasos detallados para instalar
- 🎮 **[Cómo Jugar](./docs/COMO_JUGAR.md)** - Tutorial completo del juego

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Model-View-Controller)**:

### Model (Lógica)
- **Model.java**: Gestiona la lógica del Sudoku
    - Generación de puzzles únicos
    - Validación de movimientos
    - Detección de errores
    - Sistema de ayuda

### View (Presentación)
- **menu_main.fxml**: Interfaz del menú principal
- **sudoku_main.fxml**: Interfaz del tablero de juego
- **Controller.java**: Maneja la presentación e interacción

### Controller (Mediador)
- **MenuController.java**: Transición menu → juego
- **Controller.java**: Coordina Model y View

## 🔄 Flujo de la Aplicación

```
App.java (inicio)
    ↓
MenuController (menú principal)
    ↓ [Clic en "Comenzar el Juego"]
Controller (interfaz de juego)
    ↓
Model (lógica del Sudoku)
    ↓ [Interacciones del usuario]
actualizarVista() → Actualiza la visualización
```

## 🎯 Algoritmos Principales

### Generación de Sudoku
1. Genera una solución completa mediante backtracking
2. Extrae 2 números por bloque 2x3
3. Verifica que la solución sea única
4. Si falla, usa método alternativo de respaldo

### Validación
- Verifica reglas por fila, columna y bloque
- Detecta conflictos en tiempo real
- Impide ingresar números inválidos

### Conteo de Soluciones
- Algoritmo recursivo con backtracking
- Garantiza unicidad del puzzle
- Limita búsqueda a máximo 2 soluciones

## 🐛 Solución de Problemas

| Problema | Solución |
|----------|----------|
| No se ejecuta la aplicación | Verifica que tengas Java 17+ instalado: `java -version` |
| Error al compilar | Ejecuta `mvn clean` primero, luego `mvn compile` |
| JavaFX no se encuentra | Asegúrate de tener las dependencias en pom.xml |
| El juego se ve pixelado | Aumenta el tamaño de la ventana |

## 👨‍💻 Autores

- Desarrollador: Daniel Rios ([@rios222](https://github.com/rios222))

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Ver archivo `LICENSE` para más detalles.

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Contacto

- GitHub: [@rios222](https://github.com/rios222)
- Email: daniel.rios@example.com

## 🎓 Referencias

- [Documentación JavaFX](https://openjfx.io/)
- [Tutorial de Sudoku](https://sudoku.com/es/rules/sudoku/)
- [Patrones de Diseño Java](https://www.geeksforgeeks.org/design-patterns-in-java/)

---

**Versión:** 1.0-SNAPSHOT  
**Última actualización:** 2025  
**Estado:** ✅ Funcional