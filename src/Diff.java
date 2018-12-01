import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.SwingUtilities;

public class Diff
{
	private static JFileChooser ourChooser = new JFileChooser();
	static {
        ourChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ourChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    }
	static File[] ourFiles;
    
    public Diff(){
        
    }
    
    public static void showMessage(String message){
        JOptionPane.showMessageDialog(null, message,"Diff Output",
                JOptionPane.INFORMATION_MESSAGE);
    }
    public static boolean doDiffer(File[] files){
        try {
            ProgressMonitorInputStream stream1 = 
                new ProgressMonitorInputStream(
                        null,
                        "reading "+files[0].getName(),
                        new FileInputStream(files[0]));
            ProgressMonitorInputStream stream2 = 
                new ProgressMonitorInputStream(
                        null,
                        "reading "+files[1].getName(),
                        new FileInputStream(files[1]));
            BitInputStream b1 = new BitInputStream(stream1);
            BitInputStream b2 = new BitInputStream(stream2);
            while (true) {
                int x = b1.readBits(8);
                int y = b2.readBits(8);
                if (x == -1) return y == -1;
                if (y == -1) return false;
                if (x != y) return false;
            }
            // never reached
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"trouble reading","Diff Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
      
    }
    
    public static File[] doDiffWork() {
    	
    	try {
    		ourFiles = null;
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run () {
                    int result = 0;
                    result = ourChooser.showOpenDialog(null);

                    if (result == JFileChooser.CANCEL_OPTION) {
                        ourFiles = new File[] { null };
                    } else {
                        try {
                            ourFiles = ourChooser.getSelectedFiles();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.toString());
                        }
                    }
                }
            });
            return ourFiles;
        } catch (Exception e) {
            // it is still an exception, just not one required to be handled
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args){
        ourChooser.setMultiSelectionEnabled(true);
        ourChooser.setDialogTitle("Diff: choose two files");
        ourFiles = doDiffWork();
        if (ourFiles == null || ourFiles.length != 2) {
        	JOptionPane.showMessageDialog(null,"Choose Two Files", 
                    "Diff Error",JOptionPane.ERROR_MESSAGE);
        }
        else {
        	if (doDiffer(ourFiles)){
        		showMessage("Files are the same");
        	}
        	else {
        		showMessage("Files DIFFER somewhere");
        	}
        }          
        System.exit(0);
    }
}
