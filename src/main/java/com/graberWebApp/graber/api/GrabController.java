package com.graberWebApp.graber.api;

import com.graberWebApp.graber.model.GrabData;
import com.graberWebApp.graber.model.SurpInfo;
import com.graberWebApp.graber.service.GrabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("api")
@RestController
public class GrabController {

    private final GrabService grabService;

    @Autowired
    public GrabController(GrabService grabService) {
        this.grabService = grabService;
    }

    @PostMapping
    public void addGrabData(@NonNull @RequestBody GrabData grabData){
        grabService.addGrabData(grabData);
    }

    @GetMapping
    public List<GrabData> getAllGrabData(){
        return grabService.getAllGrabData();
    }


    @GetMapping(path = "{id}")
    public GrabData getGrabDataById(@PathVariable("id") int id){
        return grabService.getGrabDataById(id).orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deleteGrabDataById(@PathVariable("id") int id){
        grabService.deleteGrabData(id);
    }

    @PutMapping(path = "{id}")
    public void updateGrabData(@PathVariable("id") int id, @NonNull @RequestBody GrabData grabDataToUpdate){
        grabService.updateGrabData(id, grabDataToUpdate);
    }

    @PostMapping("/grab")
    public int grab(@RequestParam("pageURLGrab") String url){
        return grabService.startGrab(url);
    }

    @GetMapping(path = "surp/{id}")
    public Optional<SurpInfo> surp(@PathVariable("id") int id){
        return grabService.selectSurpInfoById(id);
    }

    @GetMapping(path = "surp-grab/{id}")
    public List<GrabData> grabBySurpId(@PathVariable("id") int id){
        return grabService.selectGrabDataBySurpId(id);
    }

    @PostMapping("/surp")
    public int startSurp(@RequestParam("keyword") String keyword, @RequestParam("location") String location, @RequestParam("lang") String lang, @RequestParam("device") String device){
        return grabService.startSurp(keyword, location, lang, device);
    }

}
