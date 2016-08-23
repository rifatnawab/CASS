import java.io.*;
import java.util.*;
import java.net.*;

public class machine1{
	public static void main(String args[]) throws Exception{
		Scanner sc=new Scanner(System.in);
		int ch;
		
		//key generation for this machine
		
		System.out.println("Enter your choice\n1.Encrypt/Decrypt\n2.Digital Signature ");
		ch=sc.nextInt();
		switch(ch){
			case 1:
			System.out.println("Hello");
			break;
			
			case 2:
			System.out.println("World");
			break;
			
		}
	}
}