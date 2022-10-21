import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class ApplicationProperties {
    //PREFIX = prefijo del archivo application-XX.properties de Resources

    private static Properties instance = null;
    private static final String APPLICATION_PREFIX = "application";
    private static final String APPLICATION_SUFFIX = "properties";

    private static final Logger logger = (Logger) LogManager.getLogger(ApplicationProperties.class);

    public static Properties getInstance(){
        //singleton
        if(instance == null){
            instance = loadPropertiesFile();
        }
        return instance;
    }

    private ApplicationProperties(){
        //crear este constructor para que no se puedan construir properties desde fuera

    }

    private static Properties loadPropertiesFile(){
        //un método privado solo se puede usar dentro de esta clase
        //levantar dinámicamente el archivo de configuración
        //estamos guardando la variable de entorno que ha introducido el usuario en la consola
        String environment = Optional.ofNullable(System.getenv("env"))
                .orElse("dev");

        //Construimos el nombre del archivo de entorno
        String fileName = String.format("%s-%s.%s", APPLICATION_PREFIX, environment, APPLICATION_SUFFIX);

        //Creamos un log
        logger.info("Property ", fileName);

        Properties prop = new Properties();
        try {
            prop.load(getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            logger.error("No se pudo cargar el archivo{}", fileName);
        }

        return prop;
        //retornamos un objeto de tipo Properties
    }
}
