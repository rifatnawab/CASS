import java.io.*; 
import java.net.*; 
import java.util.*;
import java.lang.*;
import java.math.BigInteger; 

class rserver
{ 
	public static void main(String[] args) throws IOException, SocketException
	{
		RSA rsa = new RSA(8);
		ServerSocket serverSock=new ServerSocket(3000);//server socket creation
		System.out.println("waiting for client.........");
		Socket socket=serverSock.accept();//listening client connection and accept the connection
		System.out.println("client connected ");
		//BufferedReader keyRead=new BufferedReader(new InputStreamReader(System.in));//reading from keyboard(keyRead object) 
		Scanner sc = new Scanner(System.in);
		OutputStream ostream=socket.getOutputStream();//sending to client
		PrintWriter pwrite=new PrintWriter(ostream,true); 
		InputStream istream=socket.getInputStream();//receiving from server(istream object)
		BufferedReader receiveRead=new BufferedReader(new InputStreamReader(istream));//Stream for receiving data from client
		pwrite.println((rsa.N).toString());
		System.out.flush();
		pwrite.println((rsa.E).toString());
		System.out.flush();
		String receiveMessage = null;
		if((receiveMessage=receiveRead.readLine())!=null)//receive from server 
			{ 
				BigInteger clientN=new BigInteger(receiveMessage);
				System.out.println("client:> N = "+receiveMessage); //displaying message
			} 
		if((receiveMessage=receiveRead.readLine())!=null)//receive from server 
		{ 
			BigInteger clientE=new BigInteger(receiveMessage);
			System.out.println("client:> E = "+receiveMessage); //displaying message
		} 
		
		byte[] receive = null;
		int bytesRead;
		StringBuilder sendMessage = new StringBuilder();
		while(true)
		{
			if((receiveMessage=receiveRead.readLine())!=null)
			{
				System.out.println("client:>"+ receiveMessage);//receive the message from client
				String authenticatedData = rsa.RSAauth(receiveMessage);
				String decryptedData = rsa.RSAdecrypt(authenticatedData);
				System.out.println("Decrypted Data: "+decryptedData);
			}
			String rawData = sc.next();
			String encryptedData = rsa.RSAencrypt(rawData);
			String signedData = rsa.RSAsign(encryptedData);
			System.out.println(signedData);
			pwrite.println(signedData);
			System.out.flush();//flush the Stream
			rsa.sb1.setLength(0);
			if(rawData.equals("bye"))
			{
				break;
			}	
		}
	}
}

class RSA
{
	/*** Bit length of each prime number.*/
	int primeSize ;
	/*** Two distinct large prime numbers p and q.*/
	BigInteger p, q ;
	/*** Modulus N.*/
	BigInteger N ;
	/*** r = ( p - 1 ) * ( q - 1 )*/
	BigInteger r ;
	/*** Public exponent E and Private exponent D*/
	BigInteger E, D ;
	String nt,dt,et;
	/*** Client side public key variables */
	BigInteger clientE;
	BigInteger clientN;
	/*** Constructor.** @param	primeSize		Bit length of each prime number.*/
	String publicKey;
	String privateKey;
	String randomNumber;
	BigInteger[] ciphertext;
	int m[]  = new int[1000];
	String st[]  = new String[1000];
	String str = "";
	String sarray1[] = new String[100000];
	StringBuffer sb1 = new StringBuffer();
	String inputMessage,encryptedData,decryptedMessage;
	public RSA( int primeSize )
	{
		Scanner sc = new Scanner(System.in);
		this.primeSize = primeSize ;
		// Generate two distinct large prime numbers p and q.
		generatePrimeNumbers() ;
		// Generate Public and Private Keys.
		generatePublicPrivateKeys() ;
		BigInteger publicKeyB = getE();
		BigInteger privateKeyB = getD();
		BigInteger randomNumberB = getN();
		publicKey = publicKeyB.toString();
		privateKey = privateKeyB.toString();
		randomNumber = randomNumberB.toString();
		System.out.println("Public Key (E,N): "+publicKey+","+randomNumber);
		System.out.println("Private Key (D,N): "+privateKey+","+randomNumber);
		//Encrypt data
		/*System.out.println("Enter Data to Encrypt");
		inputMessage=sc.next();
		encryptedData=RSAencrypt(inputMessage);
		System.out.println("Encrypted message"+encryptedData);
		//JOptionPane.showMessageDialog(null,"Encrypted Data "+"\n"+encryptedData);
		//Decrypt data
		decryptedMessage=RSAdecrypt();
		//JOptionPane.showMessageDialog(null,"Decrypted Data "+"\n"+decryptedMessage); */
	}
/*** Generate two distinct large prime numbers p and q.*/
	public void generatePrimeNumbers()
	{
		p = new BigInteger( primeSize, 10, new Random() ) ;
		do{
			q = new BigInteger( primeSize, 10, new Random() ) ;
		}
		while( q.compareTo( p ) == 0 ) ;
	}
/*** Generate Public and Private Keys.*/
	public void generatePublicPrivateKeys()
	{
		// N = p * q
		N = p.multiply( q ) ;
		// r = ( p - 1 ) * ( q - 1 )
		r = p.subtract( BigInteger.valueOf( 1 ) ) ;
		r = r.multiply( q.subtract( BigInteger.valueOf( 1 ) ) ) ;  //(p-1)(q-1)
		// Choose E, coprime to and less than r
		do{
			E = new BigInteger( 2 * primeSize, new Random() ) ;
		}
		while( ( E.compareTo( r ) != -1 ) || ( E.gcd( r ).compareTo( BigInteger.valueOf( 1 ) ) != 0 ) ) ;
		// Compute D, the inverse of E mod r
		D = E.modInverse( r ) ;
	}
	/*** Get prime number p.** @return Prime number p.*/
	public BigInteger getp(){
		return( p ) ;
	}
	/*** Get prime number q.** @return Prime number q.*/
	public BigInteger getq(){
		return( q ) ;
	}
	/*** Get r.* * @return	r.*/
	public BigInteger getr(){
		return( r ) ;
	}
/*** Get modulus N.** @return Modulus N.*/
	public BigInteger getN(){
		return( N ) ;
	}
/*** Get Public exponent E.** @return Public exponent E.*/
	public BigInteger getE(){
		return( E ) ;
	}
	/*** Get Private exponent D.* * @return Private exponent D.*/
	public BigInteger getD(){
		return( D ) ;
	}
/**Encryption */


