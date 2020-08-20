package main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("temperature/")
public class TemperatureController {

    @Autowired
    private TemperatureService temperatureService;

    @GetMapping(value = "getForCity/{city}")
    public String getTemperature(@PathVariable("city") String city)
    {
        System.out.println("ciudad "+ city);
        return temperatureService.getTemperature(city);

    }







}
