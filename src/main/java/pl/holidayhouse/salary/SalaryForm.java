package pl.holidayhouse.salary;

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
import pl.holidayhouse.employee.Employee;

import java.util.List;

public class SalaryForm extends FormLayout {
    Binder<Salary> binder = new BeanValidationBinder<>(Salary.class);
    TextField salary_id = new TextField("Numer");
    BigDecimalField amount = new BigDecimalField("Kwota");
    DatePicker salary_date = new DatePicker("Data wypłaty");
    TextField comment = new TextField("Komentarz");
    ComboBox<Employee> employee = new ComboBox<>("Pracownik");

    Button save = new Button("Zapisz");
    Button delete = new Button("Usuń");
    Button cancel = new Button("Anuluj");
    private Salary salary;

    public SalaryForm(List<Employee> employees){
        employee.setItems(employees);
        employee.setItemLabelGenerator(e-> e.getName() + " " + e.getSurname());

        addClassName("salary-form");
        binder.forField(salary_id)
                .withNullRepresentation("")
                .withConverter( new StringToLongConverter("salary_id")).bind("salary_id");
        salary_id.setEnabled(false);

        binder.bindInstanceFields(this);

        Component buttonsLayout = createButtonsLayout();

        Div euroSuffix = new Div();
        euroSuffix.setText("PLN");
        amount.setSuffixComponent(euroSuffix);

        add(
            salary_id,
            employee,
            amount,
            salary_date,
            comment,
            buttonsLayout
        );
    }

    public void setSalary(Salary salary){
        this.salary = salary;
        binder.readBean(salary);
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, salary)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);
        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(salary);
            fireEvent(new SaveEvent(this, salary));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class SalaryFormEvent extends ComponentEvent<SalaryForm> {
        private Salary salary;

        protected SalaryFormEvent(SalaryForm source, Salary salary) {
            super(source, false);
            this.salary = salary;
        }

        public Salary getSalary() {
            return salary;
        }
    }

    public static class SaveEvent extends SalaryFormEvent {
        SaveEvent(SalaryForm source, Salary salary) {
            super(source, salary);
        }
    }

    public static class DeleteEvent extends SalaryFormEvent {
        DeleteEvent(SalaryForm source, Salary salary) {
            super(source, salary);
        }

    }

    public static class CloseEvent extends SalaryFormEvent {
        CloseEvent(SalaryForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