	public String RSAencrypt(String info) {
		//E = new BigInteger(publicKey);
		//N = new BigInteger(randomNumber);
		E = clientE;
		N = clientN;
		try {
			ciphertext = encrypt( info ) ;
			for( int i = 0 ; i < ciphertext.length ; i++ ){
				m[i] = ciphertext[i].intValue();
				st[i] = String.valueOf(m[i]);
				sb1.append(st[i]);
				sb1.append(" ");
				str = sb1.toString();
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return str;
	}
	
	public String RSAsign(String info) {
		//E = new BigInteger(publicKey);
		//N = new BigInteger(randomNumber);
		//E = clientE;
		//N = clientN;
		E = new BigInteger(privateKey); // D = E
		N = new BigInteger(randomNumber);
		try {
		sb1.setLength(0);
			ciphertext = encrypt( info ) ;
			for( int i = 0 ; i < ciphertext.length ; i++ ){
				m[i] = ciphertext[i].intValue();
				st[i] = String.valueOf(m[i]);
				sb1.append(st[i]);
				sb1.append(" ");
				str = sb1.toString();
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return str;
	}
	
	public BigInteger[] encrypt( String message ){
		int i ;
		byte[] temp = new byte[1] ;
		byte[] digits = new byte[8];
		try {
			digits = message.getBytes() ;
			String ds = new String(digits);
			System.out.println("ds="+ds);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		BigInteger[] bigdigits = new BigInteger[digits.length] ;
		for( i = 0 ; i < bigdigits.length ; i++ ){
			temp[0] = digits[i] ;
			bigdigits[i] = new BigInteger( temp ) ;
		}
		BigInteger[] encrypted = new BigInteger[bigdigits.length] ;
		for( i = 0 ; i < bigdigits.length ; i++ )
			encrypted[i] = bigdigits[i].modPow( E, N ) ;
		return( encrypted ) ;
	}
/** Decryption */
	public String RSAdecrypt(String encryptedData) {
		D = new BigInteger(privateKey);
		N = new BigInteger(randomNumber);
		System.out.println("D = " + D);
		System.out.println("N = " + N);
		int k1= 0;
		StringTokenizer st = new StringTokenizer(encryptedData);
		while (st.hasMoreTokens()) {
			sarray1[k1] = st.nextToken(" ");
			k1++;
		}
		BigInteger[] ciphertext1 = new BigInteger[100000];
		for( int i = 0 ; i <k1 ; i++ ) {
			ciphertext1[i] = new BigInteger(sarray1[i]);
		}
		String recoveredPlaintext = decrypt( ciphertext1,D,N,k1) ;
		//System.out.println(recoveredPlaintext);
		return recoveredPlaintext;
	}
	
	public String RSAauth(String encryptedData) {
		//D = new BigInteger(privateKey);
		//N = new BigInteger(randomNumber);
		D =clientE; // E = D
		N = clientN;
		System.out.println("D = " + D);
		System.out.println("N = " + N);
		int k1= 0;
		StringTokenizer st = new StringTokenizer(encryptedData);
		while (st.hasMoreTokens()) {
			sarray1[k1] = st.nextToken(" ");
			k1++;
		}
		BigInteger[] ciphertext1 = new BigInteger[100000];
		for( int i = 0 ; i <k1 ; i++ ) {
			ciphertext1[i] = new BigInteger(sarray1[i]);
		}
		String recoveredPlaintext = decrypt( ciphertext1,D,N,k1) ;
		System.out.println(recoveredPlaintext);
		return recoveredPlaintext;
	}
	
	public String decrypt( BigInteger[] encrypted,BigInteger D,BigInteger N,int size )
	{
		int i ;
		String rs="";
		BigInteger[] decrypted = new BigInteger[size] ;
		for( i = 0 ; i < decrypted.length ; i++ ) {
			decrypted[i] = encrypted[i].modPow( D, N ) ;
		}
		char[] charArray = new char[decrypted.length] ;
		byte[] byteArray = new byte[decrypted.length] ;
		for( i = 0 ; i < charArray.length ; i++ ) {
			charArray[i] = (char) ( decrypted[i].intValue() ) ;
			Integer iv = new Integer(0);
			iv=decrypted[i].intValue() ;
			byteArray[i] = iv.byteValue();
		}
		try {
			rs=new String( byteArray );
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return(rs) ;
	}



}