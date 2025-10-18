# 🎮 Cómo Jugar - Sudoku 6x6

Tutorial completo para jugar al Sudoku 6x6. Aprenderás las reglas, controles y estrategias.

---

## 📚 ¿Qué es el Sudoku?

Sudoku es un juego de lógica y razonamiento donde debes llenar una cuadrícula con números siguiendo ciertas reglas. En este caso, utilizamos un tablero de **6x6** en lugar del tradicional 9x9.

---

## 📐 Reglas Básicas del Sudoku 6x6

### Regla 1: Números en las Filas
Cada fila debe contener los números **1, 2, 3, 4, 5, 6** sin repetir.

```
Ejemplo de fila válida: | 1 | 2 | 3 | 4 | 5 | 6 |
Ejemplo inválido:      | 1 | 2 | 3 | 4 | 5 | 1 |  ❌ (el 1 se repite)
```

### Regla 2: Números en las Columnas
Cada columna debe contener los números **1, 2, 3, 4, 5, 6** sin repetir.

```
Columna 1 debe tener: 1, 2, 3, 4, 5, 6 (en cualquier orden)
```

### Regla 3: Números en los Bloques 2x3
El tablero de 6x6 se divide en **6 bloques de 2 filas × 3 columnas**. Cada bloque debe contener los números **1, 2, 3, 4, 5, 6** sin repetir.

```
Distribución de bloques:

┌─────────────┬─────────────┐
│ Bloque 1    │ Bloque 2    │
│ (2x3)       │ (2x3)       │
├─────────────┼─────────────┤
│ Bloque 3    │ Bloque 4    │
│ (2x3)       │ (2x3)       │
├─────────────┼─────────────┤
│ Bloque 5    │ Bloque 6    │
│ (2x3)       │ (2x3)       │
└─────────────┴─────────────┘
```

---

## 🎯 Objetivo del Juego

**Llenar todas las celdas vacías (en blanco) con números del 1 al 6**, cumpliendo con las tres reglas anteriores.

---

## 🖥️ Interfaz del Juego

### Vista Principal

```
╔════════════════════════════════════╗
║        Sudoku 6x6                  ║
║                                    ║
║  ┌──────────────────────────────┐  ║
║  │  [1] [ ] [3] | [ ] [ ] [2]   │  ║
║  │  [ ] [4] [ ] | [5] [ ] [ ]   │  ║
║  ├──────────────┼────────────────┤  ║
║  │  [ ] [ ] [6] | [1] [ ] [ ]   │  ║
║  │  [2] [ ] [ ] | [ ] [3] [ ]   │  ║
║  ├──────────────┼────────────────┤  ║
║  │  [ ] [5] [ ] | [ ] [ ] [4]   │  ║
║  │  [ ] [ ] [ ] | [ ] [6] [ ]   │  ║
║  └──────────────────────────────┘  ║
║                                    ║
║  [ Nuevo Juego ]  [ Ayuda ]       ║
║                                    ║
║  Celda seleccionada: (1, 1)       ║
╚════════════════════════════════════╝
```

### Elementos

| Elemento | Descripción |
|----------|-------------|
| Tablero 6x6 | Área principal con celdas |
| Números grises | Números iniciales (no editables) |
| Números verdes | Números ingresados por ti |
| Celdas blancas | Espacios vacíos para completar |
| Botón "Nuevo Juego" | Inicia una partida nueva |
| Botón "Ayuda" | Obtiene una sugerencia |
| Etiqueta de estado | Muestra mensajes y coordenadas |

---

## ⌨️ Controles del Juego

### Cómo Ingresar Números

1. **Haz clic** en una celda vacía (blanca) para seleccionarla
2. **Presiona una tecla numérica** (1, 2, 3, 4, 5 o 6) para ingresar el número
3. **Verás el número aparecer** inmediatamente

```
Ejemplo:
1. Haces clic en una celda vacía
2. Presionas la tecla "3"
3. La celda muestra: 3 (en color verde)
```

### Cómo Borrar Números

Para eliminar un número que ingresaste:

1. **Haz clic** en la celda que tiene el número
2. **Presiona BACKSPACE o DELETE**
3. **El número desaparece**

