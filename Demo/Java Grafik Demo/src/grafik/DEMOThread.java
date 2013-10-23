package grafik;

import javax.swing.JLabel;

public class DEMOThread extends Thread{



		private JLabel label;
		
		 public void run() {
		      for(int i = 0; i < 1000; i++) {
		        try {
		          sleep(40);
		          label.setLocation(i,i);
		        }
		        catch(InterruptedException e) {
		        }
		      }
		    }

		 public DEMOThread(JLabel label){
			 this.label=label;
		 }

}
