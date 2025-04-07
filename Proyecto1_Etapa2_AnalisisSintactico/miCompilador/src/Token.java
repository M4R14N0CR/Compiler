//Esta clase se crea con el fin de poder guardar los tokens que se generan en la
//etapa de análisis léxico, para luego poder utilizarlos en la etapa de análisis sintáctico.
public class Token {

    //Se crean los atributos de la clase token, las cuales
    //representan el valor en este caso el lexema y el tipo de token.
    private String Tipo;
    private String Valor;

    //Constructor
public Token(String Valor, String Tipo) {
        this.Valor = Valor;
        this.Tipo = Tipo;
    }

    //Getters y Setters
    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }
}
