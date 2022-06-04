package pl.holidayhouse.payment;

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
import pl.holidayhouse.reservation.ReservationService;
import pl.holidayhouse.view.AppLayoutBasic;

@PageTitle("Domki | Holiday House")
@Route(value = "/platnosci", layout = AppLayoutBasic.class)
public class PaymentListView extends VerticalLayout {
    private PaymentForm form;
    Grid<Payment> grid = new Grid<>(Payment.class);
    TextField filterText = new TextField();
    PaymentService paymentService;
    ReservationService reservationService;

    public PaymentListView(PaymentService paymentService, ReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationService = reservationService;
        addClassName("payments-list-view");
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
        form.setPayment(null);
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
        form = new PaymentForm(reservationService.findAll());
        form.setWidth("25em");
        form.addListener(PaymentForm.SaveEvent.class, this::savePayment);
        form.addListener(PaymentForm.DeleteEvent.class, this::deletePayment);
        form.addListener(PaymentForm.CloseEvent.class, e -> closeEditor());
    }

    private void deletePayment(PaymentForm.DeleteEvent event) {
        paymentService.delete(event.getPayment());
        updateList();
        closeEditor();
    }

    private void savePayment(PaymentForm.SaveEvent event){
        paymentService.save(event.getPayment());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Wyszukaj");
        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addPaymentButton = new Button("Dodaj płatność", new Icon(VaadinIcon.PLUS));
        addPaymentButton.addClickListener(e -> addPayment());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addPaymentButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addPayment() {
        grid.asSingleSelect().clear();
        editPayment(new Payment());
    }

    private void updateList() {
        grid.setItems(paymentService.findAll(filterText.getValue()));
    }
    private void configureGrid() {
        grid.addClassName("payment-grid");
        grid.setSizeFull();

        grid.setColumns("payment_id", "payment_date", "amount", "comment");
        grid.addColumn(reservation -> reservation.getReservation().getReservation_id()).setHeader("Rezerwaja");
        grid.getColumnByKey("payment_id").setHeader("Numer");
        grid.getColumnByKey("payment_date").setHeader("Data płatności");
        grid.getColumnByKey("amount").setHeader("Kwota");
        grid.getColumnByKey("comment").setHeader("Komentarz");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editPayment(e.getValue()));
    }

    private void editPayment(Payment payment) {
        if(payment == null){
            closeEditor();
        } else {
            form.setPayment(payment);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}