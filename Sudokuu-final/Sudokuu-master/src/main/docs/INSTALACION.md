# 🔧 Guía de Instalación - Sudoku 6x6

Guía paso a paso para instalar y ejecutar el proyecto Sudoku 6x6 en tu computadora.

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

### 1. Java Development Kit (JDK) 17 o superior

#### Windows
1. Descarga JDK 17 desde [oracle.com](https://www.oracle.com/java/technologies/downloads/) o [adoptium.net](https://adoptium.net/)
2. Ejecuta el instalador
3. Completa la instalación (recomendado dejar configuraciones por defecto)
4. Reinicia tu computadora

#### macOS
```bash
# Usando Homebrew
brew install openjdk@17

# O descargar desde
# https://adoptium.net/
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

#### Verificar instalación
Abre terminal/CMD y ejecuta:
```bash
java -version
```

Deberías ver algo como:
```
openjdk version "17.x.x" 2024-xx-xx
OpenJDK Runtime Environment (build 17.x.x+x)
```

---

### 2. Maven 3.6 o superior

#### Windows
1. Descarga Maven desde [maven.apache.org](https://maven.apache.org/download.cgi)
2. Descomprime el archivo ZIP en una carpeta (ej: `C:\maven`)
3. Agrega Maven a la ruta del sistema:
    - Click derecho en "Este PC" → Propiedades
    - Variables de entorno → Nueva variable del sistema
    - Nombre: `MAVEN_HOME`
    - Valor: `C:\maven\apache-maven-3.x.x`
    - Edita la variable PATH y agrega: `%MAVEN_HOME%\bin`

#### macOS
```bash
# Usando Homebrew
brew install maven

# O descargar desde
# https://maven.apache.org/download.cgi
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install maven
```

#### Verificar instalación
```bash
mvn -version
```

Deberías ver:
```
Apache Maven 3.x.x (xxxxxx; 2024-xx-xx xx:xx:xx)
```

---

### 3. Git (Opcional, pero recomendado)

Para clonar el repositorio fácilmente.

#### Windows
Descarga desde [git-scm.com](https://git-scm.com/)

#### macOS
```bash
brew install git
```

#### Linux
```bash
sudo apt-get install git
```

---

## 🚀 Pasos de Instalación

### Paso 1: Obtener el Proyecto

#### Opción A: Clonar desde GitHub
```bash
git clone https://github.com/rios222/sudoku.git
cd Sudokuu-final/Sudokuu-master
```

#### Opción B: Descargar manualmente
1. Ve a https://github.com/rios222/sudoku
2. Click en "Code" → "Download ZIP"
3. Descomprime el archivo
4. Abre terminal en la carpeta `Sudokuu-final/Sudokuu-master`

### Paso 2: Verificar la estructura

Asegúrate de estar en la carpeta correcta:

```
Sudokuu-master/
├── src/
│   ├── main/
│   ├── test/
├── target/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

Ejecuta:
```bash
pwd  # En macOS/Linux
cd   # En Windows (para ver la ruta actual)
```

### Paso 3: Limpiar compilaciones anteriores

```bash
mvn clean
```

Esto borra archivos compilados previos.

### Paso 4: Descargar dependencias

```bash
mvn install
```

Maven descargará todas las dependencias necesarias (JavaFX, JUnit, etc.). Esto puede tardar unos minutos en la primera ejecución.

### Paso 5: Compilar el proyecto

```bash
mvn compile
```

Verifica que no haya errores en la consola.

### Paso 6: Ejecutar la aplicación

```bash
mvn javafx:run
```

¡La aplicación debería abrirse en una ventana nueva! 🎉

---

## 🎯 Primer Uso

Si todo fue correctamente:

1. **Verás el menú principal** con título "Sudoku 6x6"
2. **Haz clic en "Comenzar el Juego"** para iniciar
3. **El tablero aparecerá** con algunos números iniciales
4. **Selecciona una celda** con el ratón
5. **Ingresa números** presionando las teclas 1-6
6. **Usa ayuda** cuando lo necesites

---

## 🔨 Comandos Útiles

### Compilar sin ejecutar
```bash
mvn compile
```

### Ejecutar tests
```bash
mvn test
```

### Generar Javadoc
```bash
mvn javadoc:javadoc
```

La documentación se genera en `target/site/apidocs/index.html`

### Crear un JAR ejecutable
```bash
mvn package
```

Se creará `target/Sudoku6x6-1.0-SNAPSHOT.jar`

### Limpiar todo
```bash
mvn clean
```

---

## ❌ Solución de Problemas

### Error: "Command 'java' not found"

**Causa:** Java no está instalado o no está en la ruta del sistema.

**Solución:**
1. Descarga e instala Java 17
2. Reinicia la terminal/CMD
3. Verifica: `java -version`

---

### Error: "Command 'mvn' not found"

**Causa:** Maven no está instalado o configurado.

**Solución:**
1. Descarga e instala Maven
2. Configura variables de entorno (Windows)
3. Reinicia la terminal/CMD
4. Verifica: `mvn -version`

---

### Error: "BUILD FAILURE - Missing dependencies"

**Causa:** Las dependencias no se descargaron correctamente.

**Solución:**
```bash
mvn clean install
```

Si persiste:
```bash
rm -rf ~/.m2/repository  # macOS/Linux
rmdir /s %USERPROFILE%\.m2\repository  # Windows
mvn clean install
```

---

### Error: "No main manifest attribute"

**Causa:** El archivo JAR no está configurado correctamente.

**Solución:** Usa siempre:
```bash
mvn javafx:run
```

No intentes ejecutar el JAR directamente si usas JavaFX.

---

### La ventana de la aplicación no aparece

**Causa:** Problemas con JavaFX o el sistema operativo.

**Solución:**
1. Intenta cerrar y ejecutar de nuevo
2. Reinicia tu computadora
3. Verifica que tu versión de SO sea compatible

---

### Error: "Cannot find symbol - class Application"

**Causa:** JavaFX no está correctamente configurado.

**Solución:**
```bash
mvn clean compile
```

Si el error persiste, verifica que el archivo `pom.xml` tenga las dependencias de JavaFX.

---

## 📱 Información del Sistema

### Configuración mínima recomendada
- **Procesador:** Dual-core 2.0 GHz
- **RAM:** 2 GB
- **Espacio en disco:** 500 MB
- **Pantalla:** 1024 x 768 píxeles o superior

### Sistemas operativos soportados
- ✅ Windows 10/11
- ✅ macOS 10.15 o superior
- ✅ Linux (Ubuntu 20.04+, Debian 10+)

---

## 🔐 Notas de Seguridad

- Las dependencias se descargan desde repositorios oficiales de Maven
- No hay conexión a servidores externos durante el juego
- El proyecto es completamente open-source
- Puedes revisar el código en GitHub

---

## 🆘 Necesitas Ayuda?

Si tienes problemas:

1. **Lee la documentación:** [JAVADOC.md](./JAVADOC.md)
2. **Revisa el archivo:** [README.md](../README.md)
3. **Abre un issue en GitHub:** https://github.com/rios222/sudoku/issues
4. **Verifica los logs:** Los errores específicos aparecen en la consola

---

## ✅ Verificación Final

Una vez instalado, verifica que todo funciona:

```bash
# 1. Limpiar
mvn clean

# 2. Compilar
mvn compile

# 3. Ejecutar pruebas (si existen)
mvn test

# 4. Ejecutar la aplicación
mvn javafx:run
```

¡Si todo funciona sin errores, estás listo para jugar! 🎮✨

---

**Versión de la guía:** 1.0  
**Última actualización:** 2025  
**Compatible con:** Java 17+, Maven 3.6+