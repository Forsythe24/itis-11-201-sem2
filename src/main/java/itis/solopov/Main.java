package itis.solopov;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

public class Main {
    private static final int PORT = port();

    public static void main(String[] args) throws LifecycleException, ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.getConnector();
        tomcat.setBaseDir(tempDirectory());
        tomcat.setPort(PORT);
        tomcat.getHost().setAppBase(".");
        tomcat.addWebapp("", ".");
        tomcat.start();
        tomcat.getServer().await();
    }

    private static int port() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8080;
    }

    private static String tempDirectory() {
        try {
            File tempDirectory = File.createTempFile("tomcat.", "." + PORT);
            tempDirectory.delete();
            tempDirectory.mkdir();
            tempDirectory.deleteOnExit();
            return tempDirectory.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
