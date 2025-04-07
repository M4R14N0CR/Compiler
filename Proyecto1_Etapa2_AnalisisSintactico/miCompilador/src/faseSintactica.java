import java.util.ArrayList;
import java.util.HashMap;

//En esta clase llamada faseSintactica nos encargaremos de hacer como tal el analisis sintactico,
//es decir realizamos el proceso de reconocimiento de la sintaxis del codigo y de los errores.
public class faseSintactica {

    private ArrayList<String> listaErrores = new ArrayList<>();
    private ArrayList<Token> listaToken;
    private int posicion;
    private int numLinea = 1;
    private int contadorParentesisIzquierdo = 0;

    private HashMap<String, String> hashMap;
    private ArrayList<Integer> listaLineasErrores = new ArrayList<>();
    private ArrayList<Token> listaIdentificadores = new ArrayList<>();


    // Constructor de la clase
    public faseSintactica(ArrayList<Token> listaToken,HashMap<String, String> hashMap){
        this.listaToken = listaToken;
        this.posicion = 0;
        this.hashMap = hashMap;


    }


    //Esta funcion nos permite eliminar los duplicados de la lista que contiene las lineas en las que hubo errores
    public void eliminarDuplicados(){
        ArrayList<Integer> listaSinDuplicados = new ArrayList<>();

        for (Integer numero : listaLineasErrores) {
            if (!listaSinDuplicados.contains(numero)) {
                listaSinDuplicados.add(numero);
            }
        }
        listaLineasErrores.clear();
        listaLineasErrores.addAll(listaSinDuplicados);
    }

    //Esta funcion de eliminar identificadores eliminamos los identificadores que estan en las lineas donde hubo errores,
    //estos los eliminamos del hash que contiene los identificadores
    public void eliminarIdentificadores(){
        eliminarDuplicados();
        String simbolo="";

        for(int i=0; i<listaLineasErrores.size(); i++){
            for(int j=0; j<listaIdentificadores.size(); j++) {
                if (Integer.parseInt(listaIdentificadores.get(j).getTipo()) == listaLineasErrores.get(i)) {
                    simbolo = listaIdentificadores.get(j).getValor();
                    hashMap.remove(simbolo);
                }
            }

        }

    }

