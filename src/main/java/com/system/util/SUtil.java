
package com.system.util;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.util.Iterator;
import java.util.regex.Pattern;

public class SUtil {
    public static final Pattern SCRIPTS = Pattern.compile("<(?i)script[^>]*>.*?</(?i)script>", Pattern.DOTALL);

    public static String RealRequestPath = "";

    protected static Log log = LogFactory.getLog(SUtil.class);
    public static boolean fileTypeCheck(InputStream inputStream) {

        ContentInfoUtil util = new ContentInfoUtil();

        try {

            ContentInfo info;
            info = util.findMatch(inputStream);
            log.info("fileTypeCheck : " + info);

            if (info == null) {
                log.info(" Unknown content-type");
                return false;
            }

            log.info(" ContentType : " + info.getName());
            log.info(" MimeType : " + info.getMimeType());

            if(!info.getName().equals("32")) {
                log.info("exe 파일이 아닙니다.");
                return false;
            }

            String[] extensions = info.getFileExtensions();
            if (extensions != null && extensions.length > 0) {
                for (int i = 0; i < extensions.length; i++) {
                    log.info(" File Extension " + i + " : " + extensions[i]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    public static InputStream getInputStream(MultipartHttpServletRequest multipartRequest) {

        Iterator<String> itr =  multipartRequest.getFileNames();
        InputStream inputStream = null;

        while (itr.hasNext()) {

            MultipartFile mpf = multipartRequest.getFile(itr.next());

            try {
                inputStream = mpf.getInputStream();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return inputStream;
    }

    public static String getFileRead(String filePath) {
        BufferedReader reader;
        String dataFull = "";

        try {
            FileInputStream fis = new FileInputStream(new File(filePath));

            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");

            BufferedReader br = new BufferedReader(isr);

            while(true){
                String str = br.readLine();
                if(str == null)
                {
                    break;
                }
                dataFull += str + "\n";
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return dataFull;
    }

    public static void setRealPath(String path) {
        RealRequestPath = path;
    }


    public static String getRealPath() {
        return RealRequestPath;
    }

}