```
Nota: No puedes borrar los números iniciales (en gris)
      Esos son fijos y no se pueden editar
```

### Otros Controles

| Tecla | Acción |
|-------|--------|
| 1-6 | Ingresa el número correspondiente |
| BACKSPACE | Borra el número de la celda |
| DELETE | Borra el número de la celda |
| Clic del ratón | Selecciona una celda |

---

## 🎨 Indicadores Visuales

### Colores de las Celdas

#### Celda Gris (Fija)
```
┌─────────┐
│    3    │  ← Número inicial
│ (gris)  │  ← No editable
└─────────┘
```
- **Significado:** Número que vino con el puzzle
- **Acción:** No puedes modificarla

#### Celda Blanca (Vacía)
```
┌─────────┐
│         │  ← Celda vacía
│ (blanca)│  ← Editable
└─────────┘
```
- **Significado:** Celda disponible para llenar
- **Acción:** Puedes ingresar números aquí

#### Celda Verde Oscuro (Tu Entrada)
```
┌─────────┐
│    4    │  ← Número que ingresaste
│ (verde) │  ← Válido hasta ahora
└─────────┘
```
- **Significado:** Número que colocaste y es válido
- **Acción:** Puedes reemplazarlo

#### Celda Roja (Error)
```
┌─────────┐
│    2    │  ← ¡Error!
│ (rojo)  │  ← Conflicto detectado
└─────────┘
```
- **Significado:** El número viola las reglas
- **Causa:** Hay otro 2 en la misma fila, columna o bloque
- **Acción:** Borra el número y coloca uno válido

#### Celda Azul Claro (Ayuda)
```
┌─────────┐
│    5    │  ← Sugerencia automática
│ (azul)  │  ← Colocado por "Ayuda"
└─────────┘
```
- **Significado:** Número sugerido por el sistema
- **Acción:** Generalmente es correcto

#### Celda Azul Oscuro (Seleccionada)
```
┌═════════┐
║    3    ║  ← Celda activa
║ (azul)  ║  ← Está seleccionada
└═════════┘
```
- **Significado:** Celda donde escribirás a continuación
- **Acción:** Presiona 1-6 para ingresar un número

---

## 🎮 Paso a Paso: Primer Juego

### Paso 1: Inicia el Juego
- Abre la aplicación
- Haz clic en **"Comenzar el Juego"**
- Verás el tablero con números iniciales

### Paso 2: Analiza el Tablero
```
Observa:
✓ Qué números ya están colocados
✓ Dónde hay huecos vacíos
✓ Qué números faltan en cada fila, columna y bloque
```

### Paso 3: Identifica Celdas Fáciles
Busca celdas donde solo hay **una opción posible**:

```
Ejemplo: En la fila 1 tienes: 1, 2, 3, _, 5, 6
         Falta el 4, así que ahí va el 4
```

### Paso 4: Completa Celdas
1. Haz clic en una celda vacía
2. Analiza qué números NO pueden ir ahí
3. Ingresa el número válido

### Paso 5: Verifica con los Colores
- Si el número está **verde**: ✓ Válido
- Si el número está **rojo**: ✗ Error, bórralo

### Paso 6: Usa Ayuda si Lo Necesitas
- Cuando estés atascado, haz clic en **"Ayuda"**
- Se colocará un número correcto automáticamente
- Continúa desde ahí

### Paso 7: Completa el Tablero
Repite hasta llenar todas las celdas

### Paso 8: ¡Gana!
Cuando completes el tablero correctamente, verás:
```
╔════════════════════════════════════╗
║       Sudoku Completado            ║
║                                    ║
║  ¡Felicidades!                     ║
║  Has completado correctamente      ║
║  el Sudoku 6x6                     ║
║                                    ║
║           [OK]                     ║
╚════════════════════════════════════╝
```

---

## 💡 Estrategias y Consejos

### Estrategia 1: Números Únicos en Bloque
Busca números que solo pueden ir en un lugar dentro de un bloque 2x3.

```
Bloque con números: 1, 2, _, _, 5, 6
Solo falta 3 y 4. Si en una fila solo cabe 3, 
entonces va 3 y en el otro va 4.
```

### Estrategia 2: Candidatos por Celda
Anota mentalmente qué números PODRÍAN ir en cada celda vacía:

