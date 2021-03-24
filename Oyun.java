package application;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Oyun {
	
	public static void main(String[] args) {
			System.out.println("oyun void main kullanýldý.");
			Oyun oyun=new Oyun();
			oyun.oynat();
			
		}
	private boolean debugModu; //Debug modunda kullanýcý 1234 sayýsýný seçtiði var sayýlýr. Ýpuçlarý sistem tarafýndan hesaplanýr. Debugging iþlemi kolaylaþýr.  
	private Scanner keyboard;
	private Integer cpununKonumu[]= {0,0,0,0,0}; //CPUnun seçtiði sayýyý temsil eder.
	private Integer cpununTahmini[]= {0,0,0,0,0}; //CPUnun tahmin ettiði sayýyý temsil eder.
	private Integer kullaniciTahmini[]= {0,0,0,0,0}; //Kullanýcýnýn tahmin ettiði sayýyý temsil eder.
	private List<Integer[]>tahminler ; //CPUnun tahminleri burada depolanýr.
	
	private List<Integer>cpuyaPozKayit ; //CPUya verilen pozitif ipuçlarýný kaydeder. 
	private List<Integer>cpuyaNegKayit; //CPUya verilen negatif ipuçlarýný kaydeder.
	private Integer kullaniciyaPoz; //Kullanýcýya verilen pozitif ipucu
	private Integer kullaniciyaNeg; //Kullanýcýya verilen negatif ipucu
    
   
	private boolean oyunBitmeli=false; //oyunun bitmesini gerektiren bir durumun varlýðý
	private Random rand;
	
	private List<Integer[]>uzay; //tahmin edilebilecek geçerli sayý kombinasyonlarýnýn listesi (olasýlýk uzayý)
	
    
    public Oyun() {
    	//System.out.println("oyun instant");
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
							//uzay[x][y][z][w]=1;
							Integer[] a= {x,y,z,w}; 
							this.uzay.add((a)); //Geçerli sayý kombinasyonlarýný listele.
						}	
					}		
				}	
			}
		}
	    this.cpununKonumu=this.uzay.get(this.rand.nextInt(this.uzay.size())); //CPU uzaydan rastgele bir sayý seçer
	    this.cpununTahmini=this.uzay.get(this.rand.nextInt(this.uzay.size())); //CPU ilk tahminini rastgele seçer
	
    }
	public void oynat() {
		
		aciklamaYap();
		while( ! this.oyunBitmeli) {
			
			kullaniciyaIpucVerTahminAl(); //Kullanýcýya varsa ipucu verilir ve tahmini alýnýr.
			gelenTahminiDegerlendir(); //Kullanýcýnýn tahminine göre ipuçlarý hesaplanýr.
			hakemeGit(); //Hakem oyunun bitip bitmemesine karar verir.
			
			tahminEt(); //Varsa ipuçlarý deðerlendirilir ve tahmin yapýlýr.
			tahminSonuclariniIste();//Kullanýcýdan ipucu alýnýr.
			hakemeGit();//Hakem oyunun bitip bitmemesine karar verir.
		}
		
		
	}		
    
	private void aciklamaYap() {
		yazdir("Merhaba, bu oyunda CPU ve kullanýcý akýllarýndan 4 basamaklý ve rakamlarý farklý bir sayý tutar(örn: 1234 gibi,1111 deðil)");
		yazdir("ve sýrayla tahmin ederek karþý tarafýn tuttuðu sayýyý bulmaya çalýþýrlar. Doðru tahmini ilk yapan kazanýr.");
		yazdir("Doðru tahmin edilen her basamak sayýsý için karþý tarafa o sayýda pozitif ipucu verilir.");
		yazdir("Doðru tahmin edilen ama yanlýþ basamaktaki her sayý için karþý tarafa o sayýda negatif ipucu verilir.");
		yazdir("Örnek: Doðru Sayý:1234 Tahmin:1239 ise pozitif:3 negatif:0");
		yazdir("Örnek: Doðru Sayý:1234 Tahmin:4321 ise pozitif:0 negatif:4");
		yazdir("CPU gelen ipuçlarýný bütün sayý kombinasyonlarýnda test ederek mümkün olmayan kombinasyonlarý eler ve en son kalan kombinasyonu doðru olarak tahmin eder.");
		yazdir("Kullanýcýnýn her þart altýnda integer deðer gireceði var sayýlmýþtýr.");
		yazdir("Kullanýcý rakam(0-9 arasý sayýlar) harici deðer girerse, rakamlarý farklý olmayan sayý oluþturmaya çalýþýrsa, imkansýz ipuçlarý oluþturmaya çalýþýrsa ");
		yazdir("Uyarý alýr ve tekrar giriþ yapmasý istenir.");
		yazdir("Kullanýcý ipuçlarýný yanlýþ hesaplarsa tahmin edilecek olasý kombinasyon kalmaz ve hakem hata bildirisi yapar");
		yazdir("Kullanýcýnýn tuttuðu sayýyý oyuna girmesi yasaktýr. Bu sebeple kullanýcý hata yaparak oyunu kazanýrsa ne yazýk ki hata tespiti yapýlamaz.");
		yazdir("Ýleride bu oyunun olasý yasaksýz versiyonuna kolaylýkla geçiþ yapabilmek için eski ipuçlarý ve tahminler bu oyunda depolanýr.");
		yazdir("Ýþbu yasak kalkarsa hatanýn yapýldýðý tur numarasý tespit edilebilir ve oyun oradan devam edebilir.");
		yazdir("");
	}
    
	private void kullaniciyaIpucVerTahminAl() {
		if(oyunBitmeli) {return;}
		
		if(tahminler.size()!=0) {
			int x=kullaniciTahmini[0];
			int y=kullaniciTahmini[1];
			int z=kullaniciTahmini[2];
			int w=kullaniciTahmini[3];
			yazdir("son tahmininiz: "+x+""+y+""+z+""+w+"" + " pozitifler: "+ kullaniciyaPoz + " negatifler " + kullaniciyaNeg + " Lutfen rakamlarý birer birer girerek tahminde bulununuz." );
		}
		
		else{yazdir("Lutfen rakamlarý birer birer girerek tahminde bulununuz.");}
		
		//tahmini al
		int x = -99;
		int y = -99;
		int z = -99;
		int w = -99;
		boolean hataliGiris=false;
		while(! farkliysaTrueDon(x,y,z,w)) { //rakamlarý farklý 4 basamaklý tahmin al.
			if(hataliGiris) {System.out.println("Hatalý giriþ basamaklarý tekrar giriniz.");}
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
		
		testEt(kullaniciTahmini,0,0,cpununKonumu,true); //true: tahmin için ipucu oluþturur.
		
	}

	private void hakemeGit() {
		if(oyunBitmeli) {return;}
		
		if(cpuyaPozKayit.size()>0 && uzay.size()==0) { //Uzayda kombinasyon kalmadýysa hata vardýr.
			yazdir("Hata tespit edildi.");
			oyunBitmeli=true;
			return;
		}
		if(kullaniciyaPoz==4) { //Kullanýcý 4 pozitif bildiyse kazanýr.
			oyunBitmeli=true;
			yazdir("Kullanici kazandi.");
			return;
			}
		
		if(cpuyaPozKayit.size()>0 && cpuyaPozKayit.get(cpuyaPozKayit.size()-1)==4) { //CPU 4 pozitif bildiyse kazanýr.
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
			uzayAzalt(); //tahmin öncesi imkansýz kombinasyonlarý ele.
			
			if(uzay.size()==0) {
				
				hakemeGit(); //kombinasyon kalmadýysa hata vardýr hakeme git.
				return;}
			tahminler.add(uzay.get(rand.nextInt(uzay.size()))); //uzaydan RASTGELE kombinasyon seç. Listenin baþýndan da seçebilirdik ama o zaman kullanýcý her seferinde listenin sonundaki
																//deðerlerden seçerek kazanma þansýný artýrabilirdi.
			
		}
		
	}

	private void tahminSonuclariniIste() {
		if(this.oyunBitmeli) {return;}
		
		int x=this.tahminler.get(this.tahminler.size()-1)[0];
		int y=this.tahminler.get(this.tahminler.size()-1)[1];
		int z=this.tahminler.get(this.tahminler.size()-1)[2];
		int w=this.tahminler.get(this.tahminler.size()-1)[3];
		System.out.println("Kalan kombinasyon sayýsý: "+uzay.size());
		if(this.debugModu==true) {
			Integer[] a= {1,2,3,4};
			String dogruCevap = debugOtoIpucuHesapla(this.tahminler.get(this.tahminler.size()-1), 0, 0,a);
			yazdir("Tahminim: "+x+""+y+""+z+""+w+" Lütfen negatif ve pozitif ipuçlarýný sýrayla,- iþareti kullanmadan giriniz. "+dogruCevap);}
		else{yazdir("Tahminim: "+x+""+y+""+z+""+w+" Lütfen negatif ve pozitif ipuçlarýný sýrayla,- iþareti kullanmadan giriniz.");}
		
		int neg;
		int poz;
		boolean hataliGiris=false;
		do {//Kullanýcýdan ipucu alýr.
			if(hataliGiris) {
				System.out.println("Hatalý giriþ negatif ve pozitif ipuçlarýný tekrar giriniz.");
			}
			neg=okut();
			poz=okut();
			hataliGiris=true;}
		while(poz==3&&neg==1 || poz+neg>4 || neg>4||neg<0 || poz>4||poz<0);
		cpuyaNegKayit.add(neg);
		cpuyaPozKayit.add(poz);
		
		
	}

	private boolean farkliysaTrueDon(int x, int y, int z, int w) {
    	//Inputta girilen sayýlar farklýysa true döner.
		return x!=y && x!=z && x!=w &&
		y!=z && y!=w && 
		z!=w;
	}
    
    private boolean testEt(Integer[] yapilanTahmin,Integer gelenPoz,Integer gelenNeg,Integer[] dogruVarsayilan,boolean ipucVerilsin){
    	//Doðru olduðu varsayýlan sayýnýn, ayný tahmin için ayný ipuçlarýný vermesi beklenir. 
		Integer pozitif=0;
		Integer negatif=0;
		for (int i=0;i<4;i++) {  //Doðru olduðu varsayýlan sayý için pozitif sayýsý hesaplar.
			if (yapilanTahmin[i]==dogruVarsayilan[i]) {
				pozitif++;
				
			}
		}
		for (int i=0;i<4;i++) {  //Doðru olduðu varsayýlan sayý için negatif sayýsý hesaplar.
			for (int j=0;j<4;j++) {
				if(i==j) {continue;}
				if (yapilanTahmin[i]==dogruVarsayilan[j]) {
					negatif++;
					
				}
			}
				
		}
		if(ipucVerilsin) {  //Kullanýcýya ipucu verir.
			this.kullaniciyaPoz=pozitif;
			this.kullaniciyaNeg=negatif;
		}
		if(negatif==gelenNeg&&pozitif==gelenPoz) { //Doðru varsayýlan sayý testte ayný ipuçlarýný üretiyorsa doðru olmasý mümkündür. Deðilse o sayýnýn seçilmediði anlaþýlýr. 
			
			return true;
		}
		
		return false;
	}
	
	private void uzayAzalt() {
		
		
		int j=tahminler.size()-1;  //Yapýlan son tahminin indisi
		for(int i=0;i<this.uzay.size();i++) {
			if( ! testEt(this.tahminler.get(j),this.cpuyaPozKayit.get(j),this.cpuyaNegKayit.get(j),this.uzay.get(i),false)) {//Uzaydaki her elemaný test et. Testi geçemeyeni uzaydan at.
				this.uzay.remove(i);
				i--;
			}
		}
		
		//Aþaðýdaki kod ileride kullanýcýnýn girdiði sayýyý paylaþacaðý bir versiyonda kullanýlmak üzere tasarlanmýþtýr.
	/*	for(int j=0;j<this.tahminler.size();j++) {
			for(int i=0;i<this.uzay.size();i++) {
				if( ! testEt(this.tahminler.get(j),this.cpuyaPozKayit.get(j),this.cpuyaNegKayit.get(j),this.uzay.get(i),false)) {
					this.uzay.remove(i);
					i--;
				}	
			}
		} */
			
	
	}


	private Integer okut() {//Ýleride input arayüzden alýnýrsa bu fonksiyon düzenlenecektir.
		Integer a=-1;
		boolean hataligiris=false;
		while (a<0 || a>9)
		{
			if(hataligiris) {
				System.out.println("Hatali giriþ tekrar rakam giriniz.");
			}
			a=keyboard.nextInt();
			hataligiris=true;
		}
		//{a=Main.getInt();}
		return a;
	}



	private void yazdir(String string) {//Ýleride output arayüzden alýnýrsa bu fonksiyon düzenlenecektir.
		System.out.println(string);
		
	}
	
	
	private String debugOtoIpucuHesapla(Integer[] yapilanTahmin,Integer gelenPoz,Integer gelenNeg,Integer[] dogruVarsayilan){
		//testEt fonksiyonunun neredeyse aynýsýdýr. Varsayýlan sayý için ipuçlarýný otomatik hesaplar ve yazdýrýr. Debug iþlemini hýzlandýrýr. 
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
						
			return "default seçim olan '1234' için doðru cevap:"+Integer.toString(negatif) +" "+Integer.toString(pozitif);
		}
}
