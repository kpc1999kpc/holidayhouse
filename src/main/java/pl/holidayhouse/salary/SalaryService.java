package pl.holidayhouse.salary;

import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public SalaryService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    public void save(Salary salary){
        if(salary == null){
            System.err.println("Salary is null");
            return;
        }
        salaryRepository.save(salary);
    }

    public List<Salary> findAll() {
        return salaryRepository.findAll();
    }

    public List<Salary> findAll(String filterText) {
        if(filterText == null || filterText.isEmpty()){
            return salaryRepository.findAll();
        } else {
            return salaryRepository.search(filterText);
        }
    }

    public long count() {
        return salaryRepository.count();
    }

    public void delete(Salary salary) {
        salaryRepository.delete(salary);
    }
}

