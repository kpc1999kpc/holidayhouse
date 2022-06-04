package pl.holidayhouse.house;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public void save(House house){
        if(house == null){
            System.err.println("House is null");
            return;
        }
        houseRepository.save(house);
    }


    public List<House> findAll(String filterText) {
        if(filterText == null || filterText.isEmpty()){
            return houseRepository.findAll();
        } else {
            return houseRepository.search(filterText);
        }
    }

    public long count() {
        return houseRepository.count();
    }

    public void delete(House house) {
        houseRepository.delete(house);
    }

    public List<House> findAll()  {
        return houseRepository.findAll();
    }
}