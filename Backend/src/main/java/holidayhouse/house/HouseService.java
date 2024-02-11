package holidayhouse.house;

import holidayhouse.secutiy.demo.AbstractService;
import holidayhouse.secutiy.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class HouseService extends AbstractService {
    @Autowired
    private HouseRepository houseRepository;

    public List<House> getAllHouses() {
        User user = getLoggedInUser();
        return houseRepository.findByUser(user);
    }

    public House getById(Long id) {
        User loggedInUser = getLoggedInUser();
        return  houseRepository.findByIdAndUser(id, loggedInUser)
                .orElseThrow(() -> new NoSuchElementException("House not found with id " + id + " for the logged-in user"));
    }

    public House addHouse(House house) {
        User loggedInUser = getLoggedInUser();
        house.setUser(loggedInUser);
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
        House house = getById(id);
        houseRepository.delete(house);
    }

    public List<Map<String, String>> getAllHouseNames() {
        User loggedInUser = getLoggedInUser();
        return houseRepository.findByUser(loggedInUser).stream()
                .map(house -> {
                    Map<String, String> houseNameMap = new HashMap<>();
                    houseNameMap.put("name", house.getName().trim()); // Usuń białe znaki na początku i końcu
                    return houseNameMap;
                })
                .collect(Collectors.toList());
    }
}
