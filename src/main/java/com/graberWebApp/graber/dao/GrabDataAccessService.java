package com.graberWebApp.graber.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graberWebApp.graber.model.GrabData;
import com.graberWebApp.graber.model.SurpInfo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@Repository("postgres")
public class GrabDataAccessService implements GrabDao{

    private final JdbcTemplate jdbcTemplate;

    public GrabDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertGrabData(GrabData grabData) {
        final String sql = "INSERT INTO GrabDataTable(serp_id, url, jsonArrData, serp_title, serp_description, organic_pos, block_pos, date_added, date_updated) VALUES(?,?,?::JSON,?,?,?,?,now(),now())";
        return jdbcTemplate.update(sql, grabData.getUrl(), grabData.getJsonArrData());
    }

    @Override
    public int[][] insertBatchGrabData(List<GrabData> grabDataList, int batchSize) {
        final String sql = "INSERT INTO GrabDataTable(serp_id, url, jsonArrData, serp_title, serp_description, organic_pos, block_pos, date_added, date_updated) VALUES(?,?,?::JSON,?,?,?,?, now(),now())";
        return jdbcTemplate.batchUpdate(sql,
                grabDataList,
                batchSize,
                (ps, argument) -> {
                    ps.setInt(1,argument.getSerp_id());
                    ps.setString(1, argument.getUrl());
                    ps.setObject(2, argument.getJsonArrData());
                    ps.setString(1, argument.getSerp_title());
                    ps.setString(1, argument.getSerp_description());
                    ps.setShort(1, argument.getOrganic_pos());
                    ps.setShort(1, argument.getBlock_pos());
                });
    }

