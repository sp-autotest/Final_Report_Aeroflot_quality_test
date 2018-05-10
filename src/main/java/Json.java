import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
/**
 * Created by mycola on 03.05.2018.
 */

public class Json {

    public static void readData(Run run, String file) {

        JSONParser parser = new JSONParser();

        try {
            Reader fr = new FileReader(file);
            Object obj = parser.parse(fr);

            JSONObject jsonObject = (JSONObject) obj;
            String fullName = (String) jsonObject.get("fullName");
            fullName = fullName.substring(fullName.indexOf("."));
            String part = fullName.replaceAll("\\D+","");
            if (part.equals(run.getPart())){
                String status = (String) jsonObject.get("status");
                run.setStatus(status);
            }

            fr.close();

            /*/ loop array
            JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