    // Esta funcion nos permite saber si una entrada es un programa, es acorde con la gramatica del lenguaje L1,
    //ademas tambien da errores en el caso de que hubiera un error con el punto y coma
    public void programa(){

        contarLineasVacias();//Con esta funcion nos saltamos las lineas vacias
        //Este error es en el caso de que solo exista un punto coma en la linea es decir este solo
        while(listaToken.get(posicion).getTipo().equals("PUNTO_COMA")){
            listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea +" contiene un error en su gramática, Expresion incorrecta");
            listaLineasErrores.add(numLinea);
            posicionPlus();

            if(listaToken.get(posicion).getTipo().equals("FIN")){
                posicionPlus();
                numLinea++;
            }
            contarLineasVacias();
        }
        //Llamamos a expresion para ver si es una expresion
        if(expresion()) {
            //Este error es para que si no hay punto y coma nos diga que falta un punto y coma
            if (listaToken.get(posicion).getTipo().equals("FIN") && !(listaToken.get(posicion - 1).getTipo().equals("PUNTO_COMA"))) {
                listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea +" contiene un error en su gramática, falta token ;");
                listaLineasErrores.add(numLinea);
                numLinea++;
                posicionPlus();
                programa();
            }
            //Si hay punto y coma entonces verificamos que puede volver a haber un programa
            else if (listaToken.get(posicion).getTipo().equals("PUNTO_COMA")) {
                posicionPlus();
                if (listaToken.get(posicion).getTipo().equals("FIN")){
                    numLinea++;
                }
                posicionPlus();
                programa();
            }
        }
    }

    // Esta funcion nos permite saber si una entrada es una expresion, es acorde con la gramatica del lenguaje L1
    public boolean expresion(){
        int checkpoint = posicion;

        // No hay identificador?
        if((posicion == 0 && (listaToken.get(posicion).getTipo().equals("ASIGNACION"))) ||
                (listaToken.get(posicion).getTipo().equals("ASIGNACION") && !(listaToken.get(posicion-1).getTipo().equals("IDENTIFICADOR")))){
            listaErrores.add("Error [Fase Sintáctica]: La línea  " + numLinea + " contiene un error en su gramática, falta un identificador");
            listaLineasErrores.add(numLinea);
            posicionPlus();
            if (!expresion()) { //Lo que sigue no es expresion?
                listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta un factor en la expresión");
                listaLineasErrores.add(numLinea);
            }
            return true;
        }

        posicion= checkpoint;//Los checkpoint para aplicar backtraking

        //Verificamos si es un identificador y luego si sigue un igual en el caso de que no entonces mas abajo hacemos un checkpoint para volver
        if(listaToken.get(posicion).getTipo().equals("IDENTIFICADOR")){
            posicionPlus();
            if(listaToken.get(posicion).getTipo().equals("ASIGNACION")){
                listaIdentificadores.add(new Token(listaToken.get(posicion-1).getValor(),Integer.toString(numLinea)));
                posicionPlus();

                if(expresion()){//Es una expresion lo que sigue?
                    return true;
                }
                else{
                    listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta un factor en la expresión");//Error en el caso de que no sea expresion lo que sigue ya que puede faltar una expresion o hasta un factor
                    listaLineasErrores.add(numLinea);
                    return true;
                }
            }
        }

        posicion= checkpoint;
        //Hacemos checkpoint en el caso de que no sea identificador mas igual
        return expresionTermino();// Llamamos a expresion termino para validar si en realidad expresion era termino

    }

    //La parte de termino de la expresion
    public boolean expresionTermino(){
        if(termino()){//Llamamos a la funcion termino
            if (listaToken.get(posicion).getTipo().equals("ASIGNACION")){//En el caso de que haya un igual en medio que no pudo ser dado por medio de expresion
                listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, simbolo de asignacion mal colocado");
                listaLineasErrores.add(numLinea);
                posicionPlus();
            }

            else if(listaToken.get(posicion).getTipo().equals("SUMA") || listaToken.get(posicion).getTipo().equals("RESTA")){
                posicionPlus();
                if(!expresionTermino()){
                    return false;
                }
            }
            expresionTermino();
            return true;
        }
        return false;
    }


    // Esta funcion nos permite saber si una entrada es un termino, es acorde con la gramatica del lenguaje L1
    public boolean termino(){
        errorOperadorRepetido();//Verificamos si hay un operador repetido para añadir ese error
        errorParentesisIzquierdo();//Verificamos si falta un parentesis izquierdo para añadir ese error

        if(factor()){

            if (listaToken.get(posicion).getTipo().equals("NUMERO")|| listaToken.get(posicion).getTipo().equals("IDENTIFICADOR") || listaToken.get(posicion).getTipo().equals("PARENTESIS_IZQ")) {
                errorSiguienteIncorrecto();//Error en el caso de que haya un factor pero antes de el no haya un simbolo de operación
            }
            //Volvemos a verificar si falta el parentesis izquierdo y ejecutamos el error
            else if (listaToken.get(posicion).getTipo().equals("PARENTESIS_DER") && contadorParentesisIzquierdo==0){
                while(listaToken.get(posicion).getTipo().equals("PARENTESIS_DER") && contadorParentesisIzquierdo==0){
                    listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta token: (");
                    listaLineasErrores.add(numLinea);
                    posicionPlus();
                }
            }
            //si no hay errores entonces sigue una multiplicacion o division y seguimos con el ciclo normal llamando otra vez a termino
            else if(listaToken.get(posicion).getTipo().equals("MULTIPLICACION") || listaToken.get(posicion).getTipo().equals("DIVISION")){
                posicionPlus();
                return termino();
            }

            return true;
        }

        return false;
    }

    // Esta funcion nos permite saber si una entrada es una factor, es acorde con la gramatica del lenguaje L1
    public boolean factor(){

        int checkpoint = posicion;
        if(listaToken.get(posicion).getTipo().equals("IDENTIFICADOR")){//Verificamos si es un identificador
            posicionPlus();
            return true;
        }

        posicion = checkpoint;
        if(listaToken.get(posicion).getTipo().equals("NUMERO")){//Verificamos si es un numero
            posicionPlus();
            return true;
        }

        posicion = checkpoint;
        if(listaToken.get(posicion).getTipo().equals("PARENTESIS_IZQ")){//Verificamos si es un parentesis izquierdo luego una expresion y luego un parentesis derecho
            posicionPlus();
            contadorParentesisIzquierdo ++;
            if(expresion()){
                if(listaToken.get(posicion).getTipo().equals("PARENTESIS_DER")){
                    posicionPlus();
                    contadorParentesisIzquierdo --;
                }
                else{
                    listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta token: )");//Error en el caso de que si sea expresion pero falte el parentesis derecho
                    listaLineasErrores.add(numLinea);
                    contadorParentesisIzquierdo --;
                }
                return true;
            }
            listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, expresion incompleta");//Error en el caso de que no sea una expresion
            listaLineasErrores.add(numLinea);
            contadorParentesisIzquierdo --;
            return true;
        }

        else{
            return false;
        }

    }

    //Auxiliares

    //Esta funcion nos permite contar las lineas vacias y saltarnoslas
    public void contarLineasVacias(){
        while (listaToken.get(posicion).getTipo().equals("VACIA") && posicion<listaToken.size()-1) {
            numLinea++;
            posicionPlus();
        }
    }
    //Esta funcion nos permite avanzar en la lista de tokens sin salirnos de la lista
    public void posicionPlus(){
        if (posicion < listaToken.size() - 1) {
            posicion++;
        }
    }
    //Errores

    public void errorOperadorRepetido(){
        while(listaToken.get(posicion).getTipo().equals("MULTIPLICACION") || listaToken.get(posicion).getTipo().equals("DIVISION")
                || listaToken.get(posicion).getTipo().equals("SUMA") || listaToken.get(posicion).getTipo().equals("RESTA")){
            listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta un factor en la expresión");
            listaLineasErrores.add(numLinea);
            posicionPlus();
        }

    }

    public void errorParentesisIzquierdo(){
        while(listaToken.get(posicion).getTipo().equals("PARENTESIS_DER") && contadorParentesisIzquierdo==0){
            listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta token: (");
            listaLineasErrores.add(numLinea);
            posicionPlus();
        }
    }

    public void errorSiguienteIncorrecto(){

        if(listaToken.get(posicion).getTipo().equals("NUMERO")|| listaToken.get(posicion).getTipo().equals("IDENTIFICADOR")){
            while(listaToken.get(posicion).getTipo().equals("NUMERO") ||  listaToken.get(posicion).getTipo().equals("IDENTIFICADOR")){
                listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta simbolo de Operación");
                listaLineasErrores.add(numLinea);
                posicionPlus();
            }
        }
        else if(listaToken.get(posicion).getTipo().equals("PARENTESIS_IZQ")){
            listaErrores.add("Error [Fase Sintáctica]: La línea " + numLinea + " contiene un error en su gramática, falta simbolo de Operación");
            listaLineasErrores.add(numLinea);
            termino();
        }
    }


    //Getter y Setters
    public ArrayList<Token> getListaToken() {
        return listaToken;
    }

    public void setListaToken(ArrayList<Token> listaToken) {
        this.listaToken = listaToken;
    }

    public int getposicion() {
        return posicion;
    }

    public void setposicion(int posicion) {
        this.posicion = posicion;
    }

    public ArrayList<String> getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(ArrayList<String> listaErrores) {
        this.listaErrores = listaErrores;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

}