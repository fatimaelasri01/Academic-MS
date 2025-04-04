package pfe.mandomati.academicms.Client;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import feign.codec.Decoder;

@Configuration
public class FeignConfig {
    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(
            new MappingJackson2HttpMessageConverter(), // For JSON
            new StringHttpMessageConverter()           // For plain text
        )));
    }
}
