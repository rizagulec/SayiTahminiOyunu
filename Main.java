/*DERLEME KILAVUZU: 
 * Oyun konsolda çalışacaksa: Oyun.java dosyası application isimli bir paketin içine konur, Oyun.java' nın void main()' i çağırılır.
 * 	Eğer başka bir classtan çağırılacaksa bir adet oyun Oyun.(true) şeklinde constructorla instantiate edilir(true inputu: konsolda çalışma ayarlarını yapar) 
 *	ve Oyun.oynat() fonksiyonu çağırılır.
 * 
 * Oyun GUI ile çalışacaksa: Main.java ve Oyun.java dosyaları application isimli bir paketin içine konur. Main.java'nın void main()' i çağırılır.
 * 	GUI' de sorunlar vardır. Detaylı bilgi Main.java GUI KULLANIM KILAVUZU' nda paylaşılmıştır. 
 * 
 * GUI KULLANIM KILAVUZU:
 * Main(Main.java) GUI' nin hazırlandığı classtır. GUI kullanılmak isteniyorsa bu classın void main()' i çağırılır.
 * GUI' de herhangi bir tuşa basıldığında bir adet Oyun instantiate edilir, Oyun.oynat() fonksiyonu çağırılır ve oyun başlar. 
 * Ne yazık ki GUI ve Oyun eş zamanlı(multi thread) çalıştırılamamıştır.
 * GUI' den oyun başlatılınca GUI geçici olarak donmaktadır. Oyuna konsoldan devam edilir.
 * Oyun bitince GUI serbest kalır ve oyunun sonucunu gösterir.
 * Bu sorunun Threadler, wait() ve sleep() methodları, synched classlarla çözüleceği düşünülmektedir ama
 * proje teslim tarihine kadar bu konuları yeterince araştıracak fırsatı ne yazık ki bulunamamıştır.
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
	static boolean inputGeldi; //GUIDen input girilip girilmediğini temsil eder
	static String input; //basılan tuşun değeri
	static Label label; //Ekrana yazdırılacak outputu gösterir
	
	Oyun oyun; //Oyun instanceı
		
	@Override
	public void start(Stage primaryStage) {
		try {			
			inputGeldi=false;
			inputIstendi=false;
			primaryStage.setTitle("Sayı Tahmini Oyunu");
			
			HBox tuslarBox=new HBox(); //10 adet tuşun konulacağı HBox.
			ArrayList<Button> tuslar=new ArrayList<Button>(); //Buttonları kaydeden liste.
			for (int i=0;i<10;i++) { //10 adet Button instantiate et HBox a at.
				tuslar.add(new Button(Integer.toString(i)));
				tuslar.get(i).setOnAction(this);
				tuslarBox.getChildren().add(tuslar.get(i));
			}
			label=new Label("Herhangi bir tuşa basınız. Konsoldan oynayınız. Oyun sonucunu görmek için bu pencereye dönünüz.");
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
		System.out.println("Main/void main kullanıldı.");
		launch(args);
		
		
	}
		
	@Override
	public void handle(ActionEvent event) {  //GUI' de bir tuşa basıldığında çağırılır.
		if(oyun==null) {
			//Oyun instantiate edilmediyse, herhangi bir tuşa basıldığında oyun GUI' den oynanacak şekilde(input: false) başlasın.
			oyun=new Oyun(false);
			oyun.oynat();
		}
		if(inputIstendi) { //Input istenmişken tuşa basıldıysa,
			input=((Button) event.getSource()).getText(); //inputu kaydet.
			inputGeldi=true; //GUI input döndürdü.
			inputIstendi=false; //artık input istenmiyor.
		}
	}
	
	
	
}
