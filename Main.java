/*DERLEME KILAVUZU: 
 * Oyun konsolda çalýþacaksa: Oyun.java dosyasý application isimli bir paketin içine konur, Oyun.java' nýn void main()' i çaðýrýlýr.
 * 	Eðer baþka bir classtan çaðýrýlacaksa bir adet oyun instantiate edilir ve Oyun.oynat(true) fonksiyonu çaðýrýlýr(true inputu: konsolda çalýþma ayarlarýný yapar).
 * 
 * Oyun GUI ile çalýþacaksa: Main.java ve Oyun.java dosyalarý application isimli bir paketin içine konur. Main.java'nýn void main' i çaðýrýlýr.
 * 	GUI' de sorunlar vardýr. Detaylý bilgi Main.java GUI KULLANIM KILAVUZU' nda paylaþýlmýþtýr. 
 * 
 * GUI KULLANIM KILAVUZU:
 * Main(Main.java) GUI' nin hazýrlandýðý classtýr. GUI kullanýlmak isteniyorsa bu classýn void main()' i çaðýrýlýr.
 * GUI' de herhangi bir tuþa basýldýðýnda bir adet Oyun instantiate edilir, Oyun.oynat() fonksiyonu çaðýrýlýr ve oyun baþlar. 
 * Ne yazýk ki GUI ve Oyun eþ zamanlý(multi thread) çalýþtýrýlamamýþtýr.
 * GUI' den oyun baþlatýlýnca GUI geçici olarak donmaktadýr. Oyuna konsoldan devam edilir.
 * Oyun bitince GUI serbest kalýr ve oyunun sonucunu gösterir.
 * Bu sorunun Threadler, wait() ve sleep() methodlarý, synched classlarla çözüleceði düþünülmektedir ama
 * proje teslim tarihine kadar bu konularý yeterince araþtýracak fýrsatý ne yazýk ki bulunamamýþtýr.
 */

package application;
	
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class Main extends Application implements EventHandler<ActionEvent>  {
	public static boolean inputIstendi;
	static boolean inputGeldi; //GUIDen input girilip girilmediðini temsil eder
	static String input; //basýlan tuþun deðeri
	static Label label; //Ekrana yazdýrýlacak outputu gösterir
	
	Oyun oyun; //Oyun instanc
		
	@Override
	public void start(Stage primaryStage) {
		try {			
			inputGeldi=false;
			inputIstendi=false;
			primaryStage.setTitle("Sayý Tahmini Oyunu");
			
			HBox tuslarBox=new HBox();
			ArrayList<Button> tuslar=new ArrayList<Button>();
			for (int i=0;i<10;i++) {
				tuslar.add(new Button(Integer.toString(i)));
				tuslar.get(i).setOnAction(this);
				tuslarBox.getChildren().add(tuslar.get(i));
			}
			label=new Label("Herhangi bir tuþa basýnýz. Konsoldan oynayýnýz. Oyun sonucunu görmek için bu pencereye dönünüz.");
			StackPane layout=new StackPane();
			layout.getChildren().add(tuslarBox);
			layout.getChildren().add(label);
			
			Scene scene=new Scene(layout,1000,200);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Main/void main kullanýldý.");
		launch(args);
		
		
	}
	
	public static void updateLabel(String mesaj) {
		//Oyun nesnesinden çaðrýlýr. GUI' deki Label' a string outputlarý yazdýrýr.
		label.setText(mesaj);
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(oyun==null) {
			//Oyun instantiate edilmediyse, herhangi bir tuþa basýldýðýnda oyun GUI' den oynanacak þekilde(input: false) baþlasýn.
			oyun=new Oyun(false);
			oyun.oynat();
		}
		if(inputIstendi) {
			input=((Button) event.getSource()).getText(); //inputu kaydet.
			inputGeldi=true; //GUI input döndürdü.
			inputIstendi=false; //artýk input istenmiyor.
		}
	}
	
	
	
}