```
Celda (2,3):
✓ No puede ser 1 (ya está en la fila 2)
✓ No puede ser 2 (ya está en la columna 3)
✓ No puede ser 3 (ya está en el bloque)
✓ Candidatos: 4, 5, 6
```

### Estrategia 3: Números Frecuentes
Primero coloca los números que aparecen más veces en el tablero.

### Estrategia 4: Elimina Posibilidades
Después de colocar cada número, revisa qué números ya no pueden ir en otras celdas.

### Estrategia 5: Resuelve por Bloques
Completa primero los bloques que ya tienen muchos números.

### Estrategia 6: Identifica Conflictos Rápido
Si un número aparece en rojo, significa que hay un conflicto. Bórralo y prueba otro.

---

## 🆘 Mensajes Comunes

### ✅ Mensajes Positivos

| Mensaje | Significado |
|---------|-------------|
| "Celda seleccionada: (1, 1)" | Has seleccionado una celda |
| "Se ha colocado una sugerencia automática..." | La ayuda funcionó |
| "Nuevo juego iniciado." | Empezó una partida nueva |
| "Juego completado." | ¡Ganaste! |

### ❌ Mensajes de Error

| Mensaje | Causa | Solución |
|---------|-------|----------|
| "Número inválido en esta posición" | Viola las reglas | Borra e intenta otro |
| "No puedes usar más ayuda: solo queda una celda vacía..." | No hay más ayudas disponibles | Completa manualmente |

---

## 🎓 Ejemplo Práctico Completo

### Tablero Inicial

```
  1   2   3   4   5   6
┌─────────────┬─────────────┐
│ 1 |   | 3 │   |   | 2 │ 1
│   | 4 |   │ 5 |   |   │ 2
├─────────────┼─────────────┤
│   |   | 6 │ 1 |   |   │ 3
│ 2 |   |   │   | 3 |   │ 4
├─────────────┼─────────────┤
│   | 5 |   │   |   | 4 │ 5
│   |   |   │   | 6 |   │ 6
└─────────────┴─────────────┘
```

### Análisis de Fila 1
```
Fila 1: 1, ?, 3, ?, ?, 2
Faltan: 4, 5, 6
```

### Análisis de Columna 2
```
Columna 2: ?, 4, ?, ?, 5, ?
Faltan: 1, 2, 3, 6
```

### Análisis del Bloque 1 (arriba-izquierda)
```
Bloque 1 tiene: 1, 3, 4, 6 (2x3)
Faltan: 2, 5
```

### Resolución Paso a Paso

**Paso 1:** Celda (1,2) - Fila 1 necesita 4,5,6 / Columna 2 necesita 1,2,3,6 / Bloque 1 necesita 2,5
- Intersección: **6** ✓

**Paso 2:** Celda (1,4) - Fila 1 necesita 4,5 / Columna 4 necesita 2,3,4,6
- Intersección: **4** ✓

**Paso 3:** Celda (1,5) - Fila 1 necesita 5 / Columna 5 necesita 1,2,4,5
- Intersección: **5** ✓

Y así sucesivamente hasta completar el tablero...

---

## 🎯 Niveles de Dificultad (Conceptual)

### Fácil
- Muchos números iniciales (40-50%)
- Muchas celdas con una única opción
- Se resuelve principalmente con lógica directa

### Medio
- Números moderados iniciales (30-40%)
- Requiere análisis de varias celdas
- Necesita estrategia de candidatos

### Difícil
- Pocos números iniciales (20-30%)
- Requiere análisis profundo
- Puede necesitar prueba y error

### Muy Difícil
- Muy pocos números iniciales (<20%)
- Requiere técnicas avanzadas
- Solo una solución posible

---

## ⏱️ Consejos de Tiempo

- **Fácil:** 5-10 minutos
- **Medio:** 10-20 minutos
- **Difícil:** 20-45 minutos
- **Muy Difícil:** 45+ minutos

---

## 🔄 Volver a Jugar

### Para Empezar una Partida Nueva
1. Haz clic en **"Nuevo Juego"**
2. Confirma en el diálogo que aparece
3. Se generará un nuevo Sudoku aleatorio
4. Todos tus números anteriores se borrarán

---

