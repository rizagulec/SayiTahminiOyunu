/*DERLEME KILAVUZU: 
 * Oyun konsolda çalışacaksa: Oyun.java dosyası application isimli bir paketin içine konur, Oyun.java' nın void main()' i çağırılır.
 * 	Eğer başka bir classtan çağırılacaksa bir adet oyun Oyun.(true) şeklinde constructorla instantiate edilir(true inputu: konsolda çalışma ayarlarını yapar) 
 *	ve Oyun.oynat() fonksiyonu çağırılır.
 * 
 * Oyun GUI ile çalışacaksa: Main.java ve Oyun.java dosyaları application isimli bir paketin içine konur. Main.java'nın void main' i çağırılır.
 * 	GUI' de sorunlar vardır. Detaylı bilgi Main.java GUI KULLANIM KILAVUZU' nda paylaşılmıştır.
 * 
 * OYUN KULLANIM KILAVUZU:
 * Merhaba, bu oyunda CPU ve kullanıcı akıllarından 4 basamaklı ve rakamları farklı bir sayı tutar(örn: 1234 gibi,1111 değil)
 * ve sırayla tahmin ederek karşı tarafın tuttuğu sayıyı bulmaya çalışırlar. Doğru tahmini ilk yapan kazanır. 
 * Doğru tahmin edilen her basamak için karşı tarafa o sayıda pozitif ipucu verilir.
 * Doğru tahmin edilen ama yanlış basamaktaki her rakam için karşı tarafa o sayıda negatif ipucu verilir.
 * Örnek: Doğru Sayı:1234 Tahmin:1239 ise pozitif:3 negatif:0 
 * Örnek: Doğru Sayı:1234 Tahmin:4321 ise pozitif:0 negatif:4
 * CPU gelen ipuçlarını bütün sayı kombinasyonlarında test ederek mümkün olmayan kombinasyonları eler ve en son kalan kombinasyonu doğru olarak tahmin eder.
 * Kullanıcı rakam(0-9 arası tam sayılar) olmayan sayı ya da karakter girerse, rakamları farklı olmayan sayı oluşturmaya çalışırsa, imkansız ipuçları oluşturmaya çalışırsa 
 * Uyarı alır ve tekrar giriş yapması istenir.
 * Kullanıcı ipuçlarını yanlış hesaplarsa tahmin edilecek olası kombinasyon kalmaz ve hakem hata bildirisi yapar.
 * Kullanıcının tuttuğu sayıyı oyuna girmesi yasaktır. Bu sebeple kullanıcı hata yaparak oyunu kazanırsa ne yazık ki hata tespiti yapılamaz.
 * İleride bu oyunun olası yasaksız versiyonuna kolaylıkla geçiş yapabilmek için eski ipuçları ve tahminler bu oyunda depolanır.
 * İşbu yasak kalkarsa hatanın yapıldığı tur numarası tespit edilebilir ve oyun oradan devam edebilir.
 */

