# e-Parser
Proyecto para la adecuación automática de metadatos en SCORM

## Forma de uso:

Al abrir el programa, deberá seleccionar la carpeta donde se encuentren los empaquetados .ZIP que serán modificados con los metadatos correctos... El programa respetará la estructura de carpetas que tiene designada.

### Archivos que podrá reconocer y modificar

1. metada.xml El archivo deberá tener una estructura reconocible para el sistema (Descrita en el archivo "metadata.xml" en el mismo proyecto) además de proporcionar una nomenclatura válida descrita por la empresa eDistribution /^.{4,5}(le[0-9]{1,2}(ob[0-9]{2}(rec[0-9]{2}){0,1}){0,1}){0,1}/
2. [expresion regular anterior].zip El archivo deberá contener en si mismo un archivo metadata.xml con los mismos requisitos que el anterior.
3. [.\*].xls ó [.\*].xlsx El archivo debera corresponder exactamente en estructura al archivo descrito por "Formato_metadatos.xltm" dentro del mismo proyecto. Este hará reaccionar al programa haciendo que todos los datos anteriormente escritos en el archivo que haga match con él sean reemplazados por nuevos escritos en este archivo.

(Cualquier elemento que no cumpla las anteriores condiciones será omitido en su modificación pero no en su copia si es el caso).

## Opciones
* Podrá realizar BackUP de los archivos antes de moddificarlos seleccionando la opción "Realizar copia de archivos en carpeta eFixer", lo cual hará que todos los archivos se copien en una carpeta con este nombre además de agregar hora y fecha del momento en que se ejecutó la acción. Tambien cabe mencionar que los cambios se realizan sobre esta carpeta, dejando intacta la carpeta original.
* Podrá decidir entre realizar las relaciones respetando la herencia del padre siguiendo la cadena en la expresión regular o no seguirla

