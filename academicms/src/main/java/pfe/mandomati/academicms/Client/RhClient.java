package pfe.mandomati.academicms.Client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "rhms", url = "https://rhms.mandomati.com")
public interface RhClient {

    
    
}
