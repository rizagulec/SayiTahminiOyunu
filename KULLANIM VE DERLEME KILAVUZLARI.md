# SayiTahminiOyunu

  DERLEME KILAVUZU: 
  Oyun konsolda çalışacaksa: Oyun.java dosyası application isimli bir paketin içine konur, Oyun.java' nın void main()' i çağırılır.
  	Eğer başka bir classtan çağırılacaksa bir adet oyun instantiate edilir ve Oyun.oynat(true) fonksiyonu çağırılır(true inputu: konsolda çalışma ayarlarını yapar).
  
  Oyun GUI ile çalışacaksa: Main.java ve Oyun.java dosyaları application isimli bir paketin içine konur. Main.java'nın void main' i çağırılır.
  	GUI' de sorunlar vardır. Detaylı bilgi Main.java GUI KULLANIM KILAVUZU' nda paylaşılmıştır. 
 
  OYUN KULLANIM KILAVUZU:
  Merhaba, bu oyunda CPU ve kullanıcı akıllarından 4 basamaklı ve rakamları farklı bir sayı tutar(örn: 1234 gibi,1111 değil)
  ve sırayla tahmin ederek karşı tarafın tuttuğu sayıyı bulmaya çalışırlar. Doğru tahmini ilk yapan kazanır. 
  Doğru tahmin edilen her basamak için karşı tarafa o sayıda pozitif ipucu verilir.
  Doğru tahmin edilen ama yanlış basamaktaki her rakam için karşı tarafa o sayıda negatif ipucu verilir.
  Örnek: Doğru Sayı:1234 Tahmin:1239 ise pozitif:3 negatif:0 .
  Örnek: Doğru Sayı:1234 Tahmin:4321 ise pozitif:0 negatif:4 .
  CPU gelen ipuçlarını bütün sayı kombinasyonlarında test ederek mümkün olmayan kombinasyonları eler ve en son kalan kombinasyonu doğru olarak tahmin eder.
  Kullanıcının her şart altında integer değer gireceği var sayılmıştır.
  Kullanıcı rakam(0-9 arası tam sayılar) olmayan sayı ya da karakter girerse, rakamları farklı olmayan sayı oluşturmaya çalışırsa, imkansız ipuçları oluşturmaya çalışırsa 
  Uyarı alır ve tekrar giriş yapması istenir.
  Kullanıcı ipuçlarını yanlış hesaplarsa tahmin edilecek olası kombinasyon kalmaz ve hakem hata bildirisi yapar.
  Kullanıcının tuttuğu sayıyı oyuna girmesi yasaktır. Bu sebeple kullanıcı hata yaparak oyunu kazanırsa ne yazık ki hata tespiti yapılamaz.
  İleride bu oyunun olası yasaksız versiyonuna kolaylıkla geçiş yapabilmek için eski ipuçları ve tahminler bu oyunda depolanır.
  İşbu yasak kalkarsa hatanın yapıldığı tur numarası tespit edilebilir ve oyun oradan devam edebilir.
 
  GUI KULLANIM KILAVUZU:
  Main(Main.java) GUI' nin hazırlandığı classtır. GUI kullanılmak isteniyorsa bu classın void main()' i çağırılır.
  GUI' de herhangi bir tuşa basıldığında bir adet Oyun instantiate edilir, Oyun.oynat() fonksiyonu çağırılır ve oyun başlar. 
  Ne yazık ki GUI ve Oyun eş zamanlı(multi thread) çalıştırılamamıştır.
  GUI' den oyun başlatılınca GUI geçici olarak donmaktadır. Oyuna konsoldan devam edilir.
  Oyun bitince GUI serbest kalır ve oyunun sonucunu gösterir.
  Bu sorunun Threadler, wait() ve sleep() methodları, synched classlarla çözüleceği düşünülmektedir ama
  proje teslim tarihine kadar bu konuları yeterince araştıracak fırsatı ne yazık ki bulunamamıştır.
 
