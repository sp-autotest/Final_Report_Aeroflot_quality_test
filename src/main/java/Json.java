import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Collection;
import java.util.Set;

/**
 * Created by mycola on 03.05.2018.
 */

public class Json {

    public static void readData(Run run, String file) {
        String DIR_NAME = "result\\screenshot\\";
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
                String description = (String) jsonObject.get("description");
                description = description.substring(description.indexOf("билеты:"));
                run.setDescription(description);

                JSONObject test = (JSONObject) jsonObject.get("testStage");
                JSONArray steps = (JSONArray) test.get("steps");
                for (int i=steps.size()-1; i>=0; i--) {
                    JSONObject jsonObjectRow = (JSONObject) steps.get(i);
                    String name = (String) jsonObjectRow.get("name");
                    if (name.equals("Запись результатов")) {
                        JSONArray steps1 = (JSONArray) jsonObjectRow.get("steps");
                        for (int j=0; j<steps1.size(); j++) {
                            JSONObject jsonObjectRow1 = (JSONObject) steps1.get(j);
                            String name1 = (String) jsonObjectRow1.get("name");
                            if (name1.contains("PNR:")) run.setPnr(name1.substring(name1.indexOf(" ")));
                            if (name1.contains("Номер карты:")) run.setCard(name1.substring(name1.indexOf(":")+1));
                            if (name1.equals("Документы:")) {
                                JSONArray steps2 = (JSONArray) jsonObjectRow1.get("steps");
                                JSONObject jsonObjectRow2 = (JSONObject) steps2.get(0);
                                String name2 = (String) jsonObjectRow2.get("name");
                                if (name2.equals("logDoc[]")) name2 = "не обнаружено";
                                run.setDocumens(name2);
                            }
                            System.out.println(name1);
                        }
                        break;
                    }
                }

                //переименование скриншотов
                if (run.getPnr()!=null) {
                    if (!run.getPnr().contains("null")) {
                        FileReader fr1 = new FileReader(file);
                        BufferedReader reader = new BufferedReader(fr1);
                        String line;
                        int n = 0;
                        while ((line = reader.readLine()) != null) {
                            if (line.contains(".png\",")) {
                                n++;
                                String OLD_FILENAME = line.substring(line.indexOf(":") + 3, line.indexOf(".png") + 4);
                                String NEW_FILENAME = run.getPnr() + "_" + n + ".png";
                                File oldFile = new File(DIR_NAME, OLD_FILENAME);
                                File newFile = new File(DIR_NAME, NEW_FILENAME);
                                if (oldFile.exists() && !newFile.exists()) {
                                    if (oldFile.renameTo(newFile)) {
                                        System.out.println("Файл переименован");
                                    } else {
                                        System.out.println("Файл не переименован");
                                    }
                                }
                            }
                        }
                        fr1.close();
                    }
                }
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
