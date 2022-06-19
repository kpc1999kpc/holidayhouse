package pl.holidayhouse.reservation;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.shared.Registration;
import pl.holidayhouse.customer.Customer;
import pl.holidayhouse.employee.Employee;
import pl.holidayhouse.employee.EmployeeForm;
import pl.holidayhouse.house.House;

import java.util.List;

public class ReservationForm extends FormLayout {


    Binder<Reservation> binder = new BeanValidationBinder<>(Reservation.class);
    TextField reservation_id = new TextField("Numer");
    IntegerField guests_number = new IntegerField("Liczba gości");
    BigDecimalField price_per_night = new BigDecimalField("Cena za dobę");
    DatePicker reservation_date = new DatePicker("Data rezerwacji");
    DatePicker check_in = new DatePicker("Data przyjazdu");
    DatePicker check_out = new DatePicker("Data wyjazdu");
    TextField reservation_status = new TextField("Status");
    TextField comment = new TextField("Komentarz");
    ComboBox<House> house = new ComboBox<>("Domek");
    ComboBox<Employee> employee = new ComboBox<>("Pracownik");
    ComboBox<Customer> customer = new ComboBox<>("Klient");

    Button save = new Button("Zapisz");
    Button delete = new Button("Usuń");
    Button cancel = new Button("Anuluj");
    private Reservation reservation;

    public ReservationForm(List<Employee> employees, List<Customer> customers, List<House> houses){
        addClassName("reservation-form");

        check_in.addValueChangeListener(e -> check_out.setMin(e.getValue()));
        check_out.addValueChangeListener(e -> check_in.setMax(e.getValue()));


        employee.setItems(employees);
        employee.setItemLabelGenerator(e-> e.getName() + " " + e.getSurname());

        customer.setItems(customers);
        customer.setItemLabelGenerator(e-> e.getName() + " " + e.getSurname());

        house.setItems(houses);
        house.setItemLabelGenerator(e-> String.valueOf(e.getHouse_id()));
        /*
        check_in.addValueChangeListener(e -> updateHouse());
        check_out.addValueChangeListener(e -> updateHouse());


        binder.forField(check_in)
                .withValidator(localDate -> {
                    int freeHouses = reservationService.booked(localDate);
                    boolean validWeekDay = freeHouses >= 1 && freeHouses <= 3;
                    return validWeekDay;
                }, "Ten termin jest zajęty")
                .bind(Reservation::getCheck_in, Reservation::setCheck_in);
        */
        binder.forField(reservation_id)
                .withNullRepresentation("")
                .withConverter( new StringToLongConverter("reservation_id")).bind("reservation_id");
        reservation_id.setEnabled(false);

        binder.bindInstanceFields(this);

        Component buttonsLayout = createButtonsLayout();

        Div euroSuffix = new Div();
        euroSuffix.setText("PLN");
        price_per_night.setSuffixComponent(euroSuffix);

        guests_number.setValue(4);
        guests_number.setHasControls(true);
        guests_number.setMin(0);
        guests_number.setMax(6);


        add(
            reservation_id,
            guests_number,
            price_per_night,
            reservation_date,
            check_in,
            check_out,
            house,
            reservation_status,
            customer,
            employee,
            buttonsLayout
        );

        setResponsiveSteps(new ResponsiveStep("0", 2));
        setColspan(customer, 2);
        setColspan(employee, 2);
        setColspan(buttonsLayout, 2);
    }

    public void setReservation(Reservation reservation){
        this.reservation = reservation;
        binder.readBean(reservation);
    }
    /*
    private void updateHouse() {
        house.setItems(reservationService.checkAvailability(check_in.getValue(), check_in.getValue()));
        house.setItemLabelGenerator(e-> String.valueOf(e.getHouse_id()));
    }*/

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, reservation)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);
        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(reservation);
            fireEvent(new SaveEvent(this, reservation));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class ReservationFormEvent extends ComponentEvent<ReservationForm> {
        private Reservation reservation;

        protected ReservationFormEvent(ReservationForm source, Reservation reservation) {
            super(source, false);
            this.reservation = reservation;
        }

        public Reservation getReservation() {
            return reservation;
        }
    }

    public static class SaveEvent extends ReservationFormEvent {
        SaveEvent(ReservationForm source, Reservation reservation) {
            super(source, reservation);
        }
    }

    public static class DeleteEvent extends ReservationFormEvent {
        DeleteEvent(ReservationForm source, Reservation reservation) {
            super(source, reservation);
        }

    }

    public static class CloseEvent extends ReservationFormEvent {
        CloseEvent(ReservationForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
