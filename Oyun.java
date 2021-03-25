/*DERLEME KILAVUZU: 
 * Oyun konsolda �al��acaksa: Oyun.java dosyas� application isimli bir paketin i�ine konur, Oyun.java' n�n void main()' i �a��r�l�r.
 * 	E�er ba�ka bir classtan �a��r�lacaksa bir adet oyun instantiate edilir ve Oyun.oynat(true) fonksiyonu �a��r�l�r(true inputu: konsolda �al��ma ayarlar�n� yapar).
 * 
 * Oyun GUI ile �al��acaksa: Main.java ve Oyun.java dosyalar� application isimli bir paketin i�ine konur. Main.java'n�n void main' i �a��r�l�r.
 * 	GUI' de sorunlar vard�r. Detayl� bilgi Main.java GUI KULLANIM KILAVUZU' nda payla��lm��t�r.
 * 
 * OYUN KULLANIM KILAVUZU:
 * Merhaba, bu oyunda CPU ve kullan�c� ak�llar�ndan 4 basamakl� ve rakamlar� farkl� bir say� tutar(�rn: 1234 gibi,1111 de�il)
 * ve s�rayla tahmin ederek kar�� taraf�n tuttu�u say�y� bulmaya �al���rlar. Do�ru tahmini ilk yapan kazan�r. 
 * Do�ru tahmin edilen her basamak i�in kar�� tarafa o say�da pozitif ipucu verilir.
 * Do�ru tahmin edilen ama yanl�� basamaktaki her rakam i�in kar�� tarafa o say�da negatif ipucu verilir.
 * �rnek: Do�ru Say�:1234 Tahmin:1239 ise pozitif:3 negatif:0 
 * �rnek: Do�ru Say�:1234 Tahmin:4321 ise pozitif:0 negatif:4
 * CPU gelen ipu�lar�n� b�t�n say� kombinasyonlar�nda test ederek m�mk�n olmayan kombinasyonlar� eler ve en son kalan kombinasyonu do�ru olarak tahmin eder.
 * Kullan�c�n�n her �art alt�nda integer de�er girece�i var say�lm��t�r.
 * Kullan�c� rakam(0-9 aras� tam say�lar) olmayan say� ya da karakter girerse, rakamlar� farkl� olmayan say� olu�turmaya �al���rsa, imkans�z ipu�lar� olu�turmaya �al���rsa 
 * Uyar� al�r ve tekrar giri� yapmas� istenir.
 * Kullan�c� ipu�lar�n� yanl�� hesaplarsa tahmin edilecek olas� kombinasyon kalmaz ve hakem hata bildirisi yapar.
 * Kullan�c�n�n tuttu�u say�y� oyuna girmesi yasakt�r. Bu sebeple kullan�c� hata yaparak oyunu kazan�rsa ne yaz�k ki hata tespiti yap�lamaz.
 * �leride bu oyunun olas� yasaks�z versiyonuna kolayl�kla ge�i� yapabilmek i�in eski ipu�lar� ve tahminler bu oyunda depolan�r.
 * ��bu yasak kalkarsa hatan�n yap�ld��� tur numaras� tespit edilebilir ve oyun oradan devam edebilir.
 */

