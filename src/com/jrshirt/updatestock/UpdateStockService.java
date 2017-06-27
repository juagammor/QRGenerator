/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jrshirt.updatestock;

import com.jrshirt.updatestock.model.Producto;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanra
 */
public class UpdateStockService {

    private static final String FILE_PRODUCTOS = "/home/juanra/Dropbox/Data/JRShirtApp/productos.txt";

    public static List<Object> getProductos() {
        //Vamos a obtener los productos de un archivo txt
        return getProductosTXT();
        //Vamos a obetenr los productos de BBDD
        //return getProductosBBDD();
    }

    private static List<Object> getProductosTXT() {
        List<Object> listadoProductos = new ArrayList<Object>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(FILE_PRODUCTOS)));
            String line = "";
            while((line = br.readLine()) != null) {

                String[] attribs = line.split(";");
                Producto producto = new Producto();
                producto.setEquipo(attribs[1]);
                producto.setJugador(attribs[2]);
                producto.setTalla(attribs[3]);
                producto.setMedidas(attribs[4]);
                producto.setPrecio(attribs[5]);
                producto.setIconURL(attribs[6]);
                listadoProductos.add(producto);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listadoProductos;
    }

    public static String getHtmlCode(List<Object> listadoProductos) {
        String html = "<html><table>";

        for(Object o : listadoProductos) {
            Producto producto = (Producto) o;
            String productoHtml = getHtmlCodeByProducto(producto);
            html += productoHtml;
        }

        return html + "</table></html>";
    }

    private static String getHtmlCodeByProducto(Producto producto) {
        String html = "<tr>";
        //Imagen
        html += "<td><img src=\"" + producto.getIconURL() +"\" width=\"200px\" ></td>";
        //Tabla a la derecha para mostrar datos
        html += "<td><table>";
        //Equipo
        html += "<tr><td><b>Equipo</b>: " + producto.getEquipo() + "</td></tr>";
        //Jugador
        html += "<tr><td><b>Jugador</b>: " + producto.getJugador() + "</td></tr>";
        //Talla
        html += "<tr><td><b>Talla</b>: " + producto.getTalla() + "</td></tr>";
        //Medidas
        html += "<tr><td><b>Medidas</b>: " + producto.getMedidas() + "</td></tr>";
        //Precio (envio incluido)
        html += "<tr><td><b>Precio (env. incl.)</b>: " + producto.getPrecio() + " &euro;</td></tr>";

        html += "</table></td>";

        return html + "</tr>";
    }

}
