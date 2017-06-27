/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrshirt.updatestock;

import java.util.List;

/**
 *
 * @author juanra
 */
public class UpdateStock {

    public static void main(String[] args) {
        updateStock();
    }

    private static void updateStock() {
        //Obtener listado de Productos
        List<Object> listadoProductos = getProductos();

        if (listadoProductos.size() > 0) {
            //Se procede a generar el html
            String htmlCode = getHtmlCode(listadoProductos);
            System.out.println(htmlCode);

        }
    }

    private static List<Object> getProductos() {
        return UpdateStockService.getProductos();
    }

    private static String getHtmlCode(List<Object> listadoProductos) {
        return UpdateStockService.getHtmlCode(listadoProductos);
    }
}
