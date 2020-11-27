package com.graberWebApp.graber.api;

import com.graberWebApp.graber.model.GrabData;
import com.graberWebApp.graber.service.GrabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
