package pl.holidayhouse.employee;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
public class EmployeeForm extends FormLayout {
    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);
    TextField name = new TextField("Imię");
    TextField surname = new TextField("Nazwisko");
    TextField id_card_number = new TextField("Numer dowodu");
    DatePicker employment_date = new DatePicker("Data zatrudnienia");
    EmailField email = new EmailField("Email");
    TextField phone_number = new TextField("Telefon");
    TextField address = new TextField("Adres");
    TextField account_number = new TextField("Numer rachunku");
    TextField employment_status = new TextField("Status");
    TextField comment = new TextField("Komentarz");

    Button save = new Button("Zapisz");
    Button delete = new Button("Usuń");
    Button cancel = new Button("Anuluj");
    private Employee emploee;

    public EmployeeForm(){
        addClassName("employee-form");
        binder.bindInstanceFields(this);
        Component buttonsLayout = createButtonsLayout();
        add(
            name,
            surname,
            email,
            phone_number,
            id_card_number,
            account_number,
            address,
            employment_date,
            employment_status,
            buttonsLayout
        );

        setResponsiveSteps(new ResponsiveStep("0", 2));

        setColspan(buttonsLayout, 2);
        setColspan(email, 2);
        setColspan(address, 2);
        setColspan(account_number, 2);


    }

    public void setEmployee(Employee employee){
        this.emploee = employee;
        binder.readBean(employee);
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, emploee)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);
        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(emploee);
            fireEvent(new SaveEvent(this, emploee));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {
        private Employee employee;

        protected EmployeeFormEvent(EmployeeForm source, Employee employee) {
            super(source, false);
            this.employee = employee;
        }

        public Employee getEmployee() {
            return employee;
        }
    }

    public static class SaveEvent extends EmployeeFormEvent {
        SaveEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }
    }

    public static class DeleteEvent extends EmployeeFormEvent {
        DeleteEvent(EmployeeForm source, Employee employee) {
            super(source, employee);
        }

    }

    public static class CloseEvent extends EmployeeFormEvent {
        CloseEvent(EmployeeForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
