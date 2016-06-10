package ru.nabsky.helper;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import spark.Request;

import java.nio.charset.StandardCharsets;


public class SecurityHelper {

    public static String getSecret(String password) {
        HashFunction hashFunction = Hashing.sha256();
        HashCode hashCode = hashFunction.hashString(password, StandardCharsets.UTF_8);
        return hashCode.toString();
    }

    public static String extractTokenId(Request request){
        String authorizationHeader = request.headers("Authorization");
        if(!authorizationHeader.contains("Bearer ")){
            return null;
        };
        String token = authorizationHeader.replace("Bearer ", "");
        return token;
    }


}
