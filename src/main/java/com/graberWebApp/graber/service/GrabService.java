package com.graberWebApp.graber.service;

import com.graberWebApp.graber.dao.GrabDao;
import com.graberWebApp.graber.model.GrabData;
import com.graberWebApp.graber.model.SurpInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrabService {
    private final GrabDao grabDao;


    @Autowired
    public GrabService(@Qualifier("postgres") GrabDao grabDao) {
        this.grabDao = grabDao;
    }


    public int addGrabData(GrabData grabData){
        return grabDao.insertGrabData(grabData);

    }

    public List<GrabData> getAllGrabData(){
        return grabDao.selectAllGrabData();
    }

    public Optional<GrabData> getGrabDataById(int id){
        return grabDao.selectGrabDataById(id);
    }

    public int deleteGrabData(int id){
        return grabDao.deleteGrabDataById(id);
    }

    public int updateGrabData(int id, GrabData grabData){
        return grabDao.updateGrabDataById(id, grabData);
    }

    public int startGrab(String url){
        return grabDao.startGrab(url);
    }

    public int addSurpInfo(SurpInfo surpInfo){
        return grabDao.insertSurpInfo(surpInfo);
    }

    public List<SurpInfo> selectAllSurpInfo(){
        return grabDao.selectAllSurpInfo();
    }
    public Optional<SurpInfo> selectSurpInfoById(int id){
        return grabDao.selectSurpInfoById(id);
    }
    public List<GrabData> selectGrabDataBySurpId(int id){
        return grabDao.selectGrabDataBySurpId(id);
    }
    public int deleteSurpInfoById(int id){
        return grabDao.deleteSurpInfoById(id);
    }
}
