package com.genai.tcoop.search;

import com.genai.tcoop.search.util.GeoPoint;
import com.genai.tcoop.search.util.GeoTrans;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.URLEncoder.encode;

@Slf4j
@Service
public class SearchService {

    @Value("${naver.client.id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    private static final String BASE_URL = "https://openapi.naver.com/v1/search/local.json";

    public NaverLocationInfoListResponse searchLocation(String keyword) throws IOException, ParseException {

        StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                .append("?" + encode("query","UTF-8") + "=" + encode(keyword, "UTF-8"))
                .append("&" + encode("display","UTF-8") + "=" + "5")
                .append("&" + encode("start","UTF-8") + "=" + "1")
                .append("&" + encode("sort","UTF-8") + "=" + encode("random", "UTF-8"));

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", NAVER_CLIENT_ID);
        requestHeaders.put("X-Naver-Client-Secret", NAVER_CLIENT_SECRET);

        String responseBody = get(urlBuilder.toString(), requestHeaders);

        return getJsonObject(responseBody);
    }


    private String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private NaverLocationInfoListResponse getJsonObject(String response) throws ParseException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray itemsArray = jsonObject.getJSONArray("items");

        List<NaverLocationInfoDTO> naverLocationInfoDtoList = new ArrayList<>();

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemObject = itemsArray.getJSONObject(i);
            String title = itemObject.getString("title").replaceAll("<[^>]*>", "");
            String address = itemObject.getString("address");
            Double mapx = Double.valueOf(itemObject.getString("mapx"));
            Double mapy = Double.valueOf(itemObject.getString("mapy"));

            // KATEC 좌표계 기준 데이터를 경도, 위도로 변환
//            GeoPoint geoPoint = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, new GeoPoint(mapx, mapy));

            NaverLocationInfoDTO naverLocationInfoDto
                    = NaverLocationInfoDTO.builder()
                    .address(address)
                    .title(title)
                    .latitude(mapx)
                    .longitude(mapy)
//                    .latitude(geoPoint.getY())
//                    .longitude(geoPoint.getX())
                    .build();
            naverLocationInfoDtoList.add(naverLocationInfoDto);
        }

        return new NaverLocationInfoListResponse(naverLocationInfoDtoList);
    }
}
