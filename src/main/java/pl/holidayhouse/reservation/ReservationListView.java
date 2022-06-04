package pl.holidayhouse.reservation;

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
import pl.holidayhouse.customer.CustomerService;
import pl.holidayhouse.employee.EmployeeService;
import pl.holidayhouse.house.HouseService;
import pl.holidayhouse.view.AppLayoutBasic;

import java.util.Collections;

@PageTitle("Rezerwacje | Holiday House")
@Route(value = "/rezerwacje", layout = AppLayoutBasic.class)
public class ReservationListView extends VerticalLayout {
    private ReservationForm form;
    Grid<Reservation> grid = new Grid<>(Reservation.class);
    TextField filterText = new TextField();
    ReservationService reservationService;
    EmployeeService employeeService;
    CustomerService customerService;
    HouseService houseService;

    public ReservationListView(ReservationService reservationService, EmployeeService employeeService,
                               CustomerService customerService, HouseService houseService) {
        this.reservationService = reservationService;
        this.employeeService = employeeService;
        this.customerService = customerService;
        this.houseService = houseService;
        addClassName("reservations-list-view");
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
        form.setReservation(null);
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
        form = new ReservationForm(employeeService.findAll(), customerService.findAll(), houseService.findAll());
        form.setWidth("25em");
        form.addListener(ReservationForm.SaveEvent.class, this::saveReservation);
        form.addListener(ReservationForm.DeleteEvent.class, this::deleteReservation);
        form.addListener(ReservationForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteReservation(ReservationForm.DeleteEvent event) {
        reservationService.delete(event.getReservation());
        updateList();
        closeEditor();
    }

    private void saveReservation(ReservationForm.SaveEvent event){
        reservationService.save(event.getReservation());
        updateList();
        closeEditor();
    }
    private Component getToolbar() {
        filterText.setPlaceholder("Wyszukaj");
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addReservationButton = new Button("Dodaj rezerwację", new Icon(VaadinIcon.PLUS));
        addReservationButton.addClickListener(e -> addReservation());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addReservationButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addReservation() {
        grid.asSingleSelect().clear();
        editReservation(new Reservation());
    }

    private void updateList() {
        grid.setItems(reservationService.findAll(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassName("reservation-grid");
        grid.setSizeFull();

        grid.setColumns("reservation_id", "guests_number", "price_per_night", "reservation_status",
                "check_in", "check_out", "reservation_date");
        grid.addColumn(house -> house.getHouse().getHouse_id()).setHeader("Domek");
        grid.addColumn(customer -> customer.getCustomer().getName() + " " + customer.getCustomer().getSurname()).setHeader("Klient");
        grid.addColumn(employee -> employee.getEmployee().getName() + " " + employee.getEmployee().getSurname()).setHeader("Pracownik");
        grid.getColumnByKey("reservation_id").setHeader("Numer");
        grid.getColumnByKey("guests_number").setHeader("Liczba osób");
        grid.getColumnByKey("price_per_night").setHeader("Cena za dobę");
        grid.getColumnByKey("reservation_status").setHeader("Status");
        grid.getColumnByKey("reservation_date").setHeader("Data rezerwacji");
        grid.getColumnByKey("check_in").setHeader("Data zameldowania");
        grid.getColumnByKey("check_out").setHeader("Data wymeldowania");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editReservation(e.getValue()));
    }

    private void editReservation(Reservation reservation) {
        if(reservation == null){
            closeEditor();
        } else {
            form.setReservation(reservation);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}