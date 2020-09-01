package org.crown.config;

import com.aspose.words.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 *
 * 获取Aspose权限
 */
@Configuration
public class AsposeAutoConfig {

    @Bean
    public License getAsposeLicense() {
        InputStream license;
        License aposeLic = null;
        try {
            license = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
            aposeLic = new License();
            aposeLic.setLicense(license);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aposeLic;
    }
}