## 🚫 Limitaciones y Reglas

### Lo que SÍ puedes hacer
- ✅ Ingresar números 1-6
- ✅ Borrar números que ingresaste
- ✅ Usar la ayuda múltiples veces
- ✅ Seleccionar cualquier celda vacía
- ✅ Jugar múltiples partidas

### Lo que NO puedes hacer
- ❌ Editar los números iniciales (grises)
- ❌ Borrar números iniciales
- ❌ Ingresar números fuera del rango 1-6
- ❌ Ingresar números cuando el tablero está completo
- ❌ Usar más de una ayuda en la misma celda

---

## 🏆 Objetivos para Mastery

### Nivel 1: Principiante
- [ ] Completar tu primer Sudoku 6x6
- [ ] Entender las tres reglas básicas
- [ ] Usar la ayuda correctamente

### Nivel 2: Intermedio
- [ ] Completar 5 Sudokus sin usar ayuda
- [ ] Completar uno en menos de 15 minutos
- [ ] Identificar bloques problemáticos rápidamente

### Nivel 3: Avanzado
- [ ] Completar 10 Sudokus sin ayuda
- [ ] Completar uno en menos de 5 minutos
- [ ] Resolver sin cometer errores

### Nivel 4: Experto
- [ ] Completar 20+ Sudokus consecutivos sin errores
- [ ] Resolver dos en menos de 10 minutos combinados
- [ ] Ayudar a otros a jugar

---

## 📞 Preguntas Frecuentes (FAQ)

### P: ¿Puedo deshacer un movimiento?
**R:** No hay función de deshacer. Usa BACKSPACE para borrar el número y coloca uno nuevo.

### P: ¿Hay un límite de tiempo?
**R:** No, puedes tomarte todo el tiempo que necesites. No hay límite de tiempo.

### P: ¿Cuántas veces puedo usar la ayuda?
**R:** Depende del Sudoku, pero generalmente puedes usar ayuda 3-4 veces máximo.

### P: ¿Por qué algunos números aparecen en rojo?
**R:** Porque ese número ya existe en la misma fila, columna o bloque. Viola las reglas.

### P: ¿Qué significa "solo queda una celda vacía"?
**R:** Que casi terminas el puzzle. La ayuda no funciona cuando casi estás ganando.

### P: ¿Puedo cambiar la dificultad?
**R:** Actualmente todos los puzzles generados son de dificultad similar. Cada nuevo juego es único.

### P: ¿Existe un modo multiplayer?
**R:** No, este es un juego para un solo jugador.

### P: ¿Puedo guardar mi progreso?
**R:** No hay función de guardado. Debes completar el puzzle en una sesión.

---

## 🎓 Técnicas Avanzadas

### Análisis de Pares
Si dos celdas en un bloque solo pueden tener dos números iguales, esos números no pueden estar en otras celdas del bloque.

### Cadenas de Deducción
Sigue una cadena lógica donde un número determina otro, que determina otro, etc.

### Análisis de Líneas de Bloque
Si un número en un bloque solo puede estar en una línea (fila o columna), no puede estar en esa línea fuera del bloque.

---

## 💾 Guardando tu Progreso

**Nota:** Esta versión no guarda automáticamente. Para no perder progreso:
- ✅ Sigue jugando hasta terminar
- ✅ Completa el puzzle en una sesión
- ❌ No cierres la aplicación hasta terminar

---

## 🎮 Disfruta el Juego

Ahora que sabes cómo jugar:

1. **Lanza la aplicación**
2. **Haz clic en "Comenzar el Juego"**
3. **¡Comienza tu aventura Sudoku!**

Recuerda:
- Mantén la calma 😌
- Analiza antes de escribir ✍️
- Usa la lógica, no la suerte 🧠
- ¡Diviértete! 🎉

---

## 📚 Recursos Adicionales

- [Sudoku.com - Tutoriales](https://sudoku.com/es/how-to-play-sudoku/)
- [Strategies for Sudoku Solving](https://www.sudokupuzzles.org/)
- [Video Tutorials](https://www.youtube.com/results?search_query=how+to+solve+sudoku)

---

**Versión de la guía:** 1.0  
**Última actualización:** 2025  
**Nivel de dificultad:** Principiante a Avanzado