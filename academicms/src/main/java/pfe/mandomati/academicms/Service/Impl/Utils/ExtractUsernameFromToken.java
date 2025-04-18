package pfe.mandomati.academicms.Service.Impl.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class ExtractUsernameFromToken {
    public static String extractUsernameFromToken(String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");

            String payload = token.split("\\.")[1];
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);

            // Utiliser Jackson pour convertir le payload JSON en Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(decodedPayload, new TypeReference<Map<String, Object>>() {
            });

            return (String) jsonMap.get("preferred_username");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token format or unable to extract username", e);
        }
    }
}