package com.graberWebApp.graber.dao;

import com.graberWebApp.graber.model.GrabData;
import com.graberWebApp.graber.model.SurpInfo;

import java.util.List;
import java.util.Optional;

public interface GrabDao {
    int insertGrabData(GrabData grabData);
    int[][] insertBatchGrabData(List<GrabData> grabData, int batchSize);

    List<GrabData> selectAllGrabData();

    Optional<GrabData> selectGrabDataById(int id);

    int deleteGrabDataById(int id);

    int updateGrabDataById(int id, GrabData grabData);

    int startGrab(String url);

    int insertSurpInfo(SurpInfo surpInfo);
    List<SurpInfo> selectAllSurpInfo();
    Optional<SurpInfo> selectSurpInfoById(int id);
    List<GrabData> selectGrabDataBySurpId(int id);
    int deleteSurpInfoById(int id);

    int startSurp(String keyword, String location, String lang, String device);


}
