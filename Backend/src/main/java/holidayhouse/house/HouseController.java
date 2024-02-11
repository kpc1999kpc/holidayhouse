package holidayhouse.house;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/houses")
public class HouseController {
    @Autowired
    private HouseService houseService;

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

    @GetMapping("/active")
    public List<Map<String, String>> getActiveHouses() {
        return houseService.getAllHouseNames();
    }
}