package application;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Oyun {
	
	public static void main(String[] args) {
			System.out.println("Oyun/void main kullanıldı.");
			Oyun oyun=new Oyun(true);//true: konsolda çalıştırır
			oyun.oynat();
			
		}
	private boolean konsoldaCalismali; //true ise konsola göre ayar yapılır, false ise GUI' ye göre ayar yapılır.
	private boolean debugModu; //Debug modunda kullanıcı 1234 sayısını seçtiği var sayılır. İpuçları sistem tarafından hesaplanır. Debugging işlemi kolaylaşır.  
	private Scanner keyboard;
	private Integer cpununKonumu[]= {0,0,0,0,0}; //CPUnun seçtiği sayıyı temsil eder.
	private Integer cpununTahmini[]= {0,0,0,0,0}; //CPUnun tahmin ettiği sayıyı temsil eder.
	private Integer kullaniciTahmini[]= {0,0,0,0,0}; //Kullanıcının tahmin ettiği sayıyı temsil eder.
	private List<Integer[]>tahminler ; //CPUnun tahminleri burada depolanır.
	
	private List<Integer>cpuyaPozKayit ; //CPUya verilen pozitif ipuçlarını kaydeder. 
	private List<Integer>cpuyaNegKayit; //CPUya verilen negatif ipuçlarını kaydeder.
	private Integer kullaniciyaPoz; //Kullanıcıya verilen pozitif ipucu
	private Integer kullaniciyaNeg; //Kullanıcıya verilen negatif ipucu
    
   
	private boolean oyunBitmeli=false; //oyunun bitmesini gerektiren bir durumun varlığı
	private Random rand;
	
	private List<Integer[]>uzay; //tahmin edilebilecek geçerli sayı kombinasyonlarının listesi (olasılık uzayı)
	
    
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
							this.uzay.add((a)); //Geçerli sayı kombinasyonlarını listele.
						}	
					}		
				}	
			}
		}
	    this.cpununKonumu=this.uzay.get(this.rand.nextInt(this.uzay.size())); //CPU uzaydan rastgele bir sayı seçer
	    //System.out.println(cpununKonumu[0]+""+cpununKonumu[1]+""+cpununKonumu[2]+""+cpununKonumu[3]+"");
	    this.cpununTahmini=this.uzay.get(this.rand.nextInt(this.uzay.size())); //CPU ilk tahminini rastgele seçer
	
    }
	public void oynat() {
		
		aciklamaYap();
		while( ! this.oyunBitmeli) {
			
			kullaniciyaIpucVerTahminAl(); //Kullanıcıya varsa ipucu verilir ve tahmini alınır.
			gelenTahminiDegerlendir(); //Kullanıcının tahminine göre ipuçları hesaplanır.
			hakemeGit(); //Hakem oyunun bitip bitmemesine karar verir.
			
			tahminEt(); //Varsa ipuçları değerlendirilir ve tahmin yapılır.
			tahminSonuclariniIste();//Kullanıcıdan ipucu alınır.
			hakemeGit();//Hakem oyunun bitip bitmemesine karar verir.
		}
		
		
	}		
    
	private void aciklamaYap() {
		yazdir("Merhaba, bu oyunda CPU ve kullanıcı akıllarından 4 basamaklı ve rakamları farklı bir sayı tutar(örn: 1234 gibi,1111 değil)\n"
				+"ve sırayla tahmin ederek karşı tarafın tuttuğu sayıyı bulmaya çalışırlar. Doğru tahmini ilk yapan kazanır.\n" 
				+"Doğru tahmin edilen her basamak için karşı tarafa o sayıda pozitif ipucu verilir.\n"
				+"Doğru tahmin edilen ama yanlış basamaktaki her rakam için karşı tarafa o sayıda negatif ipucu verilir.\n"
				+"Örnek: Doğru Sayı:1234 Tahmin:1239 ise pozitif:3 negatif:0 \n"
				+"Örnek: Doğru Sayı:1234 Tahmin:4321 ise pozitif:0 negatif:4 \n"
				+"CPU gelen ipuçlarını bütün sayı kombinasyonlarında test ederek mümkün olmayan kombinasyonları eler ve en son kalan kombinasyonu doğru olarak tahmin eder.\n"
				+"Kullanıcının her şart altında integer değer gireceği var sayılmıştır.\n"
				+"Kullanıcı rakam(0-9 arası tam sayılar) olmayan sayı ya da karakter girerse, rakamları farklı olmayan sayı oluşturmaya çalışırsa, imkansız ipuçları oluşturmaya çalışırsa \n"
				+"Uyarı alır ve tekrar giriş yapması istenir.\n"
				+"Kullanıcı ipuçlarını yanlış hesaplarsa tahmin edilecek olası kombinasyon kalmaz ve hakem hata bildirisi yapar\n"
				+"Kullanıcının tuttuğu sayıyı oyuna girmesi yasaktır. Bu sebeple kullanıcı hata yaparak oyunu kazanırsa ne yazık ki hata tespiti yapılamaz.\n"
				+"İleride bu oyunun olası yasaksız versiyonuna kolaylıkla geçiş yapabilmek için eski ipuçları ve tahminler bu oyunda depolanır.\n"
				+"İşbu yasak kalkarsa hatanın yapıldığı tur numarası tespit edilebilir ve oyun oradan devam edebilir.\n"
				+"\n");
	}
    
	private void kullaniciyaIpucVerTahminAl() { //Varsa kullanıcıya ipucu verilir ve tahmini alınır.
		if(oyunBitmeli) {return;}
		
		if(tahminler.size()!=0) {
			int x=kullaniciTahmini[0];
			int y=kullaniciTahmini[1];
			int z=kullaniciTahmini[2];
			int w=kullaniciTahmini[3];
			yazdir("son tahmininiz: "+x+""+y+""+z+""+w+"" + " pozitifler: "+ kullaniciyaPoz + " negatifler " + kullaniciyaNeg + " Lutfen rakamları birer birer girerek tahminde bulununuz." );
		}
		
		else{yazdir("Lutfen rakamları birer birer girerek tahminde bulununuz.");}
		
		//tahmini al
		int x = -99;
		int y = -99;
		int z = -99;
		int w = -99;
		boolean hataliGiris=false;
		while(! farkliysaTrueDon(x,y,z,w)) { //rakamları farklı 4 basamaklı tahmin al.
			if(hataliGiris) {System.out.println("Hatalı giriş basamakları tekrar giriniz.");}
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
		
		testEt(kullaniciTahmini,0,0,cpununKonumu,true); //true: tahmin için ipucu oluşturur.
		
	}

	private void hakemeGit() {
		if(oyunBitmeli) {return;}
		
		if(cpuyaPozKayit.size()>0 && uzay.size()==0) { //Uzayda kombinasyon kalmadıysa hata vardır.
			yazdir("Hata tespit edildi.");
			oyunBitmeli=true;
			return;
		}
		if(kullaniciyaPoz==4) { //Kullanıcı 4 pozitif bildiyse kazanır.
			oyunBitmeli=true;
			yazdir("Kullanici kazandi.");
			return;
			}
		
		if(cpuyaPozKayit.size()>0 && cpuyaPozKayit.get(cpuyaPozKayit.size()-1)==4) { //CPU 4 pozitif bildiyse kazanır.
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
			uzayAzalt(); //tahmin öncesi imkansız kombinasyonları ele.
			
			if(uzay.size()==0) {
				
				hakemeGit(); //kombinasyon kalmadıysa hata vardır hakeme git.
				return;}
			tahminler.add(uzay.get(rand.nextInt(uzay.size()))); //uzaydan RASTGELE kombinasyon seç. Listenin başından da seçebilirdik ama o zaman kullanıcı her seferinde listenin sonundaki
																//değerlerden seçerek kazanma şansını artırabilirdi.
			
		}
		
	}

	private void tahminSonuclariniIste() {
		if(this.oyunBitmeli) {return;}
		
		int x=this.tahminler.get(this.tahminler.size()-1)[0];
		int y=this.tahminler.get(this.tahminler.size()-1)[1];
		int z=this.tahminler.get(this.tahminler.size()-1)[2];
		int w=this.tahminler.get(this.tahminler.size()-1)[3];
		System.out.println("Kalan kombinasyon sayısı: "+uzay.size());
		if(this.debugModu==true) {
			Integer[] a= {1,2,3,4};
			String dogruCevap = debugOtoIpucuHesapla(this.tahminler.get(this.tahminler.size()-1), 0, 0,a);
			yazdir("Tahminim: "+x+""+y+""+z+""+w+" Lütfen negatif ve pozitif ipuçlarını sırayla,- işareti kullanmadan giriniz. "+dogruCevap);}
		else{yazdir("Tahminim: "+x+""+y+""+z+""+w+" Lütfen negatif ve pozitif ipuçlarını sırayla,- işareti kullanmadan giriniz.");}
		
		int neg;
		int poz;
		boolean hataliGiris=false;
		do {//Kullanıcıdan ipucu alır.
			if(hataliGiris) {
				System.out.println("Hatalı giriş negatif ve pozitif ipuçlarını tekrar giriniz.");
			}
			neg=okut();
			poz=okut();
			hataliGiris=true;}
		while(poz==3&&neg==1 || poz+neg>4 || neg>4||neg<0 || poz>4||poz<0);
		cpuyaNegKayit.add(neg);
		cpuyaPozKayit.add(poz);
		
		
	}

	private boolean farkliysaTrueDon(int x, int y, int z, int w) {
    	//Inputta girilen sayılar farklıysa true döner.
		return x!=y && x!=z && x!=w &&
		y!=z && y!=w && 
		z!=w;
	}
    
    private boolean testEt(Integer[] yapilanTahmin,Integer gelenPoz,Integer gelenNeg,Integer[] dogruVarsayilan,boolean ipucVerilsin){
    	//Doğru olduğu varsayılan sayının, aynı tahmin için aynı ipuçlarını vermesi beklenir. 
		Integer pozitif=0;
		Integer negatif=0;
		for (int i=0;i<4;i++) {  //Doğru olduğu varsayılan sayı için pozitif sayısı hesaplar.
			if (yapilanTahmin[i]==dogruVarsayilan[i]) {
				pozitif++;
				
			}
		}
		for (int i=0;i<4;i++) {  //Doğru olduğu varsayılan sayı için negatif sayısı hesaplar.
			for (int j=0;j<4;j++) {
				if(i==j) {continue;}
				if (yapilanTahmin[i]==dogruVarsayilan[j]) {
					negatif++;
					
				}
			}
				
		}
		if(ipucVerilsin) {  //Kullanıcıya ipucu verir.
			this.kullaniciyaPoz=pozitif;
			this.kullaniciyaNeg=negatif;
		}
		if(negatif==gelenNeg&&pozitif==gelenPoz) { //Doğru varsayılan sayı testte aynı ipuçlarını üretiyorsa doğru olması mümkündür. Değilse o sayının seçilmediği anlaşılır. 
			
			return true;
		}
		
		return false;
	}
	
	private void uzayAzalt() {
		
		
		int j=tahminler.size()-1;  //Yapılan son tahminin indisi
		for(int i=0;i<this.uzay.size();i++) {
			if( ! testEt(this.tahminler.get(j),this.cpuyaPozKayit.get(j),this.cpuyaNegKayit.get(j),this.uzay.get(i),false)) {//Uzaydaki her elemanı test et. Testi geçemeyeni uzaydan at.
				this.uzay.remove(i);
				i--;
			}
		}
		
		//Aşağıdaki kod ileride kullanıcının girdiği sayıyı paylaşacağı bir versiyonda kullanılmak üzere tasarlanmıştır.
	/*	for(int j=0;j<this.tahminler.size();j++) {
			for(int i=0;i<this.uzay.size();i++) {
				if( ! testEt(this.tahminler.get(j),this.cpuyaPozKayit.get(j),this.cpuyaNegKayit.get(j),this.uzay.get(i),false)) {
					this.uzay.remove(i);
					i--;
				}	
			}
		} */
			
	
	}


	private Integer okut() {//İleride input arayüzden alınırsa bu fonksiyon düzenlenecektir.
		Integer a=-1;
		boolean hataligiris=false;
		while (a<0 || a>9)
		{
			if(hataligiris) {
				System.out.println("Hatali giriş tekrar rakam giriniz.");
			}
			String temp="";
		//	if( ! konsoldaCalismali) {temp=guiOku() ;} else{temp=keyboard.next();} //gui' den input almaya çalışınca gui kitleniyor
			temp=keyboard.next(); 			
			if (temp.equals("0") || temp.equals("1") || temp.equals("2") || temp.equals("3") || temp.equals("4") || temp.equals("5") 
					|| temp.equals("6") || temp.equals("7") || temp.equals("8") || temp.equals("9") ){  //girilenin rakam olup olmadığını kontrol et
				a=Integer.parseInt(temp);
				} 
			
			hataligiris=true;
		}
		
		return a;
	}

	private  String guiOku() { //GUI' den input alır. GUI' de hata olduğu için çalışmıyor.
		Main.inputIstendi=true; //GUI' den input istenmiştir.
		while( ! Main.inputGeldi) {} //GUI input döndürene kadar sonsuz döngüde bekle.
		Main.inputGeldi=false; //GUI input döndürdü, flagi resetle.
		
		return Main.input; //
		
	}

	private void yazdir(String string) {  //GUI Label' ına String yazdırır. 
		if( ! konsoldaCalismali) {Main.label.setText(string);}
		System.out.println(string);
		
		
	}
	
	
	private String debugOtoIpucuHesapla(Integer[] yapilanTahmin,Integer gelenPoz,Integer gelenNeg,Integer[] dogruVarsayilan){
		//testEt fonksiyonunun neredeyse aynısıdır. Varsayılan sayı için ipuçlarını otomatik hesaplar ve yazdırır. Debug işlemini hızlandırır. 
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
						
			return "default seçim olan '1234' için doğru cevap:"+Integer.toString(negatif) +" "+Integer.toString(pozitif);
		}
}
