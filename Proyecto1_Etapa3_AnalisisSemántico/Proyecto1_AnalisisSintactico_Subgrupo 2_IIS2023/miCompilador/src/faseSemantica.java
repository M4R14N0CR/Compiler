import java.util.ArrayList;
import java.util.HashMap;

public class faseSemantica {
    private HashMap<String, String> hashMap;
    private ArrayList<Token> listaIdentificadores;
    private ArrayList<Token> listaToken;
    private int numLinea = 1;
    private ArrayList<String> listaErroresSemanticos = new ArrayList<>();

    public faseSemantica(ArrayList<Token> listaIdentificadores, ArrayList<Token> listaToken,HashMap<String, String> hashMap){
        this.listaIdentificadores = listaIdentificadores;
        this.listaToken = listaToken;
        this.hashMap = hashMap;
    }

    public void analisisSemantico() {
        for (Token token : listaToken) {//Recorremos la listaToken
            if(token.getTipo().equals("FIN")){//Se realiza el conteo de lineas
                numLinea++;
            }
            boolean coincide = false;//Variable para saber cuando un token coincide con otro token de la otra lista
            if (token.getTipo().equals("IDENTIFICADOR")) {//Solo leer los token de tipo identificador
                for (Token identificador : listaIdentificadores) {//Recorremos la lista de identificadores
                    if (token.getValor().equals(identificador.getValor())) {//Verificamos si el token coincide con el identificador
                        coincide = true;//En el caso de que coincida se cambia el valor de la variable coincide a true
                        break;
                    }
                }

                if (!coincide) {
                    hashMap.remove(token.getValor());//En el caso de que no coincida se elimina el identificador del hashmap
                    listaErroresSemanticos.add("Error [Fase Semantica]: La línea " + numLinea +" contiene un error en su gramática, no declarado identificador "+ token.getValor());//Agregamos el error a la lista de errores
                }
            }
        }
    }

    public ArrayList<String> getListaErroresSemanticos() {
        return listaErroresSemanticos;
    }

    public void setListaErroresSemanticos(ArrayList<String> listaErroresSemanticos) {
        this.listaErroresSemanticos = listaErroresSemanticos;
    }
}

