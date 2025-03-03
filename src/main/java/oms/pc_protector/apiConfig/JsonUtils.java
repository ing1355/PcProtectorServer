package oms.pc_protector.apiConfig;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@Slf4j
public class JsonUtils {

    public JsonUtils() {
    }
    // json 형식으로 유입된 HttpServletRequest를 string 형태로 return
    public  JSONObject readJSONStringFromRequestBody(HttpServletRequest request) throws JSONException {
        StringBuffer json = new StringBuffer();
        String line = null;

        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }

        }catch(Exception e) {
            log.info("Error reading JSON string: " + e.toString());
        }

        JSONObject jObj = new JSONObject(json.toString());
        return jObj;
    }
}