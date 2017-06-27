package com.jrshirt.alerts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jrshirt.util.ServidorCorreo;

public class JRShirtAlerts {

	private static final ResourceBundle PATH_BUNDLE = ResourceBundle.getBundle("com/jrshirt/config/path");
    //private static final String URLS = "urls.txt";
	private static final String URLS = PATH_BUNDLE.getString("path.alerts");
    private static final String SUBJECT = "[JRShirt Alerts] Novedades";
    private static final String EMAIL = "jrshirttienda@gmail.com";
    private static TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            jrshirtAlertMethod();
            System.out.println(new Date());
        }
    };

    public static void main(String args[]) {
        // Aqui se pone en marcha el timer cada segundo.
        Timer timer = new Timer();
        // Dentro de 0 milisegundos avisame cada 300000 milisegundos
        timer.scheduleAtFixedRate(timerTask, 0, 300000);
    }

    private static void jrshirtAlertMethod() {
        String cambiosEmail = "";
        try {
            // Recorrer fichero urls.txt
            BufferedReader urls = new BufferedReader(new FileReader(new File(
                    URLS)));
            String url = "";
            while ((url = urls.readLine()) != null) {
                String[] param = url.split(";");
                String file = param[1];
                String urlSearch = param[2];
                String list_node = param[3];
                String list_node_end = param[4];
                String block = getOutputStreamBlock(urlSearch, list_node, list_node_end);

                if (getDiffWithFile(block, file)) {
                    //Enviar Email con el block nuevo
                    cambiosEmail += "Cambio en " + file + ". Se ha encontrado un nuevo bloque\n";
                }
            }

            if (!cambiosEmail.equals("")) {
                ArrayList<String> destinatarios = new ArrayList<String>();
                destinatarios.add(EMAIL);
                ServidorCorreo.sendEmail(SUBJECT, cambiosEmail, new ArrayList<File>(), destinatarios);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static String getOutputStreamBlock(String urlSearch, String list_node,
            String list_node_end) throws Exception {
        URL url = new URL(urlSearch);
        URLConnection conn = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String str;
        String doc = "";
        while ((str = br.readLine()) != null) {
            // System.out.println(str);
            if (str.endsWith(list_node)) {
                doc += str;
                String str2 = "";
                while (!(str2 = br.readLine()).endsWith(list_node_end)) {
                    doc += str2;
                }

                doc += str2;
                break;
            }
        }
        return doc;
    }

    private static boolean getDiffWithFile(String block, String file) throws Exception {
        boolean enviarMail = false;
        File archivoTemporal = new File(file);
        if (!archivoTemporal.exists()) {
            // Runtime.getRuntime().exec("chmod 777 tmp.txt");
            crearArchivo(archivoTemporal, block);

            // Enviar mail a false
            enviarMail = false;
        } else {
            BufferedReader br2 = new BufferedReader(new FileReader(
                    archivoTemporal));
            String textoEnFichero = "";
            String cadena;
            while ((cadena = br2.readLine()) != null) {
                textoEnFichero += cadena;
            }

            if (block.equals(textoEnFichero)) {
                enviarMail = false;
            } else {
                enviarMail = true;
                //Borramos el fichero y creamos uno nuevo con el nuevo bloque
                archivoTemporal.delete();
                archivoTemporal = new File(file);
                crearArchivo(archivoTemporal, block);
            }
        }

        return enviarMail;
    }

    private static void crearArchivo(File archivoTemporal, String block) throws Exception {
        archivoTemporal.setReadable(true);
        archivoTemporal.setExecutable(true);
        archivoTemporal.setWritable(true);
        archivoTemporal.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(
                archivoTemporal));
        bw.write(block);
        bw.close();

    }
}
