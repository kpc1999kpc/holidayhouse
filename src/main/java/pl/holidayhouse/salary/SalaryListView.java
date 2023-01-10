package pl.holidayhouse.salary;

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
import pl.holidayhouse.view.AppLayoutBasic;


@PageTitle("Domki | Summer Holidays")
@Route(value = "/koszty", layout = AppLayoutBasic.class)
public class SalaryListView extends VerticalLayout {
    private SalaryForm form;
    Grid<Salary> grid = new Grid<>(Salary.class);
    TextField filterText = new TextField();
    SalaryService salaryService;

    public SalaryListView(SalaryService salaryService) {
        this.salaryService = salaryService;
        addClassName("salary-list-view");
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
        form.setSalary(null);
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
        form = new SalaryForm();
        form.setWidth("25em");
        form.addListener(SalaryForm.SaveEvent.class, this::saveSalary);
        form.addListener(SalaryForm.DeleteEvent.class, this::deleteSalary);
        form.addListener(SalaryForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteSalary(SalaryForm.DeleteEvent event) {
        salaryService.delete(event.getSalary());
        updateList();
        closeEditor();
    }

    private void saveSalary(SalaryForm.SaveEvent event){
        salaryService.save(event.getSalary());
        updateList();
        closeEditor();
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Wyszukaj");
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addSalaryButton = new Button("Dodaj koszt", new Icon(VaadinIcon.PLUS));
        addSalaryButton.addClickListener(e -> addSalary());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addSalaryButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addSalary() {
        grid.asSingleSelect().clear();
        editSalary(new Salary());
    }

    private void updateList() {
        grid.setItems(salaryService.findAll(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassName("salary-grid");
        grid.setSizeFull();

        grid.setColumns("salary_id", "salary_date", "amount", "comment");
        grid.getColumnByKey("salary_id").setHeader("Numer");
        grid.getColumnByKey("salary_date").setHeader("Data");
        grid.getColumnByKey("amount").setHeader("Kwota");
        grid.getColumnByKey("comment").setHeader("Komentarz");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editSalary(e.getValue()));
    }

    private void editSalary(Salary salary) {
        if(salary == null){
            closeEditor();
        } else {
            form.setSalary(salary);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}