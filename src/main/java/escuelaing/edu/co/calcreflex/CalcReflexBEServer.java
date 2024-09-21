/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
*/
package escuelaing.edu.co.calcreflex;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.Gson;
 
/**
*
* @author david.pineros-c
*/
public class CalcReflexBEServer {
 
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
 
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean isFirstLine = true;
            String firstLine = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (isFirstLine) {
                    firstLine = inputLine;
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
 
            URI requestURL = getRequestURI(firstLine);

            if (requestURL.getPath().startsWith("/compreflex")) {

                String command = requestURL.getQuery().substring(8);

                int startIndex = command.indexOf('(');
                int endIndex = command.indexOf(')');
                
                String operation = command.substring(0, startIndex);

                System.out.println(operation);

                Double[] params = {};

                if (startIndex != -1 && endIndex != -1) {
                    // Obtener los parámetros (dentro de los paréntesis)
                    String paramString = command.substring(startIndex + 1, endIndex);
                    String[] paramStrings = paramString.split(","); // Separar los parámetros por comas
                    
                    // Convertir los parámetros a double[]
                    params = new Double[paramStrings.length];
                    for (int i = 0; i < paramStrings.length; i++) {
                        params[i] = Double.parseDouble(paramStrings[i].trim());
                    }
                }
                


                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + responseParam(operation, params);

                // outputLine = "HTTP/1.1 200 OK\r\n"
                //         + "Content-Type: application/json\r\n"
                //         + "\r\n"
                //         + "{\"name\":\"John\", \"age\":30, \"car\":null}";
            } else {
                outputLine = getDefaultResponse();
            }
 
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
 
    }
 
    public static String responseParam(String command, Double[] params){

        Gson gson = new Gson();
        String response = "";

        if(command.equals("bbl")){
            Double[] result = bubbleSort(params);
            response = gson.toJson(result);
        }

        return response;
    }

    public static String getDefaultResponse() {
        String htmlcode
                = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Form Example</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Form with GET</h1>\n"
                + "        <form action=\"/hello\">\n"
                + "            <label for=\"name\">Name:</label><br>\n"
                + "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n"
                + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
                + "        </form> \n"
                + "        <div id=\"getrespmsg\"></div>\n"
                + "\n"
                + "        <script>\n"
                + "            function loadGetMsg() {\n"
                + "                let nameVar = document.getElementById(\"name\").value;\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                + "                    this.responseText;\n"
                + "                }\n"
                + "                xhttp.open(\"GET\", \"/computar?name=\"+nameVar);\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "        </script>\n"
                + "\n"
                + "        </script>\n"
                + "    </body>\n"
                + "</html>";
        return htmlcode;
    }
 
    public static URI getRequestURI(String firstline) throws URISyntaxException {
        String ruri = firstline.split(" ")[1];
        return new URI(ruri);
    }
 
    public static String computeMathCommand(String command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        Class c = Math.class;
        Class [] parametersTypes = {double.class};
        Method m = c.getDeclaredMethod("abs", parametersTypes);
        Object[] params = {-2,0};
        String resp = m.invoke(null, (Object) params).toString();
        return "";
    }
    
    static Double[] bubbleSort(Double[] arr) {
        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    Double temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        return arr;
    }
}