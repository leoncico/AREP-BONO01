# AREP-BONO01
## Autor: David Leonardo Piñeros Cortes

### Compilacion y Ejecucion
El primer paso es clonar el repositorio
```
git clone https://github.com/leoncico/AREP-BONO01.git
```

Luego entrar al directorio resultante y ejecutar el comando de maven para compilar
```
mvn clean install
```

Luego se ejecutan los siguientes comando para ejecutar el programa con java. De ser necesario abrir dos
terminales

```
java -cp target/classes escuelaing.edu.co.calcreflex.CalcReflexBEServer
```

```
java -cp target/classes escuelaing.edu.co.calcreflex.CalcReflexFacade
```

Finalmente ingresa a localhost:35000
