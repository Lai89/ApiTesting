import java.util.Optional;

public class ConfVariables {
    //guardamos las variables get para el archivo de entorno

    //variable host dinámica
    public static String  getHost(){
        return Optional.ofNullable(System.getenv("host"))
                .orElse((String)ApplicationProperties.getInstance().get("host"));
    }

        //variable path dinámica
    public static String  getPath(){
        return Optional.ofNullable(System.getenv("pathuri"))
                .orElse((String)ApplicationProperties.getInstance().get("pathuri"));
    }
}
