import java.io.*;
import java.util.*;
import java.net.*;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class client{
public static void main(String args[]) throws Exception{
try{
Scanner sc=new Scanner(System.in);
Scanner in=new Scanner(System.in);
int ch;
System.out.println("Enter choice ");
ch=in.nextInt();
Socket clientSocket=new Socket("localhost",4000);
String key="squirrel123";

switch(ch){

case 1:
FileInputStream fis=new FileInputStream("rifat.txt");
FileOutputStream fos=new FileOutputStream("enrifat.txt");
encrypt(key,fis,fos);
fos.flush();
fos.close();
fis.close();

FileInputStream fin=new FileInputStream("enrifat.txt");
byte data[]=new byte[50];
int content;
fin.read(data);
String line1=new String(data);

//BufferedReader inFromUser1 = new BufferedReader(new FileReader("enrifat.txt"));
//String line1=inFromUser1.readLine();

DataOutputStream outToServer1 = new DataOutputStream(clientSocket.getOutputStream());
outToServer1.writeBytes(line1);

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

FileInputStream fis2=new FileInputStream("enrifat.txt");
FileOutputStream fos2=new FileOutputStream("rifat2.txt");
decrypt(key,fis2,fos2);
fos2.flush();
fos2.close();
fis2.close();
break;

}
clientSocket.close();
}
catch(Throwable e){
	e.printStackTrace();
}
}

public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
	}

public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
	}

public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}

public static void doCopy(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
	}


}
