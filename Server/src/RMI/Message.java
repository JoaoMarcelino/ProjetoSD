package RMI;

import java.io.*;
import java.util.*;
import java.time.*;

public class Message {
    public String tipo;
    public HashMap<String, String> pares;

    public Message(String message) {
        this.pares = new HashMap<String, String>();
        message = message.trim();
        String[] tuplos = message.split("\\|");
        for (String par : tuplos) {
            String chave = par.split(":")[0].trim();
            String valor = par.split(":")[1].trim();

            if (chave.equals("type")) {
                this.tipo = valor;
            } else {
                this.pares.put(chave, valor);
            }
        }

    }

    public String pack() {
        String str = "";

        str += "type:" + this.tipo;
        for (String i : this.pares.keySet()) {
            str += " | " + i + ":" + this.pares.get(i);
        }
        return str;
    }
}