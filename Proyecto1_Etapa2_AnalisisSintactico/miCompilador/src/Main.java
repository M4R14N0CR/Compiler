import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//Este es el main que va a realizar la lectura y escritura del archivo de texto y va a llamar a la clase faseLexica
public class Main {
    public static void main(String[] args) {


        //Verificamos que los argumentos de entrada sean dos, el archivo de entrada y el archivo de salida
        if(args.length != 2){
            System.err.println("Uso: java Main <archivo_entrada> <archivo_salida>");
            return;
        }

        //Definimos el primer argumento de la entrada como el archivo de entrada y el segundo como el archivo de salida
        String archivoEntrada = args[0];
        String archivoSalida = args[1];


        //Creación de la instancia de la clase faseLexica
        faseLexica faselexer = new faseLexica();
        faseSintactica fasesintactica = new faseSintactica(faselexer.getListaToken(),faselexer.getHashMap());

        //Obetenemos el path del archivo de entrada
        Path inputFile = Paths.get(archivoEntrada);

        //Se crean dos listas, uno para guardar los tokens y otro para guardar los errores
        ArrayList<Token> Lista = faselexer.getListaToken();
        ArrayList<Token> ListaErrores = faselexer.getListaErrores();

        //Creamos el hashmap para poder escribirlo luego en un archivo este hashmap es el que ya esta modificado de la fase sintactica
        HashMap<String , String> hashMapIdentificadores = fasesintactica.getHashMap();


        // Crea un BufferedWriter para escribir en el archivo 'src/salida.txt',
        // asegurándose de que el BufferedWriter se cierre correctamente al final.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {

                List<String> lines = Files.readAllLines(Paths.get(inputFile.toUri()));

                //enviamos todas las lineas del archivo a el lexer para su correspondiente análisis
                for(String linea: lines){
                    linea = linea + " ";
                    faselexer.lexer(linea);
                    if(!linea.equals(" ")){
                        faselexer.addFinal();
                    }
                    if(linea.equals(" ")){
                        faselexer.addNumLinea();
                    }
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

                if(ListaErrores.size() == 0) {
                    fasesintactica.programa();
                    fasesintactica.eliminarIdentificadores();
                    for (String error : fasesintactica.getListaErrores()) {
                        writer.write(error);
                        writer.newLine();
                    }

                }
            //Escribimos en un archivo la tabla de simbolos ya modificada sin indentificadores que hayan tenido error
            try (BufferedWriter writerTablaSimbolos = new BufferedWriter(new FileWriter("tablaDeSimbolos.txt"))) {
                for (HashMap.Entry<String, String> entry : hashMapIdentificadores.entrySet()) {
                    writerTablaSimbolos.write("Clave: " + entry.getKey() + ", Valor: " + entry.getValue());
                    writerTablaSimbolos.newLine();
                }
                writerTablaSimbolos.flush();
            } catch (IOException e) {
                System.err.println("Ocurrió un error al escribir en tablaSimbolos.txt: " + e.getMessage());
            }



        }catch (IOException e) {
            System.err.println("Ocurrió un error: " + e.getMessage());
        }

    }
}
