package com.genai.tcoop.tourapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.net.URLEncoder.encode;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourApiService {

    private static final String BASE_URL = "http://apis.data.go.kr/B551011/EngService1/searchKeyword1";
    @Value("${tour.api.en.key}")
    private String TOUR_API_KEY;

    private final RestTemplate restTemplate;

    public List<String> test(List<String> keywords) {

        List<String> tourApiInfoList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        for(String keyword : keywords) {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                    .append("?serviceKey=")
                    .append(encode(TOUR_API_KEY, StandardCharsets.UTF_8))
                    .append("&numOfRows=5")
                    .append("&pageNo=1")
                    .append("&MobileOS=ETC")
                    .append("&_type=json")
                    .append("&MobileApp=AppTest")
                    .append("&listYN=Y")
                    .append("&keyword=")
                    .append(keyword);

            Map<String, String> map = new HashMap<>();
            map.put("serviceKey", encode(TOUR_API_KEY, StandardCharsets.UTF_8));
            map.put("numOfRows", "5");
            map.put("pageNo", "1");
            map.put("MobileOS", "ETC");
            map.put("_type", "json");
            map.put("MobileApp", "AppTest");
            map.put("listYN", "Y");
            map.put("keyword", keyword);

            String response = restTemplate.getForObject(BASE_URL, String.class, map);
            tourApiInfoList.add(response);
        }
        return tourApiInfoList;
    }

    public TourApiInfoListResponse tour(List<String> keywords) throws UnsupportedEncodingException {
        List<TourApiInfoDTO> tourApiInfoList = new ArrayList<>();

        for (String keyword : keywords) {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                    .append("?serviceKey=")
                    .append(encode(TOUR_API_KEY, StandardCharsets.UTF_8))
                    .append("&numOfRows=5")
                    .append("&pageNo=1")
                    .append("&MobileOS=ETC")
                    .append("&_type=json")
                    .append("&MobileApp=AppTest")
                    .append("&listYN=Y")
                    .append("&keyword=")
                    .append(keyword);

            String responseBody = get(urlBuilder.toString());

            log.info(urlBuilder.toString());
            log.info(responseBody);

            tourApiInfoList.add(getJsonObject(responseBody));
        }

        return new TourApiInfoListResponse(tourApiInfoList);
    }

    private String get(String apiUrl) {
        HttpURLConnection conn = connect(apiUrl);

        try {
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(conn.getInputStream());
            } else {
                return readBody(conn.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            conn.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
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

    private TourApiInfoDTO getJsonObject(String response) {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject resObject = jsonObject.getJSONObject("response");
        JSONObject body = resObject.getJSONObject("body");
        JSONObject itemsObject = body.getJSONObject("items");
        JSONArray itemsArray = itemsObject.getJSONArray("item");

        JSONObject itemObject = itemsArray.getJSONObject(0);
        String addr1 = itemObject.getString("addr1");
        String firstImage = itemObject.getString("firstimage");
        String firstImage2 = itemObject.getString("firstimage2");
        Double mapx = itemObject.getDouble("mapx");
        Double mapy = itemObject.getDouble("mapy");
        String title = itemObject.getString("title");

        return TourApiInfoDTO.builder()
                .addr1(addr1)
                .firstimage(firstImage)
                .firstimage2(firstImage2)
                .mapx(mapx)
                .mapy(mapy)
                .title(title)
                .build();
    }
}
