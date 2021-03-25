/*DERLEME KILAVUZU: 
 * Oyun konsolda �al��acaksa: Oyun.java dosyas� application isimli bir paketin i�ine konur, Oyun.java' n�n void main()' i �a��r�l�r.
 * 	E�er ba�ka bir classtan �a��r�lacaksa bir adet oyun instantiate edilir ve Oyun.oynat(true) fonksiyonu �a��r�l�r(true inputu: konsolda �al��ma ayarlar�n� yapar).
 * 
 * Oyun GUI ile �al��acaksa: Main.java ve Oyun.java dosyalar� application isimli bir paketin i�ine konur. Main.java'n�n void main' i �a��r�l�r.
 * 	GUI' de sorunlar vard�r. Detayl� bilgi Main.java GUI KULLANIM KILAVUZU' nda payla��lm��t�r. 
 * 
 * GUI KULLANIM KILAVUZU:
 * Main(Main.java) GUI' nin haz�rland��� classt�r. GUI kullan�lmak isteniyorsa bu class�n void main()' i �a��r�l�r.
 * GUI' de herhangi bir tu�a bas�ld���nda bir adet Oyun instantiate edilir, Oyun.oynat() fonksiyonu �a��r�l�r ve oyun ba�lar. 
 * Ne yaz�k ki GUI ve Oyun e� zamanl�(multi thread) �al��t�r�lamam��t�r.
 * GUI' den oyun ba�lat�l�nca GUI ge�ici olarak donmaktad�r. Oyuna konsoldan devam edilir.
 * Oyun bitince GUI serbest kal�r ve oyunun sonucunu g�sterir.
 * Bu sorunun Threadler, wait() ve sleep() methodlar�, synched classlarla ��z�lece�i d���n�lmektedir ama
 * proje teslim tarihine kadar bu konular� yeterince ara�t�racak f�rsat� ne yaz�k ki bulunamam��t�r.
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
	static boolean inputGeldi; //GUIDen input girilip girilmedi�ini temsil eder
	static String input; //bas�lan tu�un de�eri
	static Label label; //Ekrana yazd�r�lacak outputu g�sterir
	
	Oyun oyun; //Oyun instanc
		
	@Override
	public void start(Stage primaryStage) {
		try {			
			inputGeldi=false;
			inputIstendi=false;
			primaryStage.setTitle("Say� Tahmini Oyunu");
			
			HBox tuslarBox=new HBox();
			ArrayList<Button> tuslar=new ArrayList<Button>();
			for (int i=0;i<10;i++) {
				tuslar.add(new Button(Integer.toString(i)));
				tuslar.get(i).setOnAction(this);
				tuslarBox.getChildren().add(tuslar.get(i));
			}
			label=new Label("Herhangi bir tu�a bas�n�z. Konsoldan oynay�n�z. Oyun sonucunu g�rmek i�in bu pencereye d�n�n�z.");
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
		System.out.println("Main/void main kullan�ld�.");
		launch(args);
		
		
	}
	
	public static void updateLabel(String mesaj) {
		//Oyun nesnesinden �a�r�l�r. GUI' deki Label' a string outputlar� yazd�r�r.
		label.setText(mesaj);
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(oyun==null) {
			//Oyun instantiate edilmediyse, herhangi bir tu�a bas�ld���nda oyun GUI' den oynanacak �ekilde(input: false) ba�las�n.
			oyun=new Oyun(false);
			oyun.oynat();
		}
		if(inputIstendi) {
			input=((Button) event.getSource()).getText(); //inputu kaydet.
			inputGeldi=true; //GUI input d�nd�rd�.
			inputIstendi=false; //art�k input istenmiyor.
		}
	}
	
	
	
}
