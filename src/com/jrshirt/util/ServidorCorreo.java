package com.jrshirt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ServidorCorreo {
	
	private static final String email = "jrshirttienda@gmail.com";
	private static final String passwd = "Juagammor10";
	private static final String serverSMTP = "smtp.gmail.com";
	private static final String port = "465";
	
	public static void sendEmail(String subject, String messageText, ArrayList<File> listadoAdjuntos,
			ArrayList<String> listaDestinatarios) throws Exception {
		Properties props = new Properties();
		// Nombre del host de correo, es smtp.gmail.com
		props.setProperty("mail.smtp.host", serverSMTP);
		// TLS si esta disponible
		props.setProperty("mail.smtp.starttls.enable", "true");
		// Puerto de gmail para envio de correos
		props.setProperty("mail.smtp.port", port);
		// Nombre del usuario
		props.setProperty("mail.smtp.user", email);
		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");
				
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
		

		Session session = Session.getDefaultInstance(props);
		// Para obtener un log de salida mas extenso
		session.setDebug(true);

		/*----------------------------------------------------*/
		//Creamos un mensaje de tipo multipart e iremos anadiendo las diferentes partes que lo componen
		MimeMultipart multiParte = new MimeMultipart();
		
		//Texto del Mensaje
		BodyPart texto = new MimeBodyPart();
		texto.setText(messageText);
		multiParte.addBodyPart(texto);
		
		
		// Luego construimos la parte del adjunto con la imagen. Suponemos que
		// la tenemos en un fichero
		if (listadoAdjuntos != null) {
			for (Iterator<File> iterator = listadoAdjuntos.iterator(); iterator.hasNext();) {
				File archivo = (File) iterator.next();
				BodyPart adjunto = new MimeBodyPart();
				// Cargamos la imagen
				adjunto.setDataHandler(new DataHandler(new FileDataSource(archivo.getAbsolutePath())));
				// Opcional. De esta forma transmitimos al receptor el nombre
				// original del
				// fichero de imagen.
				adjunto.setFileName(archivo.getName());
				multiParte.addBodyPart(adjunto);
			}
		}

		
		// Finalmente construimos el mensaje, le ponemos este MimeMultiPart como
		// contenido y rellenamos el resto de campos from, to y subject.
		MimeMessage message = new MimeMessage(session);
		// Se rellena el From
		message.setFrom(new InternetAddress(email));

		// Se rellenan los destinatarios
		for (Iterator<String> iterator = listaDestinatarios.iterator(); iterator.hasNext();) {
			String direccionCorreo = (String) iterator.next();
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(direccionCorreo));
		}
		// Se rellena el subject
		message.setSubject(subject);
		// Se mete el texto y la foto adjunta.
		message.setContent(multiParte);

		/*--------------------------------*/
		Transport t = session.getTransport("smtp");

		// Aqui usuario y password de gmail
		t.connect(serverSMTP, email, passwd);
		t.sendMessage(message, message.getAllRecipients());
		t.close();
	}

}
