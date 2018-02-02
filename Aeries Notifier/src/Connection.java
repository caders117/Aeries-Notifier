import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;



public class Connection {
	public static void main(String[] args){
		JFrame frame = new JFrame("Login to Aeries");
		JTextField usrField = new JTextField();
		frame.add(new JLabel("Usr"));
		frame.add(usrField);
		frame.setVisible(true);
		
		String usr = "vasanthsadhasivan@gmail.com";
		String pass = "aeries";
		
				
				String parameter = "checkCookiesEnabled=true&"
						+ "checkMobileDevice=false&"
						+ "checkStandaloneMode=false&"
						+ "checkTabletDevice=false&"
						+ "portalAccountUsername=" + usr + "&"
						+ "portalAccountPassword=" + pass + "&"
						+ "portalAccountUsernameLabel=&"
						+ "submit=";
				parameter = "checkCookiesEnabled=true&checkMobileDevice=false&checkStandaloneMode=false&checkTabletDevice=false&portalAccountUsername=vasanthsadhasivan%40gmail.com&portalAccountPassword=aeries&portalAccountUsernameLabel=&submit=";
		 URL obj;
		 String url = "https://parentportal.eduhsd.k12.ca.us/aeries.net/LoginParent.aspx?page=default.aspx";
		    InputStream is = null;
		    BufferedReader br;
		    String line;
		   

		    try {
		    	
		        obj = new URL(url);
		       
		        
		        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		        connection.setRequestMethod("POST");
		        connection.setDoOutput(true);

		        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		        wr.writeBytes(parameter);
		        wr.flush();
				wr.close();
				
				int responseCode = connection.getResponseCode();
				System.out.println("\nSending 'POST' request to URL : " + url);
				System.out.println("Post parameters : " + parameter);
				System.out.println("Response Code : " + responseCode);
				
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					//response.append(inputLine);
					System.out.println(inputLine);
				}
				in.close();

				//print result
				//System.out.println(response.toString());
				System.out.println(connection.getResponseMessage());
				//System.out.println(String uri = ((HttpServletRequest) externalContext.getRequest()).getRequestURI());
		        
		        /*is = obj.openStream();  // throws an IOException\
		        br = new BufferedReader(new InputStreamReader(is));
		        
		        
		        while ((line = br.readLine()) != null) {
		            System.out.println(line);
		        }*/
		    } catch (MalformedURLException mue) {
		         mue.printStackTrace();
		    } catch (IOException ioe) {
		         ioe.printStackTrace();
		    } finally {
		        try {
		            if (is != null) is.close();
		        } catch (IOException ioe) {
		            // nothing to see here
		        }
		    }
		}

}

