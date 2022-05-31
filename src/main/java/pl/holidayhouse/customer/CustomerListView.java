package pl.holidayhouse.customer;

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

@PageTitle("Klienci | Holiday House")
@Route(value = "/klienci", layout = AppLayoutBasic.class)
public class CustomerListView extends VerticalLayout {
    private CustomerForm form;
    Grid<Customer> grid = new Grid<>(Customer.class);
    TextField filterText = new TextField();
    CustomerService customerService;

    public CustomerListView(CustomerService customerService) {
        this.customerService = customerService;
        addClassName("customers-list-view");
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
        form.setCustomer(null);
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
        form = new CustomerForm();
        form.setWidth("25em");
        form.addListener(CustomerForm.SaveEvent.class, this::saveCustomer);
        form.addListener(CustomerForm.DeleteEvent.class, this::deleteCustomer);
        form.addListener(CustomerForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteCustomer(CustomerForm.DeleteEvent event) {
        customerService.delete(event.getCustomer());
        updateList();
        closeEditor();
    }

    private void saveCustomer(CustomerForm.SaveEvent event){
        customerService.save(event.getCustomer());
        updateList();
        closeEditor();
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Wyszukaj");
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addCustomerButton = new Button("Dodaj klienta", new Icon(VaadinIcon.PLUS));
        addCustomerButton.addClickListener(e -> addCustomer());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCustomerButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addCustomer() {
        grid.asSingleSelect().clear();
        editCustomer(new Customer());
    }

    private void updateList() {
        grid.setItems(customerService.findAll(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassName("customer-grid");
        grid.setSizeFull();

        grid.setColumns("name", "surname", "id_card_number", "phone_number", "email", "address", "nationality");
        grid.getColumnByKey("name").setHeader("Imię");
        grid.getColumnByKey("surname").setHeader("Nazwisko");
        grid.getColumnByKey("id_card_number").setHeader("Numer dowodu");
        grid.getColumnByKey("phone_number").setHeader("Telefon");
        grid.getColumnByKey("email").setHeader("Email");
        grid.getColumnByKey("address").setHeader("Adres");
        grid.getColumnByKey("nationality").setHeader("Narodowość");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editCustomer(e.getValue()));
    }

    private void editCustomer(Customer customer) {
        if(customer == null){
            closeEditor();
        } else {
            form.setCustomer(customer);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}