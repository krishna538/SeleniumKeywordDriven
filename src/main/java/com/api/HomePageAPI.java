package com.api;

import com.base.Constants;
import com.base.ExcelManager;
import com.utils.Helper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.Map;

public class HomePageAPI {

    public JsonNode getUserDetails(Map<String, String> data) throws Exception {
        Map<String, String> apiData = ExcelManager
                .getExcelRowsAsListOfMap(Constants.RUN_MANAGER_WORKBOOK.toString(),
                        "APIDetails", "GetUserProfileDetails").get(0);
        // TODO Need to Fix by getting the params from Excel using data - for now hard coded.
        String url = apiData.get("BaseURL") + apiData.get("EndPoint");
        Unirest.config().verifySsl(false);
        Helper.log("API URL " + url);
        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("Accept", "application/json")
                .header("X-IBM-Client-Id", "d10918b44fd1378b1edef0c42bfe51f5")
                .header("X_IBM_Client_Secret", "84bcdc446ab1c06731785a48955f0ca9")
                .header("LanguageCode", "en")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoiZGl3YW5kZW1vYiIsInVzZXJkb21haW4iOiJnYXp0IiwiZXhwIjoxNjk4ODQ4MjQzLCJpYXQiOjE2NjczMTIyNDN9.e-OgKJWHld683j3DjeQmJ6LnuSIr35Rxq4pFGKTH_D4")
                .asJson();
        Helper.log("API Response " + response.getBody().toString());
        return response.getBody();
    }

    public boolean isUserIsActive(Map<String, String> data) throws Exception {
        String activeStatus = getUserDetails(data).getObject().getJSONObject("result").getJSONObject("data").get("isActive").toString();
        if (activeStatus.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public void assertUserIsActive(Map<String, String> data) throws Exception {
        //Assert.assertTrue(isUserIsActive(data), "User is Active.");
    }
}