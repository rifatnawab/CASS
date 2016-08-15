import java.io.*;
import java.net.*;
import java.util.*;

public class server{
public static void main(String args[]) throws Exception{
String clientLine;
ServerSocket welcomeSocket=new ServerSocket(4000);

while(true)
{
Socket connectionSocket=welcomeSocket.accept();
BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

clientLine=inFromClient.readLine();
if(clientLine.endsWith(".txt")){
BufferedReader br = new BufferedReader(new FileReader(clientLine));
String input=br.readLine();

DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
outToClient.writeBytes(input + '\n');

}
else{
File file=new File("/C:/Users/Rifat/Desktop/niki.txt");
if(!file.exists()){
    file.createNewFile();
}

FileWriter fw = new FileWriter(file.getAbsoluteFile());
BufferedWriter bw = new BufferedWriter(fw);
bw.write(clientLine);
bw.close();
}
}

}
}