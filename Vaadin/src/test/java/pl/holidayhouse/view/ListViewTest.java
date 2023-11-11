package pl.holidayhouse.view;

import pl.holidayhouse.employee.EmployeeListView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ListViewTest {

    @Autowired
    private EmployeeListView listView;

    @Test
    public void formShownWhenContactSelected() {

    }

}