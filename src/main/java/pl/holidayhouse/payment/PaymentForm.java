package pl.holidayhouse.payment;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.shared.Registration;
import pl.holidayhouse.reservation.Reservation;

import java.util.List;

public class PaymentForm extends FormLayout {
    Binder<Payment> binder = new BeanValidationBinder<>(Payment.class);
    TextField payment_id = new TextField("Numer");
    DatePicker payment_date = new DatePicker("Data płatności");
    BigDecimalField amount = new BigDecimalField("Kwota");
    TextField comment = new TextField("Komentarz");
    ComboBox<Reservation> reservation = new ComboBox<>("Rezerwacja");

    Button save = new Button("Zapisz");
    Button delete = new Button("Usuń");
    Button cancel = new Button("Anuluj");
    private Payment payment;

    public PaymentForm(List<Reservation> reservations){
        addClassName("payment-form");

        reservation.setItems(reservations);
        reservation.setItemLabelGenerator(e-> String.valueOf(e.getReservation_id()));

        addClassName("reservation-form");
        binder.forField(payment_id)
                .withNullRepresentation("")
                .withConverter( new StringToLongConverter("payment_id")).bind("payment_id");
        payment_id.setEnabled(false);

        binder.bindInstanceFields(this);
        Component buttonsLayout = createButtonsLayout();
        add(
            payment_id,
            payment_date,
            reservation,
            amount,
            comment,
            buttonsLayout
        );
    }

    public void setPayment(Payment payment){
        this.payment = payment;
        binder.readBean(payment);
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, payment)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);
        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(payment);
            fireEvent(new SaveEvent(this, payment));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class PaymentFormEvent extends ComponentEvent<PaymentForm> {
        private Payment payment;

        protected PaymentFormEvent(PaymentForm source, Payment payment) {
            super(source, false);
            this.payment = payment;
        }

        public Payment getPayment() {
            return payment;
        }
    }

    public static class SaveEvent extends PaymentFormEvent {
        SaveEvent(PaymentForm source, Payment payment) {
            super(source, payment);
        }
    }

    public static class DeleteEvent extends PaymentFormEvent {
        DeleteEvent(PaymentForm source, Payment payment) {
            super(source, payment);
        }

    }

    public static class CloseEvent extends PaymentFormEvent {
        CloseEvent(PaymentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
