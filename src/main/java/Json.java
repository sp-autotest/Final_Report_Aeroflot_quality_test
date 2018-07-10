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
                run.setUid((String) jsonObject.get("uid"));
                run.setStatus((String) jsonObject.get("status"));
                String description = (String) jsonObject.get("description");
                System.out.println("description = " + description);
                description = description.substring(description.indexOf("билеты:"));
                run.setDescription(description);
                String message = (String) jsonObject.get("statusMessage");
                if (null != message) {
                    run.setMessage("Ошибка" + message.substring(message.indexOf(":")));
                } else run.setMessage("");
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
                                String docs = "";
                                for (int k=0; k<steps2.size(); k++) {
                                    JSONObject jsonObjectRow2 = (JSONObject) steps2.get(k);
                                    String name2 = (String) jsonObjectRow2.get("name");
                                    if (name2.equals("logDoc[]")) name2 = "не обнаружено";
                                    int dots = name2.indexOf(":");
                                    if (dots>0) {
                                        String type = name2.substring(0, dots+1);
                                        String doc = getNumberFromStringEnd(name2.substring(dots+1));
                                        if (doc.length()>0) {
                                            if (docs.contains(type)) {
                                                name2 = ", " + doc;
                                            } else {
                                                name2 = type + doc;
                                                if (docs.length() > 0) name2 = "\r\n" + name2;
                                            }
                                        } else name2 = "";
                                    } else {
                                        if ((steps2.size()-k-1) > 0) name2 = name2 + "\r\n";
                                    }
                                    docs = docs + name2; //НАРАЩИВАНИЕ ТЕКСТА
                                }
                                run.setDocumens(docs);
                            }
                            System.out.println(name1);
                        }
                        if (run.getStatus().equals("passed")) break;
                    }else {
                        if (name.equals("Неблокирующие ошибки")) {
                            run.setLastStep(name);
                            JSONArray steps1 = (JSONArray) jsonObjectRow.get("steps");
                            String text1 = "";
                            String text2 = "";
                            for (Object step : steps1) {
                                JSONObject jsonObjectRow1 = (JSONObject) step;
                                String err = jsonObjectRow1.get("name") + "\r\n";
                                text1 = text1 + err;
                                if (err.indexOf("[")>0 & err.indexOf("]")>0) {
                                    text2 = text2 + "\r\n" + err.substring(err.indexOf("[")+1, err.indexOf("]"));
                                }
                            }
                            run.setLastSubStep(text1);
                            run.setPeriodicity(text2);
                        } else { //запись инфы об упавшем тесте
                            run.setLastStep(name);
                            JSONArray steps1 = (JSONArray) jsonObjectRow.get("steps");
                            if (steps1.size() > 0) {
                                JSONObject jsonObjectRow1 = (JSONObject) steps1.get(steps1.size()-1);
                                run.setLastSubStep("Шаг: " + jsonObjectRow1.get("name"));
                                if (name.indexOf(",") >= 0) {
                                    name = name.substring(0, name.indexOf(",")).replaceAll("\\D+", "");
                                }
                                if (name.length()<2) name = "0" + name;
                                if (steps1.size()<10) name = name + "0";
                                run.setPeriodicity(name + steps1.size());
                            }
                            break;
                        }
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

    private static String getNumberFromStringEnd(String text) {
        String result = "";
        for(int i=text.length()-1; i>=0; i=i-1){
            char c = text.charAt(i);
            if (Character.isDigit(c)) {
                result = c + result;
            }else break;
        }
        return result;
    }

}
