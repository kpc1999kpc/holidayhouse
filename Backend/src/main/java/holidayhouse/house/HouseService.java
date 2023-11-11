package holidayhouse.house;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
        return houseRepository.save(house);
    }

    public void delete(Long id) {
        houseRepository.deleteById(id);
    }
}
