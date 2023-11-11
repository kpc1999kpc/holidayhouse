package pl.holidayhouse.customer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class CustomerForm extends FormLayout {
    Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);
    TextField name = new TextField("Imię");
    TextField surname = new TextField("Nazwisko");
    //TextField id_card_number = new TextField("Numer dowodu");
    TextField phone_number = new TextField("Telefon");
    EmailField email = new EmailField("Email");
    //TextField address = new TextField("Adres");
    //TextField nationality = new TextField("Narodowość");
    TextField comment = new TextField("Komentarz");

    Button save = new Button("Zapisz");
    Button delete = new Button("Usuń");
    Button cancel = new Button("Anuluj");
    private Customer customer;

    public CustomerForm(){
        addClassName("customer-form");
        binder.bindInstanceFields(this);
        Component buttonsLayout = createButtonsLayout();
        add(
            name,
            surname,
                phone_number,
                comment,
                email,

            //id_card_number,
            //address,
            //nationality,

            buttonsLayout
        );

        setResponsiveSteps(new ResponsiveStep("0", 2));

        setColspan(buttonsLayout, 2);
        setColspan(email, 2);
        //setColspan(address, 2);
        setColspan(comment, 2);
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
        binder.readBean(customer);
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, customer)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);
        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(customer);
            fireEvent(new SaveEvent(this, customer));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {
        private Customer customer;

        protected CustomerFormEvent(CustomerForm source, Customer customer) {
            super(source, false);
            this.customer = customer;
        }

        public Customer getCustomer() {
            return customer;
        }
    }

    public static class SaveEvent extends CustomerFormEvent {
        SaveEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }
    }

    public static class DeleteEvent extends CustomerFormEvent {
        DeleteEvent(CustomerForm source, Customer customer) {
            super(source, customer);
        }

    }

    public static class CloseEvent extends CustomerFormEvent {
        CloseEvent(CustomerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
