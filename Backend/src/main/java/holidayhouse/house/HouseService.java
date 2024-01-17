package holidayhouse.house;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class HouseService {
    private final HouseRepository houseRepository;

    @Autowired
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }

    public House getById(Long id) {
        return houseRepository.findById(id).orElseThrow(() -> new NoSuchElementException("House not found with id " + id));
    }

    public House addHouse(House house) {
        return houseRepository.save(house);
    }

    public House updateHouse(Long id, House houseDetails) {
        House house = getById(id);
        house.setName(houseDetails.getName());
        house.setStatus(houseDetails.getStatus());
        house.setComment(houseDetails.getComment());
        house.setNumberOfBeds(houseDetails.getNumberOfBeds());
        return houseRepository.save(house);
    }

    public void delete(Long id) {
        houseRepository.deleteById(id);
    }

    public List<Map<String, String>> getAllHouseNames() {
        return houseRepository.findAll().stream()
                .map(house -> {
                    Map<String, String> houseNameMap = new HashMap<>();
                    houseNameMap.put("name", house.getName().trim()); // Usuń białe znaki na początku i końcu
                    return houseNameMap;
                })
                .collect(Collectors.toList());
    }
}