package application;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Oyun {
	
	public static void main(String[] args) {
			System.out.println("Oyun/void main kullan�ld�.");
			Oyun oyun=new Oyun(true);//true: konsolda �al��t�r�r
			oyun.oynat();
			
		}
	private boolean konsoldaCalismali; //true ise konsola g�re ayar yap�l�r, false ise GUI' ye g�re ayar yap�l�r.
	private boolean debugModu; //Debug modunda kullan�c� 1234 say�s�n� se�ti�i var say�l�r. �pu�lar� sistem taraf�ndan hesaplan�r. Debugging i�lemi kolayla��r.  
	private Scanner keyboard;
	private Integer cpununKonumu[]= {0,0,0,0,0}; //CPUnun se�ti�i say�y� temsil eder.
	private Integer cpununTahmini[]= {0,0,0,0,0}; //CPUnun tahmin etti�i say�y� temsil eder.
	private Integer kullaniciTahmini[]= {0,0,0,0,0}; //Kullan�c�n�n tahmin etti�i say�y� temsil eder.
	private List<Integer[]>tahminler ; //CPUnun tahminleri burada depolan�r.
	
	private List<Integer>cpuyaPozKayit ; //CPUya verilen pozitif ipu�lar�n� kaydeder. 
	private List<Integer>cpuyaNegKayit; //CPUya verilen negatif ipu�lar�n� kaydeder.
	private Integer kullaniciyaPoz; //Kullan�c�ya verilen pozitif ipucu
	private Integer kullaniciyaNeg; //Kullan�c�ya verilen negatif ipucu
    
   
	private boolean oyunBitmeli=false; //oyunun bitmesini gerektiren bir durumun varl���
	private Random rand;
	
	private List<Integer[]>uzay; //tahmin edilebilecek ge�erli say� kombinasyonlar�n�n listesi (olas�l�k uzay�)
	
    
    public Oyun(boolean konsoldaCalismali) {
    	this.konsoldaCalismali=konsoldaCalismali;
    	this.debugModu=true;
    	this.keyboard = new Scanner(System.in);
    	this.tahminler = new ArrayList<Integer[]>();
    	this.cpuyaPozKayit = new ArrayList<Integer>();
    	this.cpuyaNegKayit = new ArrayList<Integer>();
	
    	this.rand=new Random();
    	this.uzay = new ArrayList<Integer[]>();
	    for (int x=0;x<10;x++) {
			for (int y=0;y<10;y++) {
				for (int z=0;z<10;z++) {
					for (int w=0;w<10;w++) {
						if(farkliysaTrueDon(x,y,z,w)) {

							Integer[] a= {x,y,z,w}; 
							this.uzay.add((a)); //Ge�erli say� kombinasyonlar�n� listele.
						}	
					}		
				}	
			}
		}
	    this.cpununKonumu=this.uzay.get(this.rand.nextInt(this.uzay.size())); //CPU uzaydan rastgele bir say� se�er
	    //System.out.println(cpununKonumu[0]+""+cpununKonumu[1]+""+cpununKonumu[2]+""+cpununKonumu[3]+"");
	    this.cpununTahmini=this.uzay.get(this.rand.nextInt(this.uzay.size())); //CPU ilk tahminini rastgele se�er
	
    }
	public void oynat() {
		
		aciklamaYap();
		while( ! this.oyunBitmeli) {
			
			kullaniciyaIpucVerTahminAl(); //Kullan�c�ya varsa ipucu verilir ve tahmini al�n�r.
			gelenTahminiDegerlendir(); //Kullan�c�n�n tahminine g�re ipu�lar� hesaplan�r.
			hakemeGit(); //Hakem oyunun bitip bitmemesine karar verir.
			
			tahminEt(); //Varsa ipu�lar� de�erlendirilir ve tahmin yap�l�r.
			tahminSonuclariniIste();//Kullan�c�dan ipucu al�n�r.
			hakemeGit();//Hakem oyunun bitip bitmemesine karar verir.
		}
		
		
	}		
    
	private void aciklamaYap() {
		yazdir("Merhaba, bu oyunda CPU ve kullan�c� ak�llar�ndan 4 basamakl� ve rakamlar� farkl� bir say� tutar(�rn: 1234 gibi,1111 de�il)\n"
				+"ve s�rayla tahmin ederek kar�� taraf�n tuttu�u say�y� bulmaya �al���rlar. Do�ru tahmini ilk yapan kazan�r.\n" 
				+"Do�ru tahmin edilen her basamak i�in kar�� tarafa o say�da pozitif ipucu verilir.\n"
				+"Do�ru tahmin edilen ama yanl�� basamaktaki her rakam i�in kar�� tarafa o say�da negatif ipucu verilir.\n"
				+"�rnek: Do�ru Say�:1234 Tahmin:1239 ise pozitif:3 negatif:0 \n"
				+"�rnek: Do�ru Say�:1234 Tahmin:4321 ise pozitif:0 negatif:4 \n"
				+"CPU gelen ipu�lar�n� b�t�n say� kombinasyonlar�nda test ederek m�mk�n olmayan kombinasyonlar� eler ve en son kalan kombinasyonu do�ru olarak tahmin eder.\n"
				+"Kullan�c�n�n her �art alt�nda integer de�er girece�i var say�lm��t�r.\n"
				+"Kullan�c� rakam(0-9 aras� tam say�lar) olmayan say� ya da karakter girerse, rakamlar� farkl� olmayan say� olu�turmaya �al���rsa, imkans�z ipu�lar� olu�turmaya �al���rsa \n"
				+"Uyar� al�r ve tekrar giri� yapmas� istenir.\n"
				+"Kullan�c� ipu�lar�n� yanl�� hesaplarsa tahmin edilecek olas� kombinasyon kalmaz ve hakem hata bildirisi yapar\n"
				+"Kullan�c�n�n tuttu�u say�y� oyuna girmesi yasakt�r. Bu sebeple kullan�c� hata yaparak oyunu kazan�rsa ne yaz�k ki hata tespiti yap�lamaz.\n"
				+"�leride bu oyunun olas� yasaks�z versiyonuna kolayl�kla ge�i� yapabilmek i�in eski ipu�lar� ve tahminler bu oyunda depolan�r.\n"
				+"��bu yasak kalkarsa hatan�n yap�ld��� tur numaras� tespit edilebilir ve oyun oradan devam edebilir.\n"
				+"\n");
	}
    
	private void kullaniciyaIpucVerTahminAl() {
		if(oyunBitmeli) {return;}
		
		if(tahminler.size()!=0) {
			int x=kullaniciTahmini[0];
			int y=kullaniciTahmini[1];
			int z=kullaniciTahmini[2];
			int w=kullaniciTahmini[3];
			yazdir("son tahmininiz: "+x+""+y+""+z+""+w+"" + " pozitifler: "+ kullaniciyaPoz + " negatifler " + kullaniciyaNeg + " Lutfen rakamlar� birer birer girerek tahminde bulununuz." );
		}
		
		else{yazdir("Lutfen rakamlar� birer birer girerek tahminde bulununuz.");}
		
		//tahmini al
		int x = -99;
		int y = -99;
		int z = -99;
		int w = -99;
		boolean hataliGiris=false;
		while(! farkliysaTrueDon(x,y,z,w)) { //rakamlar� farkl� 4 basamakl� tahmin al.
			if(hataliGiris) {System.out.println("Hatal� giri� basamaklar� tekrar giriniz.");}
			x = okut();
			y = okut();
			z = okut();
			w = okut();
			hataliGiris=true;
		
		}
		kullaniciTahmini[0]= x;
		kullaniciTahmini[1]= y;
		kullaniciTahmini[2]= z;
		kullaniciTahmini[3]= w;
		
		
		
	}

	private void gelenTahminiDegerlendir() {
		if(oyunBitmeli) {return;}
		
		testEt(kullaniciTahmini,0,0,cpununKonumu,true); //true: tahmin i�in ipucu olu�turur.
		
	}

	private void hakemeGit() {
		if(oyunBitmeli) {return;}
		
		if(cpuyaPozKayit.size()>0 && uzay.size()==0) { //Uzayda kombinasyon kalmad�ysa hata vard�r.
			yazdir("Hata tespit edildi.");
			oyunBitmeli=true;
			return;
		}
		if(kullaniciyaPoz==4) { //Kullan�c� 4 pozitif bildiyse kazan�r.
			oyunBitmeli=true;
			yazdir("Kullanici kazandi.");
			return;
			}
		
		if(cpuyaPozKayit.size()>0 && cpuyaPozKayit.get(cpuyaPozKayit.size()-1)==4) { //CPU 4 pozitif bildiyse kazan�r.
			oyunBitmeli=true;
			yazdir("CPU kazandi.");
			return;
			}		
	}

	private void tahminEt() {
		if(oyunBitmeli) {return;}
		
		if(tahminler.size()==0) {
			tahminler.add(cpununTahmini);}
		else {
			uzayAzalt(); //tahmin �ncesi imkans�z kombinasyonlar� ele.
			
			if(uzay.size()==0) {
				
				hakemeGit(); //kombinasyon kalmad�ysa hata vard�r hakeme git.
				return;}
			tahminler.add(uzay.get(rand.nextInt(uzay.size()))); //uzaydan RASTGELE kombinasyon se�. Listenin ba��ndan da se�ebilirdik ama o zaman kullan�c� her seferinde listenin sonundaki
																//de�erlerden se�erek kazanma �ans�n� art�rabilirdi.
			
		}
		
	}

	private void tahminSonuclariniIste() {
		if(this.oyunBitmeli) {return;}
		
		int x=this.tahminler.get(this.tahminler.size()-1)[0];
		int y=this.tahminler.get(this.tahminler.size()-1)[1];
		int z=this.tahminler.get(this.tahminler.size()-1)[2];
		int w=this.tahminler.get(this.tahminler.size()-1)[3];
		System.out.println("Kalan kombinasyon say�s�: "+uzay.size());
		if(this.debugModu==true) {
			Integer[] a= {1,2,3,4};
			String dogruCevap = debugOtoIpucuHesapla(this.tahminler.get(this.tahminler.size()-1), 0, 0,a);
			yazdir("Tahminim: "+x+""+y+""+z+""+w+" L�tfen negatif ve pozitif ipu�lar�n� s�rayla,- i�areti kullanmadan giriniz. "+dogruCevap);}
		else{yazdir("Tahminim: "+x+""+y+""+z+""+w+" L�tfen negatif ve pozitif ipu�lar�n� s�rayla,- i�areti kullanmadan giriniz.");}
		
		int neg;
		int poz;
		boolean hataliGiris=false;
		do {//Kullan�c�dan ipucu al�r.
			if(hataliGiris) {
				System.out.println("Hatal� giri� negatif ve pozitif ipu�lar�n� tekrar giriniz.");
			}
			neg=okut();
			poz=okut();
			hataliGiris=true;}
		while(poz==3&&neg==1 || poz+neg>4 || neg>4||neg<0 || poz>4||poz<0);
		cpuyaNegKayit.add(neg);
		cpuyaPozKayit.add(poz);
		
		
	}

	private boolean farkliysaTrueDon(int x, int y, int z, int w) {
    	//Inputta girilen say�lar farkl�ysa true d�ner.
		return x!=y && x!=z && x!=w &&
		y!=z && y!=w && 
		z!=w;
	}
    
    private boolean testEt(Integer[] yapilanTahmin,Integer gelenPoz,Integer gelenNeg,Integer[] dogruVarsayilan,boolean ipucVerilsin){
    	//Do�ru oldu�u varsay�lan say�n�n, ayn� tahmin i�in ayn� ipu�lar�n� vermesi beklenir. 
		Integer pozitif=0;
		Integer negatif=0;
		for (int i=0;i<4;i++) {  //Do�ru oldu�u varsay�lan say� i�in pozitif say�s� hesaplar.
			if (yapilanTahmin[i]==dogruVarsayilan[i]) {
				pozitif++;
				
			}
		}
		for (int i=0;i<4;i++) {  //Do�ru oldu�u varsay�lan say� i�in negatif say�s� hesaplar.
			for (int j=0;j<4;j++) {
				if(i==j) {continue;}
				if (yapilanTahmin[i]==dogruVarsayilan[j]) {
					negatif++;
					
				}
			}
				
		}
		if(ipucVerilsin) {  //Kullan�c�ya ipucu verir.
			this.kullaniciyaPoz=pozitif;
			this.kullaniciyaNeg=negatif;
		}
		if(negatif==gelenNeg&&pozitif==gelenPoz) { //Do�ru varsay�lan say� testte ayn� ipu�lar�n� �retiyorsa do�ru olmas� m�mk�nd�r. De�ilse o say�n�n se�ilmedi�i anla��l�r. 
			
			return true;
		}
		
		return false;
	}
	
	private void uzayAzalt() {
		
		
		int j=tahminler.size()-1;  //Yap�lan son tahminin indisi
		for(int i=0;i<this.uzay.size();i++) {
			if( ! testEt(this.tahminler.get(j),this.cpuyaPozKayit.get(j),this.cpuyaNegKayit.get(j),this.uzay.get(i),false)) {//Uzaydaki her eleman� test et. Testi ge�emeyeni uzaydan at.
				this.uzay.remove(i);
				i--;
			}
		}
		
		//A�a��daki kod ileride kullan�c�n�n girdi�i say�y� payla�aca�� bir versiyonda kullan�lmak �zere tasarlanm��t�r.
	/*	for(int j=0;j<this.tahminler.size();j++) {
			for(int i=0;i<this.uzay.size();i++) {
				if( ! testEt(this.tahminler.get(j),this.cpuyaPozKayit.get(j),this.cpuyaNegKayit.get(j),this.uzay.get(i),false)) {
					this.uzay.remove(i);
					i--;
				}	
			}
		} */
			
	
	}


	private Integer okut() {//�leride input aray�zden al�n�rsa bu fonksiyon d�zenlenecektir.
		Integer a=-1;
		boolean hataligiris=false;
		while (a<0 || a>9)
		{
			if(hataligiris) {
				System.out.println("Hatali giri� tekrar rakam giriniz.");
			}
			String temp="";
		//	if( ! konsoldaCalismali) {temp=guiOku() ;} else{temp=keyboard.next();} //gui' den input almaya �al���nca gui kitleniyor
			temp=keyboard.next(); 			
			if (temp.equals("0") || temp.equals("1") || temp.equals("2") || temp.equals("3") || temp.equals("4") || temp.equals("5") 
					|| temp.equals("6") || temp.equals("7") || temp.equals("8") || temp.equals("9") ){  //girilenin rakam olup olmad���n� kontrol et
				a=Integer.parseInt(temp);
				} 
			
			hataligiris=true;
		}
		
		return a;
	}

	private  String guiOku() { //GUI' den input al�r. GUI' de hata oldu�u i�in �al��m�yor.
		Main.inputIstendi=true; //GUI' den input istenmi�tir.
		while( ! Main.inputGeldi) {} //GUI input d�nd�rene kadar sonsuz d�ng�de bekle.
		Main.inputGeldi=false; //GUI input d�nd�rd�, flagi resetle.
		
		return Main.input; //
		
	}

	private void yazdir(String string) {  //GUI Label' �na String yazd�r�r. 
		if( ! konsoldaCalismali) {Main.label.setText(string);}
		System.out.println(string);
		
		
	}
	
	
	private String debugOtoIpucuHesapla(Integer[] yapilanTahmin,Integer gelenPoz,Integer gelenNeg,Integer[] dogruVarsayilan){
		//testEt fonksiyonunun neredeyse ayn�s�d�r. Varsay�lan say� i�in ipu�lar�n� otomatik hesaplar ve yazd�r�r. Debug i�lemini h�zland�r�r. 
			Integer pozitif=0;
			Integer negatif=0;
			for (int i=0;i<4;i++) {
				if (yapilanTahmin[i]==dogruVarsayilan[i]) {
					pozitif++;
					
				}
			}
			for (int i=0;i<4;i++) {
				for (int j=0;j<4;j++) {
					if(i==j) {continue;}
					if (yapilanTahmin[i]==dogruVarsayilan[j]) {
						negatif++;
						
					}
				}
					
			}
						
			return "default se�im olan '1234' i�in do�ru cevap:"+Integer.toString(negatif) +" "+Integer.toString(pozitif);
		}
}
