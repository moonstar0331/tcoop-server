package com.genai.tcoop.service;

import com.genai.tcoop.model.dto.TourInfoDTO;
import com.genai.tcoop.model.dto.response.TourInfoListResponse;
import com.genai.tcoop.search.util.GeoPoint;
import com.genai.tcoop.search.util.GeoTrans;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.net.URLEncoder.encode;

@Slf4j
@Service
public class TourService {

    @Value("${tour.api.key}")
    private String TOUR_API_KEY;

    private static final String BASE_URL = "http://apis.data.go.kr/B551011/KorService1/locationBasedList1";

    public TourInfoListResponse suggest(Double mapx, Double mapy) throws UnsupportedEncodingException {

        GeoPoint geoPoint = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, new GeoPoint(mapx, mapy));
        Double lat = geoPoint.getY() * 1E6;
        Double lng = geoPoint.getX() * 1E6;

        StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                .append("?serviceKey=" + encode(TOUR_API_KEY, "UTF-8"))
                .append("&numOfRows=5")
                .append("&pageNo=1")
                .append("&MobileOS=ETC")
                .append("&MobileApp=TCOOP")
                .append("&_type=json")
                .append("&mapX=" + lat.intValue())
                .append("&mapY=" + lng.intValue())
                .append("&radius=20000");

        String responseBody = get(urlBuilder.toString());

        log.info(urlBuilder.toString());
        log.info(responseBody);

        return getJsonObject(responseBody);
    }

    private String get(String apiUrl) {
        HttpURLConnection conn = connect(apiUrl);

        try {
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
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

    private TourInfoListResponse getJsonObject(String response) {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject resObject = jsonObject.getJSONObject("response");
        JSONObject body = resObject.getJSONObject("body");
        JSONObject itemsObject = body.getJSONObject("items");
        JSONArray itemsArray = itemsObject.getJSONArray("item");

        List<TourInfoDTO> tourInfoList = new ArrayList<>();

        for(int i=0; i < itemsArray.length(); i++) {
            JSONObject itemObject = itemsArray.getJSONObject(i);
            String addr1 = itemObject.getString("addr1");
            String addr2 = itemObject.getString("addr2");
            Double dist = itemObject.getDouble("dist");
            String imageUrl = itemObject.getString("firstimage");
            Double mapx = itemObject.getDouble("mapx");
            Double mapy = itemObject.getDouble("mapy");
            String title = itemObject.getString("title");

            TourInfoDTO tourInfoDTO = TourInfoDTO.builder()
                    .addr1(addr1)
                    .addr2(addr2)
                    .dist(dist)
                    .imageUrl(imageUrl)
                    .mapx(mapx)
                    .mapy(mapy)
                    .title(title)
                    .build();

            tourInfoList.add(tourInfoDTO);
        }

        return new TourInfoListResponse(tourInfoList);
    }
}