    @Override
    public List<GrabData> selectAllGrabData() {
        final String sql = "SELECT id, url, jsonArrData, date_added, date_updated FROM GrabDataTable ORDER BY date_added DESC";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            return new GrabData(
                    resultSet.getInt("id"),
                    0,
                    resultSet.getString("url"),
                    "",
                    "",
                    "",
                    (short)0,
                    (short)0,
                    resultSet.getString("date_added"),
                    resultSet.getString("date_updated")
            );
        });
    }

    @Override
    public Optional<GrabData> selectGrabDataById(int id)
    {
        final String sql = "SELECT id, url, jsonArrData, date_added, date_updated FROM GrabDataTable WHERE id = ?";
        GrabData grabData = jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (resultSet, i) -> {
            return new GrabData(
                    resultSet.getInt("id"),
                    resultSet.getInt("surp_id"),
                    resultSet.getString("url"),
                    resultSet.getString("jsonArrData"),
                    resultSet.getString("serp_title"),
                    resultSet.getString("serp_description"),
                    resultSet.getShort("organic_pos"),
                    resultSet.getShort("block_pos"),
                    resultSet.getString("date_added"),
                    resultSet.getString("date_updated")
            );
        });

        return Optional.ofNullable(grabData);
    }

    @Override
    public int deleteGrabDataById(int id)
    {
        final String sql = "DELETE FROM GrabDataTable WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int updateGrabDataById(int id, GrabData grabData)
    {
        final String sql = "UPDATE GrabDataTable SET url = ?, jsonArrData = ?::JSON, date_updated = now() WHERE id = ?";
        return jdbcTemplate.update(sql, grabData.getUrl(), grabData.getJsonArrData(), id);
    }

    @Override
    public int insertSurpInfo(SurpInfo surpInfo){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO SerpInfoTable(keyword, location, search_engine, search_lang, device, serpArrData, organic, people_also_ask, related_searches, featured_snippet, fetch_date) VALUES(?,?,?,?,?,?::JSON,?::JSON,?::JSON,?::JSON,?::JSON,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql);
            ps.setString(1, surpInfo.getKeyword());
            ps.setString(2, surpInfo.getLocation());
            ps.setString(3, surpInfo.getSearch_engine());
            ps.setString(4, surpInfo.getSearch_lang());
            ps.setString(5, surpInfo.getDevice());
            ps.setObject(6, surpInfo.getSerpArrData());
            ps.setObject(7, surpInfo.getOrganic());
            ps.setObject(8, surpInfo.getPeople_also_ask());
            ps.setObject(9, surpInfo.getRelated_searches());
            ps.setObject(10, surpInfo.getFeatured_snippet());
            ps.setString(11, surpInfo.getFetch_date());
            return ps;
        }, keyHolder);

        return (int) keyHolder.getKey();
    }
    @Override
    public List<SurpInfo> selectAllSurpInfo(){
        final String sql = "SELECT keyword, location, search_engine, search_lang, device, fetch_date FROM SerpInfoTable ORDER BY fetch_date DESC";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            return new SurpInfo(
                    resultSet.getInt("id"),
                    resultSet.getString("keyword"),
                    resultSet.getString("location"),
                    resultSet.getString("search_engine"),
                    resultSet.getString("search_lang"),
                    resultSet.getString("device"),
                    null,
                    null,
                    null,
                    null,
                    null,
                    resultSet.getString("fetch_date")
            );
        });
    }
    @Override
    public Optional<SurpInfo> selectSurpInfoById(int id){
        final String sql = "SELECT id, keyword, location, search_engine, search_lang, device, serpArrData, organic, people_also_ask, related_searches, featured_snippet, fetch_date FROM SerpInfoTable WHERE id = ?";
        SurpInfo surpInfo = jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (resultSet, i) -> {
                    return new SurpInfo(
                            resultSet.getInt("id"),
                            resultSet.getString("keyword"),
                            resultSet.getString("location"),
                            resultSet.getString("search_engine"),
                            resultSet.getString("search_lang"),
                            resultSet.getString("device"),
                            resultSet.getString("serpArrData"),
                            resultSet.getString("organic"),
                            resultSet.getString("people_also_ask"),
                            resultSet.getString("related_searches"),
                            resultSet.getString("featured_snippet"),
                            resultSet.getString("fetch_date")
                    );
                });

        return Optional.ofNullable(surpInfo);

    }
    @Override
    public List<GrabData> selectGrabDataBySurpId(int surp_id){
        String sql = "SELECT * FROM GrabDataTable WHERE serp_id = "+surp_id;
        List<GrabData> grabDataList = jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new GrabData(
                                rs.getInt("id"),
                                rs.getInt("serp_id"),
                                rs.getString("url"),
                                rs.getString("jsonArrData"),
                                rs.getString("serp_title"),
                                rs.getString("serp_description"),
                                rs.getShort("organic_pos"),
                                rs.getShort("block_pos"),
                                rs.getString("date_added"),
                                rs.getString("date_updated")
                        )
                );

        return grabDataList;

    }
    @Override
    public int deleteSurpInfoById(int id){
        final String sql = "DELETE FROM SerpInfoTable WHERE id = ?";
        return jdbcTemplate.update(sql, id);

    }


    public String generateListString(Elements allH1Elements){
        List<String> allStrList = new ArrayList<>();
        for (Element h1Ele : allH1Elements){
            String eleStr = StringUtil.normaliseWhitespace(h1Ele.text());
            if (eleStr.equals("")){
                continue;
            }
            allStrList.add("'" + eleStr + "'");
        }
        String allStr = "";
        if (allStrList.size() > 0){
            allStr = "[" + String.join(",",removeDuplicate(allStrList)) + "]";
        }

        return allStr;
    }

    public String generateAttrListString(Elements elements, String attr){
        List<String> allAttrList = new ArrayList<>();
        for (Element element : elements){
            if (!element.attr(attr).isEmpty()){
                allAttrList.add("'" + element.attr(attr) + "'");
            }
        }
        String allStr = "";
        if (allAttrList.size() > 0){
            allStr = "[" + String.join(",",removeDuplicate(allAttrList)) + "]";
        }

        return allStr;
    }

    public String generateDomainAttrListString(Elements elements, String attr){
        List<String> allAttrList = new ArrayList<>();
        for (Element element : elements){
            if (!element.attr(attr).isEmpty()){
                String attrStr = StringUtil.normaliseWhitespace(element.attr(attr));
                if (attrStr.equals("#") || attrStr.contains("javascript")){
                    continue;
                }
                allAttrList.add("'" + element.absUrl(attr) + "'");
            }
        }
        String allStr = "";
        if (allAttrList.size() > 0){
            allStr = "[" + String.join(",",removeDuplicate(allAttrList)) + "]";
        }

        return allStr;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
    public static int countMatches(String text, String str) {
        if (isEmpty(text) || isEmpty(str)) {
            return 0;
        }

        int index = 0, count = 0;
        while (true) {
            index = text.indexOf(str, index);
            if (index != -1) {
                count ++;
                index += str.length();
            } else {
                break;
            }
        }

        return count;
    }
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public List<String> removeDuplicate(List<String> inputList){
        List<String> clearList = new ArrayList<>();
        for (String inputStr : inputList){
            if (!clearList.contains(inputStr)){
                clearList.add(inputStr);
            }

        }
        return clearList;
    }

    public String grabWebPage(String url){
        String grabJson = "";
        List<String> unwatedTagList = new ArrayList<String>();
        unwatedTagList.add("header");
        unwatedTagList.add("footer");
        List<String> unwantedClassList = new ArrayList<String>();
        unwatedTagList.add(".sidebar");

        Connection connection = Jsoup.connect(url);
        connection.userAgent("Mozilla");
        connection.timeout(0);
        connection.followRedirects(true);
        Map<String, String> data = new LinkedHashMap<>();

        try{
            Document doc = connection.get();
            data.put("url",url);
            String domainUrl = getDomainName(doc.baseUri());
            data.put("url_domain",domainUrl);
            data.put("status_code",Integer.toString(connection.response().statusCode()));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = df.format(new Date());
            data.put("p_date",dateNow);
            data.put("u_date",dateNow);
            data.put("title", doc.title());
            Element headElement = doc.head();
            Element desElement = headElement.select("meta[name='description']").first();
            if (desElement != null){
                data.put("meta_description","['description','"+desElement.attr("content")+"']");
            }else{
                data.put("meta_description","");
            }
            Element keywordElement = headElement.select("meta[name='keywords']").first();
            if (keywordElement != null){
                data.put("meta_keywords","['keywords','"+keywordElement.attr("content")+"']");
            }else{
                data.put("meta_keywords","");
            }
            Element ogUrlElement = headElement.select("meta[property='og:url']").first();
            if (ogUrlElement != null){
                data.put("meta_og_url","['og:url','"+ogUrlElement.attr("content")+"']");
            }else{
                data.put("meta_og_url","");
            }
            Element ogTitleElement = headElement.select("meta[property='og:title']").first();
            if (ogTitleElement != null){
                data.put("meta_og_title","['og:title','"+ogTitleElement.attr("content")+"']");
            }else{
                data.put("meta_og_title","");
            }
            Element ogDesElement = headElement.select("meta[property='og:description']").first();
            if (ogDesElement != null){
                data.put("meta_og_description","['og:description','"+ogDesElement.attr("content")+"']");
            }else{
                data.put("meta_og_description","");
            }

            Element ogImgElement = headElement.select("meta[property='og:image']").first();
            if (ogImgElement != null){
                data.put("meta_og_image","['og:image','"+ogImgElement.attr("content")+"']");
            }else{
                data.put("meta_og_image","");
            }

            Element canonicalElement = headElement.select("link[rel='canonical']").first();
            if (canonicalElement != null){
                data.put("meta_canonical_tag","['canonical','"+canonicalElement.attr("href")+"']");
            }else{
                data.put("meta_canonical_tag","");
            }
            Element charElement = headElement.selectFirst("meta[charset*='']");
            if (charElement != null){
                data.put("meta_charset","['"+charElement.attr("charset")+"']");
            }else{
                data.put("meta_charset","");
            }

            Element langElement = doc.selectFirst("html[lang*='']");
            if (langElement != null){
                data.put("meta_href_lang","['"+langElement.attr("lang")+"']");
            }else{
                data.put("meta_href_lang","");

            }

            for (String unwantedTag : unwatedTagList) {
                doc.select(unwantedTag).remove();
            }
            for (String unwantedClass : unwantedClassList){
                doc.select(unwantedClass).remove();
            }

            Element bodyElement = doc.body();
            data.put("all_h1_text",generateListString(bodyElement.select("h1")));
            data.put("all_h2_text",generateListString(bodyElement.select("h2")));
            data.put("all_h3_text",generateListString(bodyElement.select("h3")));
            data.put("all_h4_text",generateListString(bodyElement.select("h4")));
            data.put("all_h5_text",generateListString(bodyElement.select("h5")));

            Elements imgElements = bodyElement.select("img");
            data.put("images_src", generateDomainAttrListString(imgElements,"src"));
            data.put("images_alt", generateAttrListString(imgElements,"alt"));

            Elements aElements = bodyElement.select("a");
            data.put("all_a_href", generateDomainAttrListString(aElements, "href"));
            data.put("all_a_text", generateListString(aElements));

            Elements tableElements = bodyElement.select("table");
            String allTableStr = "";
            for (Element element : tableElements){
                String liStr = generateListString(element.select("caption, th"));
                if (!liStr.equals("")){
                    allTableStr = allTableStr + liStr + ", ";
                }
            }
            if (tableElements.size() > 0 && !allTableStr.equals("")){
                allTableStr = "[" + allTableStr.substring(0,allTableStr.length() - 2) + "]";
            }
            data.put("all_tables", allTableStr);

            //table
            String ulAllStr = "";
            Elements ulElements = bodyElement.select("ul");
            for (Element ulElement :ulElements ){
                String liStr = generateListString(ulElement.select("li"));
                if (!liStr.equals("")){
                    ulAllStr = ulAllStr + liStr + ", ";
                }
            }
            if (ulElements.size() > 0 && !ulAllStr.equals("")){
                ulAllStr = "[" + ulAllStr.substring(0,ulAllStr.length() - 2) + "]";
            }
            data.put("list_ul_li_text", ulAllStr);
            String olAllStr = "";
            Elements olElements = bodyElement.select("ol");
            for (Element olElement :olElements ){
                String liStr = generateListString(olElement.select("li"));
                if (!liStr.equals("")){
                    olAllStr = olAllStr + liStr+ ", ";
                }
            }
            if (olElements.size() > 0 && !olAllStr.equals("")){
                olAllStr = "[" + olAllStr.substring(0,olAllStr.length() - 2) + "]";
            }

            data.put("list_ol_li_text", olAllStr);

            String words[] = doc.text().split("\\s");
            data.put("total_words",Integer.toString(words.length));
            data.put("total_images",Integer.toString(imgElements.size()));
            data.put("total_links",Integer.toString(aElements.size()));

            int inbound = 0;
            int outbound = 0;
            for (Element aElement : aElements){
                String hrefStr = "";
                if (!aElement.attr("href").isEmpty()){
                    hrefStr = aElement.attr("href");
                    if (!hrefStr.contains(domainUrl)){
                        outbound ++;
                    }else{
                        inbound ++;
                    }
                }
            }
            data.put("total_links_inbound", Integer.toString(inbound));
            data.put("total_links_outbound", Integer.toString(outbound));

            String[] wordsToIgnore = {"the", "and", "a", "an", "am", "is", "I", "n", "can"};

            String pageText = doc.text();
            List<String> clearWordList = new ArrayList<>();
            for (String word : words){
                clearWordList.add(StringUtil.normaliseWhitespace(word));

            }
            Set<String> uniqueWords = new HashSet<String>();
            for (String word : clearWordList) {
                if (!Arrays.asList(wordsToIgnore).contains(word)){
                    uniqueWords.add(word);
                }
            }
            Map<String, Integer> counterMap = new HashMap<>();

            for (String word : uniqueWords){
                int counterInt = countMatches(pageText, word);
                if (counterInt > 1){
                    counterMap.put(word, counterInt);
                }
            }

            Map<String, Integer> sorted = counterMap
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(
                            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                    LinkedHashMap::new));

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String json = objectMapper.writeValueAsString(sorted);
                data.put("All_Top_Keywords_Frequency", json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                data.put("All_Top_Keywords_Frequency", "");
            }

            try {
                grabJson = objectMapper.writeValueAsString(data);
                return grabJson;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return grabJson;
    }

    @Override
    public int startGrab(String url) {
        System.out.print(url);
//        GrabData grabData = new GrabData(0 ,url, grabWebPage(url), "", "");
//        insertGrabData(grabData);
        startAdvancedGrab();
        return 0;
    }

    public int startAdvancedGrab(){
        String url = "https://api.dataforseo.com/v3/serp/google/organic/live/advanced";
        String name = "leebin424224@gmail.com";
        String password = "73130ec81bd33195";
        String authString = name + ":" + password;
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
        System.out.println("Base64 encoded auth string: " + authStringEnc);
        Client restClient = Client.create();
        WebResource webResource = restClient.resource(url);

        String input = "[\n    {\n        \"language_code\": \"en\",\n        \"location_name\": \"United States\",\n        \"keyword\": \"PostgreSQL vs. SQL Server\"\n    }\n]";
        ClientResponse resp = webResource.accept("application/json")
                .header("Authorization", "Basic " + authStringEnc)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, input);
        if(resp.getStatus() != 200){
            System.err.println("Unable to connect to the server");
        }
        String output = resp.getEntity(String.class);
        System.out.println("response: "+output);

        return 0;
    }




}
