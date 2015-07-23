package praxis.vesit.com.config;

/**
 * Created by shruti.mantri on 20/07/15.
 */
public class AppConfig {

    public static String HOST;
    public static String PORT;

    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        AppConfig.HOST = HOST;
    }

    public static String getPORT() {
        return PORT;
    }

    public static void setPORT(String PORT) {
        AppConfig.PORT = PORT;
    }

}
