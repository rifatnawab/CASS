import java.io.*;
import java.util.*;
import java.net.*;

public class client{
public static void main(String args[]) throws Exception{
Scanner sc=new Scanner(System.in);
Scanner in=new Scanner(System.in);
int ch;
System.out.println("Enter choice ");
ch=in.nextInt();
Socket clientSocket=new Socket("localhost",4000);

switch(ch){

case 1:
BufferedReader inFromUser1 = new BufferedReader(new FileReader("rifat.txt"));
String line1=inFromUser1.readLine();

//logic to encrypt line

DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
outToServer1.writeBytes(line1.toUpperCase() + '\n');

break;

case 2:
System.out.println("Enter the file name");
String line2=sc.nextLine();
DataOutputStream outToServer2 = new DataOutputStream(clientSocket.getOutputStream());
outToServer2.writeBytes(line2 + '\n');

BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
String recievedLine = inFromServer.readLine();

File file=new File("/C:/Users/Rifat/Desktop/rifat1.txt");
if(!file.exists()){
    file.createNewFile();
}

FileWriter fw = new FileWriter(file.getAbsoluteFile());
BufferedWriter bw = new BufferedWriter(fw);
bw.write(recievedLine);
bw.close();

break;

}
clientSocket.close();
}
}