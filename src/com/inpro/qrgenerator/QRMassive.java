/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inpro.qrgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author juanra
 */
public class QRMassive {

    //private static final String NODES = "/home/juanra/VirtualBoxShared/nodes.txt";
    private static final String NODES = "/home/juanra/Escritorio/nodes.txt";
    private static final String NODES_HTML = "/home/juanra/Escritorio/nodes.html";
    private static final String WEBPAGE = "http://www.jrshirt.com/node/";
    private static final String QRIMAGE_PATH = "/home/juanra/tmp/qr/";
    private static final String QRIMAGE_EXT = ".png";
    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGTH = 300;
    private static final int QR_WIDTH_OUT = 150;
    private static final int QR_HEIGTH_OUT = 150;
    private static final int COLUMNS = 5;

    public static void main(String args[]) {
        try {
            File f = new File(NODES);
            BufferedReader br = new BufferedReader(new FileReader(f));
            TestQRCode qr = new TestQRCode();
            HashMap<String, Object> nodeMap = null;
            List<Object> listadoNodos = new ArrayList<Object>();
            String nid;
            while ((nid = br.readLine()) != null) {

                try {
                    Integer id = new Integer(nid.trim());
                    if (nodeMap != null) {
                        listadoNodos.add(nodeMap);
                    }
                    nodeMap = new HashMap<String, Object>();

                    String url = WEBPAGE + nid.trim();
                    File qrImg = new File(QRIMAGE_PATH + nid + QRIMAGE_EXT);
                    qr.generateQR(qrImg, url, QR_HEIGTH, QR_WIDTH);

                    nodeMap.put("ID", id);
                    nodeMap.put("IMAGE", qrImg.getAbsolutePath());

                } catch (Exception ex) {
                    nodeMap.put("TITLE", nid);
                }
            }
            listadoNodos.add(nodeMap);

            String html = createHtmlDocument(listadoNodos);

            File fw = new File(NODES_HTML);
            fw.createNewFile();
            FileWriter bw = new FileWriter(fw);
            bw.write(html);
            bw.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String createHtmlDocument(List<Object> listaNodos) {
        String htmlDoc = "<html>";

        File directoryQR = new File(QRIMAGE_PATH);

        if (directoryQR.isDirectory()) {

            htmlDoc += "<table border=\"1\">";
            boolean abierto = false;
            //File[] images = directoryQR.listFiles();
            int i = 0;
            for (Iterator<Object> it = listaNodos.iterator(); it.hasNext();) {

                HashMap<String, Object> map = (HashMap<String, Object>) it.next();

                if (i % COLUMNS == 0) {
                    htmlDoc += "<tr style=\"page-break-inside:avoid; page-break-after:auto\">";
                    abierto = true;
                }

                htmlDoc += createTdDocument(map);

                if (i % COLUMNS == COLUMNS - 1) {
                    htmlDoc += "</tr>";
                    abierto = false;
                }
                i++;
            }
            if (abierto) {
                htmlDoc += "</tr>";
            }
            htmlDoc += "</table>";
        }

        htmlDoc += "</html>";
        return htmlDoc;
    }

    private static String createTdDocument(File image) {
        String tdStruct = "<td align=\"center\">";
        tdStruct += "<img src=\"" + image.getAbsolutePath() + "\" height=\"" + QR_HEIGTH_OUT + "\" width=\"" + QR_WIDTH_OUT + "\"><br>" + image.getName().replace(".png", "");
        tdStruct += "</td>";
        return tdStruct;
    }

    private static String createTdDocument(HashMap<String, Object> params) {
        String tdStruct = "<td align=\"center\" style=\"font-size:10px;\">";
        tdStruct += ((String) params.get("TITLE") == null ? "" : transformHTML((String) params.get("TITLE"))) + "<br><img src=\"" + (String) params.get("IMAGE") + "\" height=\"" + QR_HEIGTH_OUT + "\" width=\"" + QR_WIDTH_OUT + "\"><br>" + (Integer) params.get("ID");
        tdStruct += "</td>";
        return tdStruct;
    }

    private static String transformHTML(String title) {
        return title.replaceAll("á", "&aacute;")
                .replaceAll("é", "&eacute;")
                .replaceAll("í", "&iacute;")
                .replaceAll("ó", "&oacute;")
                .replaceAll("ú", "&uacute;")
                .replaceAll("Á", "&Aacute;")
                .replaceAll("É", "&Eacute;")
                .replaceAll("Í", "&Iacute;")
                .replaceAll("Ó", "&Oacute;")
                .replaceAll("Ú", "&Uacute;")
                .replaceAll("ñ", "&ntilde;")
                .replaceAll("Ñ", "&Ntilde;");
    }
}
