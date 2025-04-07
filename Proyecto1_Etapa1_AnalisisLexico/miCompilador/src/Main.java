import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//Este es el main que va a realizar la lectura y escritura del archivo de texto y va a llamar a la clase faseLexica
public class Main {
    public static void main(String[] args) {

        //Con esto comprobamos que las entradas sean exactamente dos es decir el archivo de entrada
        //con el de salida, en el caso que la cantidad de archivos no sea dos, enviaremos un error de que se necesitan dos archivos
        if(args.length != 2){
            System.err.println("Uso: java Main <archivo_entrada> <archivo_salida>");
            return;
        }

        //Definimos el primer argumento de la entrada como el archivo de entrada y el segundo como el archivo de salida
        String archivoEntrada = args[0];
        String archivoSalida = args[1];


        //Creación de la instancia de la clase faseLexica
        faseLexica faselexer = new faseLexica();

        //Se crea el path del archivo de texto que se va a leer
        Path inputFile = Paths.get(archivoEntrada);

        //Se crean dos listas, uno para guardar los tokens y otro para guardar los errores
        ArrayList<Token> Lista = faselexer.getListaToken();
        ArrayList<Token> ListaErrores = faselexer.getListaErrores();


        // Crea un BufferedWriter para escribir en el archivo 'src/salida.txt',
        // asegurándose de que el BufferedWriter se cierre correctamente al final.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {

                List<String> lines = Files.readAllLines(inputFile);

                //enviamos todas las lineas del archivo a el lexer para su correspondiente análisis
                for(String linea: lines){
                    linea = linea + " ";
                    faselexer.lexer(linea);
                }

                //Recorremos la lista de tokens que tiene el resultado del lexer para que lo escribamos en el archivo de salida
                for(Token token : Lista){
                    String valor = token.getValor();
                    String tipo = token.getTipo();
                    String variable = "Token: " + valor + ", Tipo: " + tipo;
                    writer.write(variable);
                    writer.newLine();
                }
                //Recorremos la lista de errores que tiene el resultado del lexer para que lo escribamos en el archivo de salida
                for(Token token : ListaErrores){
                    String valor = token.getValor();
                    writer.write(valor);
                    writer.newLine();
                }


        }catch (IOException e) {
            System.err.println("Ocurrió un error: " + e.getMessage());
        }

    }
}
