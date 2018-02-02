import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
/**
 * This program shows a popup notification on your desktop when your grades are changed.
 * @author Cade @version 1.1.1
 */
public class newApachePost {
	
	static SpringForm inputForm;
	//static initCourseNames

	@SuppressWarnings("deprecation")

	public static void main(String[] args){
		JFrame frame = new JFrame("Login to Aeries");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultSize(24);
						
		String[] labels = {"Email: ", "Password: "};
		inputForm = new SpringForm(labels, 5, 10, true);
		
		frame.getContentPane().add(inputForm);
				
		frame.pack();
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// Set inputForm submit button to be activated when enter key pressed
		frame.getRootPane().setDefaultButton(inputForm.getSubmitButton());
		
		// Wait until inputForm is submitted
		while(!inputForm.isSubmitted()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		frame.dispose();
		
		JOptionPane.showMessageDialog(null, "You will now recieve notifications when your grades are updated on Aeries.", "Ta-da", JOptionPane.PLAIN_MESSAGE);
		
		HashMap<String, String> values = inputForm.getFormVals();
		System.out.println("Email: " + values.get("Email: "));
		System.out.println("Password: " + values.get("Password: "));
	
	
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setRedirectStrategy(new DefaultRedirectStrategy() {                
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
                boolean isRedirect=false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
                        return true;
                    }
                }
                return isRedirect;
            }
        });
		HttpPost httpPost = new HttpPost("https://parentportal.eduhsd.k12.ca.us/aeries.net/LoginParent.aspx");

		// Request parameters and other properties.
		String usr = "sel.richard@gmail.com";
		String password = "Putien88ae";
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(8);
        nameValuePair.add(new BasicNameValuePair("checkCookiesEnabled","true"));
        nameValuePair.add(new BasicNameValuePair("checkMobileDevice","false"));
        nameValuePair.add(new BasicNameValuePair("checkStandaloneMode","false"));
        nameValuePair.add(new BasicNameValuePair("checkTabletDevice","false"));
        nameValuePair.add(new BasicNameValuePair("portalAccountUsername", usr));
        nameValuePair.add(new BasicNameValuePair("portalAccountPassword", password));
        nameValuePair.add(new BasicNameValuePair("portalAccountUsernameLabel", ""));
        nameValuePair.add(new BasicNameValuePair("submit", ""));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            
            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());
            String inputLine ;
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            try {
                while ((inputLine = br.readLine()) != null) {
                    //System.out.println(inputLine);
                }
                
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        HttpGet httpGet = new HttpGet("https://parentportal.eduhsd.k12.ca.us/Aeries.Net/Widgets/ClassSummary/GetClassSummary?IsProfile=True&sort=Period-asc&group=&filter=&_=1494727026707");
		StringBuilder courseData = new StringBuilder();
		HashMap<String, Double> coursePercentages = new HashMap<>();

        try{
        HttpResponse response = httpClient.execute(httpGet);
        
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

		BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		
		while ((line = br.readLine()) != null) {
			courseData.append(line);
			System.out.println(line);
		}
		br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
        
        ArrayList<Double> percentages = new ArrayList<>();
        ArrayList<String> courseNames = new ArrayList<>();
        Scanner scanPercent;
        
        //vscan CourseData string for course names
        for(int i = 0; i < courseData.length() - 10; i++){
        	if(courseData.substring(i, i+10).equals("CourseName")){
        		for(int j = i+13; j < courseData.length(); j++){
        			//Check if course name is not null, if it is null, it won't start with a " character
        			if(courseData.charAt(j) == '"'){
        				//Add course name
        				if(courseNames.contains(courseData.substring(i+13, j)))
            				courseNames.add(courseData.substring(i+13, j) + "2");
        				else
        					courseNames.add(courseData.substring(i+13, j));
        				
        				//Once course name is found scan from that place in the courseData String to the next percent
        				for(int k = j+1; k < courseData.length(); k++){
        					if(courseData.substring(k, k+7).equals("Percent")){

        						// Check if course name is not null
        						if(courseData.charAt(k+9) != 'n'){
        							
        							// Deals with 3 character percentages
        							if(courseData.charAt(k+13) == '"'){
            							percentages.add(Double.parseDouble(courseData.substring(k+10, k+13)));

        							} else {
        								percentages.add(Double.parseDouble(courseData.substring(k+10, k+14)));
        							}
        						} else {
        							percentages.add(null);
        						}
        		        		break;
        		        	}
        				}
        				break;
        			}
        		}
        		
        	}
        	
        }
        for(int i = 0; i < courseNames.size(); i++){
        //System.out.println(courseNames.get(i));
        //System.out.println(percentages.get(i));
        coursePercentages.put(courseNames.get(i), percentages.get(i));
        
        //System.out.println(coursePercentages.kcoursePercentages.get(courseNames.get(i)));

        }
        
        //converts to array in jumbled order...Debating whether to change that
        String courseNamesSet[] = coursePercentages.keySet().toArray(new String[coursePercentages.keySet().size()]);
        for(int i = 0; i < courseNamesSet.length; i++){
        	String spacing;
        	if(courseNamesSet[i].length() >=16){
        		spacing = "\t";
        	} else {
        		spacing = "\t\t";
        	}
    
        	System.out.println(courseNamesSet[i] + spacing + coursePercentages.get(courseNamesSet[i]));
        }
	}
	
	public static HashMap<String, Double> initCourses(){
		
		return null;
		
	}
	
	/**
	 * Deals with hiDPI swing scaling problems
	 * Taken from http://stackoverflow.com/questions/26877517/java-swing-on-high-dpi-screen
	 */
	public static void setDefaultSize(int size) {
	
		Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
		Object[] keys = keySet.toArray(new Object[keySet.size()]);
		
		for (Object key : keys) {
			
			if (key != null && key.toString().toLowerCase().contains("font")) {
				
				//System.out.println(key);
				Font font = UIManager.getDefaults().getFont(key);
				if (font != null) {
				font = font.deriveFont((float)size);
				UIManager.put(key, font);
				}
			}
		}
	}
	
}
