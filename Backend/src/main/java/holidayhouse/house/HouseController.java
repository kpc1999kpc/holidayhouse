package holidayhouse.house;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/houses")
public class HouseController {
    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService){
        this.houseService = houseService;
    }

    @GetMapping
    public List<House> getAllHouses() {
        return houseService.getAllHouses();
    }

    @GetMapping({"/{id}"})
    public House getHouse(@PathVariable Long id) {
        return houseService.getById(id);
    }

    @PostMapping
    public House addHouse(@RequestBody House house) {
        return houseService.addHouse(house);
    }

    @PutMapping({"/{id}"})
    public House updateHouse(@PathVariable Long id, @RequestBody House houseDetails) {
        return houseService.updateHouse(id, houseDetails);
    }

    @DeleteMapping({"/{id}"})
    public void deleteHouse(@PathVariable Long id) {
        houseService.delete(id);
    }
}