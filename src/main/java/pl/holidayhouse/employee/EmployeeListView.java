package pl.holidayhouse.employee;

import pl.holidayhouse.view.AppLayoutBasic;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
@PageTitle("Pracownicy | Summer Holidays")
@Route(value = "/pracownicy", layout = AppLayoutBasic.class)
public class EmployeeListView extends VerticalLayout {
    private EmployeeForm form;
    Grid<Employee> grid = new Grid<>(Employee.class);
    TextField filterText = new TextField();
    EmployeeService employeeService;

    public EmployeeListView(EmployeeService employeeService) {
        this.employeeService = employeeService;
        addClassName("employees-list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(
            getToolbar(),
            getContent()
        );
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setEmployee(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new EmployeeForm();
        form.setWidth("25em");
        form.addListener(EmployeeForm.SaveEvent.class, this::saveEmployee);
        form.addListener(EmployeeForm.DeleteEvent.class, this::deleteEmployee);
        form.addListener(EmployeeForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteEmployee(EmployeeForm.DeleteEvent event) {
        employeeService.delete(event.getEmployee());
        updateList();
        closeEditor();
    }

    private void saveEmployee(EmployeeForm.SaveEvent event){
        employeeService.save(event.getEmployee());
        updateList();
        closeEditor();
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Wyszukaj");
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addEmployeeButton = new Button("Dodaj pracownika", new Icon(VaadinIcon.PLUS));
        addEmployeeButton.addClickListener(e -> addEmployee());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addEmployeeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addEmployee() {
        grid.asSingleSelect().clear();
        editEmployee(new Employee());
    }

    private void updateList() {
        grid.setItems(employeeService.findAll(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassName("employee-grid");
        grid.setSizeFull();

        grid.setColumns("name", "surname", "id_card_number", "employment_date", "phone_number", "email", "address");
        grid.getColumnByKey("name").setHeader("Imię");
        grid.getColumnByKey("surname").setHeader("Nazwisko");
        grid.getColumnByKey("id_card_number").setHeader("Numer dowodu");
        grid.getColumnByKey("employment_date").setHeader("Data zatrudnienia");
        grid.getColumnByKey("phone_number").setHeader("Telefon");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("address").setHeader("Adres");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editEmployee(e.getValue()));
    }

    private void editEmployee(Employee employee) {
        if(employee == null){
            closeEditor();
        } else {
            form.setEmployee(employee);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}