package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    public static void main(String[] args) throws IOException {
        final String HOME = System.getProperty("user.home");

        ServerSocket serverSocket = new ServerSocket(7000);
        System.out.println("==Servidor==");
        while(true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            
            try {
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                String result = "";
                while(true) {
                    String clienteInput = dis.readUTF();
                    if (clienteInput.equalsIgnoreCase("readdir")) {
                        File arquivo = new File(HOME);
                        result = Arrays.stream(arquivo.list()).toList().toString();
                        dos.writeUTF(result);
                    } else if (clienteInput.split(" ")[0].equalsIgnoreCase("create")) {
                        File arquivo = new File(HOME + "\\" + clienteInput.split(" ")[1]);
                        if (arquivo.createNewFile()) {
                            result = "Arquivo criado com sucesso";
                        } else {
                            result = "Arquivo não criado com sucesso";
                        }
                        dos.writeUTF(result);
                    } else if (clienteInput.split(" ")[0].equalsIgnoreCase("rename")){
                        File antiguinho = new File(HOME + "\\" + clienteInput.split(" ")[1]);
                        File novinho = new File(HOME + "\\" + clienteInput.split(" ")[2]);
                        if (antiguinho.renameTo(novinho)) {
                            result = "Arquivo foi renomeado";
                        } else {
                            result = "Deu falha na renomeação";
                        }
                        dos.writeUTF(result);
                    } else if (clienteInput.split(" ")[0].equalsIgnoreCase("remove")) {
                        File arquivo = new File(HOME + "\\" + clienteInput.split(" ")[1]);
                        if (arquivo.delete()) {
                            result = "Arquivo deletado";
                        } else {
                            result = "Arquivo não deletado";
                        }
                        dos.writeUTF(result);
                    }           
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
