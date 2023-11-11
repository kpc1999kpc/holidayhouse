package pl.holidayhouse.house;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.shared.Registration;

public class HouseForm extends FormLayout {
    Binder<House> binder = new BeanValidationBinder<>(House.class);
    TextField house_id = new TextField("Numer");
    TextField status = new TextField("Status");
    TextField comment = new TextField("Komentarz");

    Button save = new Button("Zapisz");
    Button delete = new Button("Usuń");
    Button cancel = new Button("Anuluj");
    private House house;

    public HouseForm(){
        addClassName("house-form");

        addClassName("reservation-form");
        binder.forField(house_id)
                .withNullRepresentation("")
                .withConverter( new StringToLongConverter("house_id")).bind("house_id");
        house_id.setEnabled(false);

        binder.bindInstanceFields(this);
        Component buttonsLayout = createButtonsLayout();
        add(
            house_id,
            status,
            comment,
            buttonsLayout
        );
    }

    public void setHouse(House house){
        this.house = house;
        binder.readBean(house);
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, house)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        delete.addClickShortcut(Key.DELETE);
        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(house);
            fireEvent(new SaveEvent(this, house));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class HouseFormEvent extends ComponentEvent<HouseForm> {
        private House house;

        protected HouseFormEvent(HouseForm source, House house) {
            super(source, false);
            this.house = house;
        }

        public House getHouse() {
            return house;
        }
    }

    public static class SaveEvent extends HouseFormEvent {
        SaveEvent(HouseForm source, House house) {
            super(source, house);
        }
    }

    public static class DeleteEvent extends HouseFormEvent {
        DeleteEvent(HouseForm source, House house) {
            super(source, house);
        }

    }

    public static class CloseEvent extends HouseFormEvent {
        CloseEvent(HouseForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
