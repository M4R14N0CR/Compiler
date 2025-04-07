import java.util.ArrayList;
import java.util.HashMap;


//En esta clase llamada faseLexica nos encargaremos de hacer como tal el analisis léxico,
//es decir realizamos el proceso de reconocimiento de los tokens, y errores.
public class faseLexica {

    //Creamos la lista de tokens y la lista de errores, ademas del
    //hashmap que se encarga de guardar solo los identificadores
    private ArrayList<Token> listaToken = new ArrayList<Token>();
    private ArrayList<Token> listaErrores = new ArrayList<Token>();
    private HashMap<String, String> hashMap = new HashMap<>();

    //Esta variable que se usa para saber en que linea del documento estamos.
    public int numeroLinea = 0;
    Estados estado;


    //En esta clase llamada lexer recibe la cadena o el texto que le enviemos desde el main,
    //despues de esto se encarga de recorrer caracter por caracter y dependiendo de lo que sea
    //va a ir cambiando de estado para ir formando los lexemas, con los cuales iremos llenando la lista de tokens.
    public void lexer(String pCadena){
        numeroLinea++;

        char[] pCadenaChar = pCadena.toCharArray();//Convertimos la cadena en arreglo de char
        estado = Estados.INICIO;
        StringBuilder lexema = new StringBuilder();//Creamos un StringBuilder para ir añadiendo los caracteres de un lexema
        for (char caracter : pCadenaChar) {

            switch (estado) {
                case INICIO:
                    if (Character.isWhitespace(caracter)) {
                        continue;
                    }
                    else if (Character.isLetter(caracter) && Character.isLowerCase(caracter) ) {
                        lexema.append(caracter);
                        estado = Estados.IDENTIFICADOR;
                    }
                    else if (Character.isDigit(caracter)) {
                        lexema.append(caracter);
                        estado = Estados.NUMERO;
                    }
                    else if (caracter == '(') {
                        lexema.append(caracter);
                        estado = Estados.PARENTESIS_IZQ;
                    }
                    else if (caracter == ')') {
                        lexema.append(caracter);
                        estado = Estados.PARENTESIS_DER;
                    }
                    else if (caracter == ';') {
                        lexema.append(caracter);
                        estado = Estados.PUNTO_COMA;

                    }
                    else if (caracter == '=') {
                        lexema.append(caracter);
                        estado = Estados.ASIGNACION;
                    }
                    else if (caracter == '+') {
                        lexema.append(caracter);

                        estado = Estados.SUMA;
                    }
                    else if (caracter == '-') {
                        lexema.append(caracter);
                        estado = Estados.RESTA;
                    }
                    else if (caracter == '*') {
                        lexema.append(caracter);
                        estado = Estados.MULTIPLICACION;
                    }
                    else if (caracter == '/') {
                        lexema.append(caracter);
                        estado = Estados.DIVISION;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case IDENTIFICADOR:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"IDENTIFICADOR"));
                        hashMap.put(lexema.toString(), "IDENTIFICADOR");
                        lexema.setLength(0);
                        continue;
                    }
                    else if(Character.isLetter(caracter) && Character.isLowerCase(caracter) && lexema.length() < 12){
                        lexema.append(caracter);
                    }

                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case NUMERO:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"NUMERO"));
                        lexema.setLength(0);
                        continue;
                    }
                    else if(Character.isDigit(caracter)){
                        lexema.append(caracter);
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case PARENTESIS_IZQ:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"PARENTESIS_IZQ"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case PARENTESIS_DER:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"PARENTESIS_DER"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case PUNTO_COMA:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"PUNTO_COMA"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case ASIGNACION:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"ASIGNACION"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case SUMA:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"SUMA"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case RESTA:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"RESTA"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case MULTIPLICACION:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"MULTIPLICACION"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case DIVISION:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaToken.add(new Token(lexema.toString(),"DIVISION"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        estado = Estados.Error;
                        lexema.append(caracter);
                    }
                    break;
                case Error:
                    if(Character.isWhitespace(caracter)){
                        estado = Estados.INICIO;
                        listaErrores.add(new Token("Error [Fase Léxica]: La línea " + numeroLinea + " contiene un error, lexema no reconocido: " + lexema.toString(), "ERROR"));
                        lexema.setLength(0);
                        continue;
                    }
                    else {
                        lexema.append(caracter);
                    }
                    break;

            }
        }
    }

    //Función de imprimir para ver si esta guardando bien los tokens en la lista de tokens.
    public void imprimir(){

        for (Token token : listaToken) {
            System.out.println("Token: " + token.getValor() + ", Tipo: " + token.getTipo());
        }
    }


    //Getters y Setters
    public ArrayList<Token> getListaToken() {
        return listaToken;
    }

    public void setListaToken(ArrayList<Token> listaToken) {
        this.listaToken = listaToken;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public ArrayList<Token> getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(ArrayList<Token> listaErrores) {
        this.listaErrores = listaErrores;
    }
}